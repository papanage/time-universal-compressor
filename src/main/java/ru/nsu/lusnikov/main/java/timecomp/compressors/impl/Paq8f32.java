package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.io.*;
import java.util.*;

public class Paq8f32 extends MainCompressor {

    @Override
    protected String runGen(File toCompress) {
        return comp.toString() + args + toCompress.getAbsolutePath();
    }

    public Paq8f32() throws Exception {
        super("/compressors/paq8f/paq8f.exe", "paq8f", true, null, " " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "Paq8f32";
    }
}
