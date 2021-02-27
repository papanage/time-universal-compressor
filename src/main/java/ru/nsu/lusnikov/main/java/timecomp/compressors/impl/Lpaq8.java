package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.util.Optional;

public class Lpaq8 extends MainCompressor {

    public Lpaq8() throws Exception {
        super("compressors/lpaq8/lpaq8.exe", "lpaq8", true, null, " 6 " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "Lpaq8";
    }

}
