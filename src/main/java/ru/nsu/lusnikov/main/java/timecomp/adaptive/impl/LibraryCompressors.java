package ru.nsu.lusnikov.main.java.timecomp.adaptive.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Cmv;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Lpaq8;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Mcm;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.NanoZip;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Paq8f32;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Tangelo;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Zip7;

import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * point of randevu all TUC system
 * contains info about all compressors
 */
public class LibraryCompressors {
    private static Vector<CompressorBase> compressor = new Vector<>();
    private static volatile LibraryCompressors libraryCompressors;
    private  LibraryCompressors() {
    }

    public static synchronized LibraryCompressors getLibraryCompressors() throws Exception {
        if (libraryCompressors == null) {
            libraryCompressors = new LibraryCompressors();
            init();
        }
        return libraryCompressors;
    }

    public Vector<CompressorBase> baseSet() {
        return new Vector<>(compressor);
    }

    private static void init() throws Exception {
        compressor.add(new Paq8f32());
        compressor.add(new Zip7());
        compressor.add(new NanoZip());
        compressor.add(new Cmv());
        compressor.add(new Lpaq8());
        compressor.add(new Mcm());
        compressor.add(new Tangelo());
    }

    private  CompressorBase getCompressorsByExtension(String extension)  throws NoSuchElementException {
        for (CompressorBase mainCompressor : compressor) {
            if (mainCompressor.toString().equals(extension)) {
                return mainCompressor;
            }
        }
        throw new NoSuchElementException("compressor with extension dont exist in library: " + extension);
    }

    private  CompressorBase getCompressorByPosition(int pos) throws IndexOutOfBoundsException{
        if (pos > compressor.size()) throw new IndexOutOfBoundsException();
        return compressor.get(pos);
    }

    private  Integer getPositionInLibrary(MainCompressor compressorBase) {
        return compressor.indexOf(compressorBase);
    }

}
