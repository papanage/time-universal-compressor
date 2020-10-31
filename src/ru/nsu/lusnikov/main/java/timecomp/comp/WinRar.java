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

public class WinRar extends Compressor {

    public WinRar() throws Exception {
        super("/compressors/WinRAR/rar.exe");
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
        // System.out.println(comp);
    }

    @Override
    public String toString() {
        return "WinRar";
    }

    @Override
    public Path fcompress(File toCompress) throws IllegalArgumentException {
        try {
            //System.out.println(comp.toString()+ " a " + toCompress.getAbsolutePath() + ".rar " + toCompress.getAbsolutePath());
            workWithExe(comp.toString()+ " a " + toCompress.getAbsolutePath()  + ".rar " + toCompress.getAbsolutePath(), null, null, false);
            Stream<Path> pat = Files.walk(Paths.get("resources/temp/"));
            List<Path> paths = pat.collect(Collectors.toList());

            for (Path f : paths){
                if (Arrays.asList(f.getFileName().toString().split("\\.")).contains("rar")) {
                    return f;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
