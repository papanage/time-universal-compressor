package ru.nsu.lusnikov.main.java.timecomp;

import ru.nsu.lusnikov.main.java.timecomp.comp.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.comp.CompressorBase;
import ru.nsu.lusnikov.main.java.timecomp.fcm.FileCompressManager;

import java.io.File;
import java.util.Vector;

public class BechMarkManager {

    public BechMarkManager(){


    }

    /**
     * round schema
     * @param file
     * @param compressors
     * @param per
     * @param places
     * @throws Exception
     */
    public static void startTest(File file, Vector<CompressorBase> compressors, Vector<Double> per, Vector<Integer> places) throws Exception {

        FileCompressManager fileCompressManager = new FileCompressManager(file);
        CompressFile best = getBest(file, compressors);
        CompressFile chosen = fileCompressManager.start(compressors, per, places);
        Vector<CompressorBase> c = new Vector<>();
        c.add(chosen.compressorBase);
        chosen = getBest(file, c);
        System.out.println(file.getName()
                + ", " + file.length()
                + ", " + best.compressorBase
                + ", " + chosen.compressorBase
                + ", " + best.size
                + ", " + chosen.size
                + ", " + (double)chosen.size/ best.size );
    }

    /**
     * schema distances from all compress to best
     * @param file
     * @throws Exception
     */
    public static void startTest2(File file,  CompressorBase fix, CompressFile best) throws Exception {
        Vector<CompressorBase> c = new Vector<>();
        c.add(fix);
        CompressFile compressFile = getBest(file, c);
        System.out.println(file.getName()
                + ", " + file.length()
                + ", " + best.compressorBase
                + ", " + compressFile.compressorBase
                + ", "  + best.size
                + ", "  + compressFile.size
                + ", " + Math.round((double)compressFile.size/ best.size * 10000.0) / 10000.0);
    }

    public static CompressFile getBest(File file, Vector<CompressorBase> compressors) throws Exception {
        Vector<Double> p = new Vector<>();
        p.add(1.0);
        Vector<Integer> ps = new Vector<>();
        ps.add(1);
        FileCompressManager fileCompressManager = new FileCompressManager(file);
        return fileCompressManager.start(compressors, p, ps);
    }


    /**
     * test with dynamical places
     * @param file
     * @param compressors
     * @param per
     * @throws Exception
     */
    public static void startTest3(File file, Vector<CompressorBase> compressors, Vector<Double> per) throws Exception {

        FileCompressManager fileCompressManager = new FileCompressManager(file);
        CompressFile best = getBest(file, compressors);
        //System.out.println("wtf = " + best.size);
        CompressFile chosen = fileCompressManager.startDynamicalRounds(compressors, per);
        Vector<CompressorBase> c = new Vector<>();
        c.add(chosen.compressorBase);
        chosen = getBest(file, c);
        System.out.println(file.getName()
                + ", " + file.length()
                + ", " + best.compressorBase
                + ", " + chosen.compressorBase
                + ", " + best.size
                + ", " + chosen.size
                + ", " + (double)chosen.size/ best.size
        );
    }

}
