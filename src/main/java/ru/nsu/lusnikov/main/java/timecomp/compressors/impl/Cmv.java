package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.util.Optional;

public class Cmv extends MainCompressor {

    public Cmv() throws Exception {
        super("compressors/cmv/CMV.exe", "cmv", true, null, " c " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "CMV";
    }

}
