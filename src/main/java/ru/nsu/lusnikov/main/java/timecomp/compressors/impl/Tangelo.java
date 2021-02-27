package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.util.Optional;

public class Tangelo extends MainCompressor {

    public Tangelo() throws Exception {
        super("/compressors/tangelo/tangelo.exe", "tangelo", false, null, " c  " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "Tangelo";
    }

}

