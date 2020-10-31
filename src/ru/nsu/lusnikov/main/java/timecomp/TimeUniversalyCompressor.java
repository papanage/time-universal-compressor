package ru.nsu.lusnikov.main.java.timecomp;

import ru.nsu.lusnikov.main.java.timecomp.comp.*;
import ru.nsu.lusnikov.main.java.timecomp.fcm.FileCompressManager;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class TimeUniversalyCompressor {
    public static void main(String[] args) throws Exception{

        Vector<CompressorBase> compressor = new Vector<>();
        compressor.add(new Paq8f32());
        compressor.add(new Zip7());
        compressor.add(new NanoZip());
        compressor.add(new Cmv());
        compressor.add(new Lpaq8());
        compressor.add(new Mcm());
        compressor.add(new Tangelo());
        compressor.add(new WinRar());

        Vector<Double> per = new Vector<>();
        per.add(0.01);
        per.add(0.05);

        Vector<Integer> places = new Vector<>();
        places.add(3);
        places.add(1);

        TimeUniversalyCompressor timeUniversalyCompressor = new TimeUniversalyCompressor();
        timeUniversalyCompressor.test(compressor, per, places);


    }

    public void test2(Vector<CompressorBase> compressor) throws Exception {

        HashMap<File, CompressFile> compressFileHashMap = new HashMap<>();
        for (CompressorBase fix : compressor) {
            System.out.println(fix);
            System.out.println("name, size, best, fix, best_len, fix_len, ratio");
            File file = new File("resources/corps/main/");
            for (File f : file.listFiles()) {
                if (!compressFileHashMap.containsKey(f)) {
                    compressFileHashMap.put(f, BechMarkManager.getBest(f,compressor));
                }
                BechMarkManager.startTest2(f, fix, compressFileHashMap.get(f));
            }
            File file2 = new File("resources/corps/artific/");
            for (File f : file2.listFiles()) {
                if (!compressFileHashMap.containsKey(f)) {
                    compressFileHashMap.put(f, BechMarkManager.getBest(f,compressor));
                }
                BechMarkManager.startTest2(f, fix, compressFileHashMap.get(f));
            }
            File file3 = new File("resources/corps/calgery/");
            for (File f : file3.listFiles()) {
                if (!compressFileHashMap.containsKey(f)) {
                    compressFileHashMap.put(f, BechMarkManager.getBest(f,compressor));
                }
                BechMarkManager.startTest2(f, fix, compressFileHashMap.get(f));
            }
        }
    }
    public void test(Vector<CompressorBase> compressor, Vector<Double> per, Vector<Integer> places) throws Exception {

        File file = new File("resources/corps/main");
        System.out.println("name, size, best, choose,  ratio");
            for (File f : file.listFiles()) {
                System.out.println(f.getName());
                BechMarkManager.startTest3(f, compressor, per);
           }
    }

    public void test3(Vector<CompressorBase> compressor, Vector<Double> per, Vector<Integer> places) throws Exception {

        File file = new File("resources/corps/main");
        System.out.println("name, size, best, choose,  ratio");
         for (File f : file.listFiles()) {
            //System.out.println(f.getName());
            BechMarkManager.startTest(f, compressor, per, places);
        }
    }

}
