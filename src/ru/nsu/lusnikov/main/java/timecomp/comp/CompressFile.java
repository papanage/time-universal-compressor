package ru.nsu.lusnikov.main.java.timecomp.comp;

import java.io.File;

public class CompressFile {
    public Long size;
    public File file;
    public CompressorBase compressorBase;

    public CompressFile(File file, CompressorBase compressorBase, Long size) {
        this.file = file;
        this.compressorBase = compressorBase;
        this.size = size;
    }

    @Override
    public String toString() {
        return "CompressFile{" +
                "file size =" + size +
                ", compressorBase=" + compressorBase +
                '}';
    }
}
