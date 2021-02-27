package ru.nsu.lusnikov.main.java.timecomp.compressors.commons;

import java.io.File;
import java.nio.file.Path;

/**
 * Интерфейс архиватора
 */
public interface CompressorBase {
    /**
     * Сжать файл
     * @param toCompress файл для сжатия
     * @param isCopy
     * @return
     * @throws IllegalArgumentException
     */
    Path compress(Path toCompress, boolean isCopy) throws IllegalArgumentException;

    /**
     * Расжать файл
     * @param toCompress файл для сжатия
     * @return
     * @throws IllegalArgumentException
     */
    Path decompress(Path toCompress) throws IllegalArgumentException;
}
