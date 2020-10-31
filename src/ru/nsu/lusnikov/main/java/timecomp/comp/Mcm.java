package ru.nsu.lusnikov.main.java.timecomp.comp;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mcm extends Compressor {

    public Mcm() throws Exception {
        super("/compressors/msm/mcm.exe");
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "Mcm";
    }

    @Override
    public Path fcompress(File toCompress) throws IllegalArgumentException {

        try {
            workWithExe(comp.toString() + " -m6 " + toCompress.getAbsolutePath() + " " + toCompress.getAbsolutePath() + ".mcm",
                    null, null, true );
            Stream<Path> pat = Files.walk(Paths.get("resources/temp/"));
            List<Path> paths = pat.collect(Collectors.toList());

            for (Path f : paths){
                if (Arrays.asList(f.getFileName().toString().split("\\.")).contains("mcm")) return f;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
