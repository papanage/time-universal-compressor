package ru.nsu.lusnikov.main.java.timecomp;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.api.TimeUniversalCompressorAPI;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.impl.DynamicTimeUniversal;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.impl.StaticTimeUniversal;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Lpaq8;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Mcm;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.NanoZip;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Tangelo;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Zip7;
import ru.nsu.lusnikov.main.java.timecomp.util.BenchMarkManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
@Slf4j
public class TimeUniversalyCompressor {
    public static void main(String[] args) throws Exception{

        Vector<CompressorBase> compressor = new Vector<>();



        compressor.add(new NanoZip());
        compressor.add(new Lpaq8());
        compressor.add(new Mcm());
        compressor.add(new Tangelo());
        compressor.add(new Zip7());




        Vector<Double> per = new Vector<>();
        per.add(0.01);
        per.add(0.09);
        per.add(0.1);

        Vector<Integer> places = new Vector<>();
        places.add(5);
        places.add(2);
        places.add(1);

        StaticTimeUniversal staticTimeUniversal = new StaticTimeUniversal();

        File[] list = {
                new File("resources/runt/"),
                 //new File("resources/corps/main/"),
                //new File("resources/corps/calgery/")
        };

        staticTimeUniversal.setPercentsForArchive(per);
        staticTimeUniversal.setPlaces(places);
        staticTimeUniversal.setCompressors(compressor);

        TimeUniversalyCompressor timeUniversalyCompressor = new TimeUniversalyCompressor();
        DynamicTimeUniversal dynamicTimeUniversal = new DynamicTimeUniversal();
        dynamicTimeUniversal.setCompressors(compressor);
        timeUniversalyCompressor.getTablesWithDynamicalRound(staticTimeUniversal, list);
        timeUniversalyCompressor.getTableWithFixedCompressor(compressor, list);


    }

    /**
     * show metric about all compress
     * @param compressor
     * @throws Exception
     */
    public void getTableWithFixedCompressor(Vector<CompressorBase> compressor, File[] list) throws Exception {
        HashMap<File, CompressFile> compressFileHashMap = new HashMap<>();
        for (CompressorBase fix : compressor) {
            double error = 0;
            int c = 0;
            log.debug(fix.toString());
            log.debug("name, size, best, fix, best_len, fix_len, ratio");

            for (File file : list) {
                for (File f : file.listFiles()) {
                    if (!compressFileHashMap.containsKey(f)) {
                        compressFileHashMap.put(f, BenchMarkManager.getBest(f.toPath(), compressor));
                    }
                    error += BenchMarkManager.staticRoundsFixed(f.toPath(), fix, compressFileHashMap.get(f));
                    c++;
                }
            }
            log.info("Фиксируя данный архиватор {} получаем {}", fix, error/c);
        }
    }



    public void justCompress(TimeUniversalCompressorAPI timeUniversalCompressorAPI) {
        File[] file = {
                // new File("resources/corps/calgery"),
                //  new File("resources/corps/artific/"),
                new File("resources/corps/main/")
        };
        for (File f : file) {
            for (File file1 : f.listFiles()) {
                File compressed = timeUniversalCompressorAPI.compress(file1.toPath(), false).toFile();
                compressed.delete();
            }
        }

    }

    public void getTablesWithDynamicalRound(TimeUniversalCompressorAPI timeUniversalCompressorAPI, File[] file) {

        double error = 0;
        int c = 0;
        System.out.println("name, size, best, choose,  best_size, choose_size, ratio, best_time, tuc_time, choose_time");
        for (File f : file) {
            for (File file1 : f.listFiles()) {
                try {
                    System.out.println(f);
                    error += BenchMarkManager.staticRoundsGeneral(file1.toPath(),  timeUniversalCompressorAPI);
                    c++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("Main Error : {}", error/c);

    }

}
