package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.io.*;
import java.util.Optional;

public class NanoZip extends MainCompressor {

    public NanoZip() throws Exception {
        super("compressors/nanozip/nz.exe", "nz", false, null, " a -cO " );
        setFolderExecutaion(comp.getParentFile());
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "NanoZip";
    }

    @Override
    protected String runGen(File toCompress) {
        return comp.toString() + args + toCompress.getAbsolutePath() + ".nz " + toCompress.getAbsolutePath();
    }


}
