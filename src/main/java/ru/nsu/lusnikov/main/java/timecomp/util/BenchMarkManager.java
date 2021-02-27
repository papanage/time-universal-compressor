package ru.nsu.lusnikov.main.java.timecomp.util;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.api.TimeUniversalCompressorAPI;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import static ru.nsu.lusnikov.main.java.timecomp.adaptive.impl.StaticTimeUniversal.startStaticRounds;

@Slf4j
public class BenchMarkManager {

    public BenchMarkManager(){

    }
    /**
     * round schema
     * @param file
     * @throws Exception
     */
    public static Double staticRoundsGeneral(Path file, TimeUniversalCompressorAPI timeUniversalCompressor) throws IOException  {

        /* выбрали лучший из всех на самом деле*/
        CompressFile best = getBest(file, timeUniversalCompressor.getCompressors());

        /* сжали им - засекли время сжатия, по окончанию сжатый удалили */
        long m = System.currentTimeMillis();
        Path temp = best.compressorBase.compress(file, false);
        double timeBestCompress = (double) (System.currentTimeMillis() - m) / 1000.0;
        temp.toFile().delete();

        /* архиватор, которая выбрала схема */
        CompressorBase chosen = timeUniversalCompressor.getBestInList(file);

        /*сжали им - засекли время сжатия, по окончанию сжатый файл удалили*/
        m = System.currentTimeMillis();
        Path temp2 = chosen.compress(file, false);
        long chose_size = temp2.toFile().length();
        double timeChoosenCompress = (double) (System.currentTimeMillis() - m) / 1000.0;
        temp2.toFile().delete();

        /* работа всей схемы самостоятельно - выбрать лучший и сжать им */
        m = System.currentTimeMillis();
        File part = new File("resources/temp/" + file.toFile().getName());
        Files.copy(Paths.get(file.toUri()), Paths.get(part.toURI()));
       // System.out.println(part.getAbsoluteFile());
        File compressed = timeUniversalCompressor.compress(file, false).toFile();
       // System.out.println(compressed.getAbsoluteFile());
        double timeUniveralCompress = (double) (System.currentTimeMillis() - m) / 1000.0;
        compressed.delete();

        System.out.println(file.toFile().getName()
                + ", " + file.toFile().length()
                + ", " + best.compressorBase
                + ", " + chosen
                + ", " + best.size
                + ", " + chose_size
                + ", " + (double)chose_size/ best.size
                + ", " + timeBestCompress
                + ", " + (timeUniveralCompress)
                + ", " + timeChoosenCompress
                );

        return (double)(chose_size - best.size)/ best.size;
    }




    /**
     * schema distances from all compress to best
     * @param file
     * @throws Exception
     */
    public static double staticRoundsFixed(Path file, CompressorBase fix, CompressFile best) throws IOException {
        Vector<CompressorBase> c = new Vector<>();
        c.add(fix);
        CompressFile compressFile = getBest(file, c);
//        System.out.println(file.getName()
//                + ", " + file.length()
//                + ", " + best.compressorBase
//                + ", " + compressFile.compressorBase
//                + ", "  + best.size
//                + ", "  + compressFile.size
//                + ", " + Math.round((double)compressFile.size/ best.size * 10000.0) / 10000.0);
        log.debug("Best {}, Chose {}", best.size, compressFile.size);
        return (double)(compressFile.size - best.size)/ best.size;
    }

    public static CompressFile getBest(Path file, Vector<CompressorBase> compressors) throws IOException {
        Vector<Double> p = new Vector<>();
        p.add(1.0);
        Vector<Integer> ps = new Vector<>();
        ps.add(1);
        return startStaticRounds(compressors, p, ps, file, false);
    }


}
