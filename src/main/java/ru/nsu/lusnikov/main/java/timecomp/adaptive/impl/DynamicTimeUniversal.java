package ru.nsu.lusnikov.main.java.timecomp.adaptive.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.api.TimeUniversalCompressorAPI;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.commons.AnalyseUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static ru.nsu.lusnikov.main.java.timecomp.util.FileCompressManager.compressOneRound;
import static ru.nsu.lusnikov.main.java.timecomp.util.FileCompressManager.deleteTemp;

/**
 * динамическая раундовая схема.
 * Состоит из раундов, на каждом раунде все архиваторы
 * сжимают определнный процент текста {@link DynamicTimeUniversal:percentsForArchive}, в
 * следующий раунд проходит некоторая часть архиваторов - кто лучше всего сжал, количество
 * мест определяется на каждом раунде индивудуально (в этой имплементации с помощью k-means алгоритма)
 */
@Getter
@Setter
@Slf4j
public class DynamicTimeUniversal extends TimeUniversalCompressorAPI {
    /**
     * Массив с процентом для сжатия на каждом раунде
     */
    Vector<Double> percentsForArchive = new Vector<>();

    Double delta;

    @Override
    public CompressorBase getBestInList(Path file) {
        CompressFile chosen = null;
        try {
            chosen = startDynamicalRounds(compressors, file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chosen.compressorBase;
    }

    public static CompressFile startDynamicalRounds(Vector<CompressorBase> compressor, Path file, boolean isCopy) throws IOException{
        List<CompressorBase> c = new ArrayList<>(compressor);
        double delta = 0.0;
        double p = 0.05;
        Vector<CompressFile> compressFiles = new Vector<>();
        do {
            if (c.size() == 1) {
                delta += p * c.size();
                log.debug("delta = " + delta);
                deleteTemp(compressFiles.get(0).file.toFile().getParent());
                return compressFiles.get(0);
            }
            compressFiles = compressOneRound(c, p, file, isCopy);
            c.clear();
            c.addAll(AnalyseUtils.getBest(compressFiles));
            deleteTemp(compressFiles.get(0).file.toFile().getParent());
            delta += p * c.size();
            if (p < 0.1) p += 0.01;
        } while (true);
    }
}

