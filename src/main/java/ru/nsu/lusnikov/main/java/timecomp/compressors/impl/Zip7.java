package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.io.*;
import java.util.Optional;

public class Zip7 extends MainCompressor {

    public Zip7() throws Exception {
        super("/compressors/7zip/7-Zip/7z.exe", "7z", false, null, " a " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
        // System.out.println(comp);
    }

    @Override
    public String toString() {
        return "Zip7";
    }

    @Override
    protected String runGen(File toCompress) {
        return comp.toString()+ args + toCompress.getParent() + "\\" + toCompress.getName() +  "."+extension+ " " + toCompress.getAbsolutePath();
    }


}
