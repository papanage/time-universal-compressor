package ru.nsu.lusnikov.main.java.timecomp.seacher;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.Compressor;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Lpaq8;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Mcm;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.NanoZip;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Tangelo;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Zip7;
import ru.nsu.lusnikov.main.java.timecomp.util.FileCompressManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Searcher  - класс дял поиска лучшей статической
 * раундовой схемы с данными краевыми условиями
 */

@Slf4j
@RequiredArgsConstructor
public class Searcher {

    @NonNull
    Vector<Compressor> compressors;
    /**
     * путь к файлу, в котором хранится информация по архивации
     */
    private static final String GENERATE_INFO = "resources/searcher/generate";
    /**
     * Специальный класс, отвечающий за всю логику
     * поиска лучшей схемы
     */
    ParameterOptimization parameterOptimization = new ParameterOptimization();

    /**
     * Получаем лучшую статическую раундовую схему
     * @param files
     * @return
     * @throws IOException
     */
    public StaticSchema getBest(Path[] files) throws IOException {
        generate(files, compressors);
        parameterOptimization.optimizeStaticSchema(10, 34, 5, compressors);
        return parameterOptimization.bestSchema;
    }

    public StaticSchema getBest() throws IOException {
        parameterOptimization.optimizeStaticSchema(10, 34, 5, compressors);
        return parameterOptimization.bestSchema;
    }

    private void generate(Path[] files, Vector<Compressor> compressor) throws IOException {

        log.trace("FIX {}", FileCompressManager.fixRandom);
        OutputStreamWriter writer = (new OutputStreamWriter(new FileOutputStream(GENERATE_INFO), StandardCharsets.UTF_8));
        List<BookInfo> bookInfos = new ArrayList<>();

        for (Path file : files) {
            if (file.toFile().listFiles() != null)
            for (File f : file.toFile().listFiles()) {
                BookInfo bookInfo = TableGenerator.generateTable(1, 10, 1, f.toPath(), compressor);
                bookInfos.add(bookInfo);
                System.out.println(bookInfo);
            }
        }
        Gson gson = new Gson();
        writer.write(gson.toJson(bookInfos));
        writer.flush();
    }

    public static void main(String[] args) throws Exception {

        Vector<Compressor> compressor = new Vector<>();

        compressor.add(new NanoZip());
        compressor.add(new Lpaq8());
        compressor.add(new Mcm());
        compressor.add(new Tangelo());
        compressor.add(new Zip7());

        Searcher searcher = new Searcher(compressor);
        ParameterOptimization.setBOOKS_INFO(Searcher.GENERATE_INFO);
        Path[] list = {
                new File("resources/runt/").toPath(),
                // new File("resources/corps/artific/"),
                //new File("resources/corps/calgery/")
        };


        //searcher.getBest(list);
        searcher.getBest();
    }
}
