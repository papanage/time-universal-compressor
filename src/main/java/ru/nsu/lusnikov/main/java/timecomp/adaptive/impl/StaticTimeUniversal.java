package ru.nsu.lusnikov.main.java.timecomp.adaptive.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.api.TimeUniversalCompressorAPI;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static ru.nsu.lusnikov.main.java.timecomp.util.FileCompressManager.compressOneRound;
import static ru.nsu.lusnikov.main.java.timecomp.util.FileCompressManager.deleteTemp;

/**
 * Статическая раундовая схема.
 * Состоит из раундов, на каждом раунде все архиваторы
 * сжимают определнный процент текста {@link StaticTimeUniversal:percentsForArchive}, в
 * следующий раунд проходит некоторая часть архиваторов - кто лучше всего сжал, количество
 * мест в {@link StaticTimeUniversal:places}
 */
@Getter
@Setter
@Slf4j
public class StaticTimeUniversal extends TimeUniversalCompressorAPI {
    /**
     * Массив с процентом для сжатия на каждом раунде
     */
    Vector<Double> percentsForArchive = new Vector<>();
    /**
     * Массив с местами(квотами)
     */
    Vector<Integer> places = new Vector<>();
    

    public Double getDelta() {
        double delta = 0;
        for (int i = 0; i < percentsForArchive.size(); i++) {
            delta += percentsForArchive.get(i) * places.get(i);
        }
        return delta+ 1.0;
    }

    @Override
    public CompressorBase getBestInList(Path file) {

        log.debug("Начало выбора лучшего архиватора");
        CompressFile chosen = null;
        try {
            chosen = startStaticRounds(compressors, percentsForArchive, places, file, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Выбо окончен, выбран: " + chosen.compressorBase.toString());
        return chosen.compressorBase;
    }

    public static CompressFile startStaticRounds(
            Vector<CompressorBase> compressor, Vector<Double> per, Vector<Integer> places, Path file, boolean isCopy) throws IOException {
        List<CompressorBase> c = new ArrayList<>(compressor);
        int pl_index = 0;
        Vector<CompressFile> compressFiles = new Vector<>();
        for (Double p : per) {
            if (compressFiles.size() == 1)  {
                break;
            }
            compressFiles = compressOneRound(c, p, file, isCopy);
            c.clear();
            for (CompressFile compressFile : compressFiles.subList(0, places.get(pl_index++))){
                c.add(compressFile.compressorBase);
            }
            deleteTemp("resources/temp");
        }
        //   System.out.println(compressFiles.get(0).compressorBase + " " + compressFiles.get(0).size);
        return compressFiles.get(0);
    }

}
