package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.util.Optional;

public class Mcm extends MainCompressor {

    public Mcm() throws Exception {
        super("/compressors/msm/mcm.exe", "mcm", true, null, " -x11 " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "Mcm";
    }

}
