package ru.nsu.lusnikov.main.java.timecomp.adaptive.api;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

/**
 * Общий интерфейс для адаптивных архиваторов.
 * Архивация из двух этапов
 * 1) Предсказание: Выбор лучшего из массива архиваторов
 * 2) Непосредственно сжатие выбранным архиватором
 */
@Getter
@Setter
@Slf4j
public abstract class TimeUniversalCompressorAPI implements CompressorBase{
    /**
     * Массив архиваторов, из которых выбирается лучший
     */
    protected Vector<CompressorBase> compressors;

    @Override
    public Path compress(Path file, boolean isCopy) throws IllegalArgumentException {
        int pos = compressors.indexOf(getBestInList(file));
        if (pos > compressors.size()) throw new IllegalArgumentException("position of compressor more than size of vector compressor");
        return Paths.get(String.valueOf(compressors.get(pos).compress(file, isCopy)));
    }

    @Override
    public Path decompress(Path file) {
        return null;
    }

    /**
     * Выбор лучшего из массива архиваторов
     * @param file
     * @return
     */
    abstract public CompressorBase getBestInList(Path file);
}
