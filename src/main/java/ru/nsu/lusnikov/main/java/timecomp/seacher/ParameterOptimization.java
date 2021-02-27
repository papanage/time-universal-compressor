package ru.nsu.lusnikov.main.java.timecomp.seacher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.Compressor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Slf4j
public class ParameterOptimization {
    double minError = Double.MAX_VALUE;
    StaticSchema bestSchema;
    public static int MAX_PERCENT = 10;
    @Setter
    public static String BOOKS_INFO = "resources/control1.txt";
    public static String OUT = "resources/out.csv";


    public StaticSchema optimizeStaticSchema(int maxRound, double maxDelta, int compressorCount, Vector<Compressor> compressor) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(BOOKS_INFO));
        Type listType = new TypeToken<ArrayList<BookInfo>>(){}.getType();
        List<BookInfo> bookInfos = new Gson().fromJson(reader, listType);
        reader.close();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(OUT), StandardCharsets.UTF_8);

        StaticSchema staticSchema = new StaticSchema(compressorCount);

        setRound(0, 1,  compressorCount, staticSchema, writer, bookInfos);
        log.info("Теоретическая оценка архиватора: {}", computeAlphaValue(bookInfos, bestSchema));
        log.info("Лучшая схема : {}", bestSchema);
        log.info("Оценка схемы : {}", minError);
        List<String> compressors = new ArrayList<>();

        compressor.forEach(s -> compressors.add(s.toString()));

        computeBetaValue(bookInfos, compressors);
        return null;
    }

    /**
     * Записывает в файл рекурсивно все раундовые схемы
     * по стратегии "гладкого спуска" и выбирает лучшую схему
     * @param num номер раунда
     * @param per проуент дял сжатия
     * @param quotas места (квоты)
     * @param staticSchema1 предыдущий райнд раундовая схема
     * @param writer куда записывать
     * @param info инорфмация о сжатиях
     * @throws IOException
     */
    public void setRound(int num, int per, int quotas, StaticSchema staticSchema1,  OutputStreamWriter writer, List<BookInfo> info ) throws IOException {
        for (int j = quotas; j >= 1; j--) {
            for (int k = per; k <= MAX_PERCENT; k++) {
                StaticSchema staticSchema = new StaticSchema(staticSchema1);

                if (num != 0) {
                    if (j == 1) {
                        staticSchema.getRounds().put(num, new StaticSchema.RoundInfo(k, 1));
                        writer.write("I " + staticSchema+  "\n");
                        roundsUp(staticSchema, info);
                        writer.flush();
                        return;
                    }
                    if (staticSchema.getDeltaWithNewRound(k) > 0.45) {
                        writer.write("D " + staticSchema +  "\n");
                        writer.flush();
                        roundsUp(staticSchema, info);
                        return;

                    }

                    staticSchema.getRounds().put(num, new StaticSchema.RoundInfo(k, j));
                    staticSchema.getRounds().put(num + 1, new StaticSchema.RoundInfo(k, 1));
                    writer.write("N " + staticSchema +  "\n");
                    writer.flush();
                    roundsUp(staticSchema, info);
                    setRound(num+1, k+1, j-1, staticSchema, writer, info);
                }
                else {
                    setRound(num+1, k, j, staticSchema, writer, info);
                }

            }
        }
    }

    /**
     * Вычисляет теоретическую оценку
     * @param bookInfos
     */
    private Double computeAlphaValue(List<BookInfo> bookInfos, StaticSchema schema) {

        double alphaValue = 0;
        for (BookInfo bookInfo : bookInfos) {
            log.debug(bookInfo.name);
            alphaValue += getPartAlphaValue(bookInfo, schema);
        }
       return alphaValue/bookInfos.size();
    }

    private Double getPartAlphaValue(BookInfo bookInfo, StaticSchema schema) {
        Map.Entry<String , Long> res = computeRounds(bookInfo, schema);
        Map<String , Long> map = bookInfo.getInfo().get(100);
        long result = map.get(res.getKey());
        List<Map.Entry<String , Long>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        long best =  list.get(0).getValue();
      //  log.info("BEST {}, RES {}",  best, result);
        return (double)(result - best) / best;
    }


    private Map.Entry<String , Long> computeRounds(BookInfo bookInfo, StaticSchema schema) {

        List<Map.Entry<String , Long>> res = new ArrayList<>();
        for (int i = 1; i <= schema.getRounds().size() ; i++) {
            int per =  schema.getRounds().get(i).getPercent();
            int q =  schema.getRounds().get(i).getQuotas();
            Map<String , Long> map = bookInfo.getInfo().get(per);

            if (i == 1) {
                res = new ArrayList<>(map.entrySet());
            }

            List<Map.Entry<String , Long>> list = new ArrayList<>(map.entrySet());
            List<Map.Entry<String , Long>> temp = new ArrayList<>();
            for (Map.Entry<String, Long> stringLongEntry : list) {
                for (Map.Entry<String, Long> re : res) {
                    if (re.getKey().equals(stringLongEntry.getKey())) {
                        temp.add(stringLongEntry);
                        break;
                    }
                }
            }
            list = temp;
            list.sort(Map.Entry.comparingByValue());

            list = list.subList(0, q);
            log.debug("{}", list);
            res = list;
        }
        return res.get(0);
    }

    /**
     * сравнивает теоритические оценки и выставляет лучшую схему
     * @param schema
     * @param bookInfos
     */
    private void roundsUp(StaticSchema schema, List<BookInfo> bookInfos) {
    //    log.trace("{}", schema);

        double alphaValue;

        if (schema.getRounds().size() == 0) return;

        alphaValue = computeAlphaValue(bookInfos, schema);

        if (alphaValue < minError) {
            minError = alphaValue;
            bestSchema = new StaticSchema(schema);
            log.info("New best schema {}", bestSchema);
        }
        log.debug("{}", alphaValue);
    }

    /**
     * Вычисляет практическую оценку
     * @param bookInfos
     * @param compress
     */
    public void computeBetaValue(List<BookInfo> bookInfos, List<String> compress) {

        long result;
         for(String c : compress) {
             double betaValue = 0;
             for (BookInfo bookInfo : bookInfos) {
                 Map<String , Long> map = bookInfo.getInfo().get(100);
                 result = map.get(c);
                 List<Map.Entry<String , Long>> list = new ArrayList<>(map.entrySet());
                 list.sort(Map.Entry.comparingByValue());
                 Long best = list.get(0).getValue();
                 log.trace("BOOK {} ,BEST {}, RES {}",  bookInfo.name, best, result);
                 betaValue += (double)(result - best) / best;
         }
             betaValue = betaValue/bookInfos.size();
             log.info("Фиксируя архиватор: {},  получаем для него оценку {}", c, betaValue);
        }

    }
}
