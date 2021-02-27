package ru.nsu.lusnikov.main.java.timecomp.compressors.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.nio.file.Path;

@AllArgsConstructor
@Data
public class CompressFile {
    /**
     * размер файла после сжатия
     */
    public Long size;
    /**
     * Файл, после сжатия
     */
    public Path file;
    /**
     * Архиватор, каким сжимали
     */
    public CompressorBase compressorBase;

}
