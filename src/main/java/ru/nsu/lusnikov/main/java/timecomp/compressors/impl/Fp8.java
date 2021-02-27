package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.util.Optional;

public class Fp8 extends MainCompressor {

    public Fp8() throws Exception {
        super("/compressors/fp8/fp8.exe", "fp8", true, null, " " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "fp8";
    }

}
