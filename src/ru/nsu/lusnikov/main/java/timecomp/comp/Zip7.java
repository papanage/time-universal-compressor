package ru.nsu.lusnikov.main.java.timecomp.comp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Zip7 extends Compressor {

    public Zip7() throws Exception {
        super("/compressors/7zip/7-Zip/7z.exe");
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
        // System.out.println(comp);
    }

    @Override
    public String toString() {
        return "Zip7";
    }

    @Override
    public Path fcompress(File toCompress) throws IllegalArgumentException {
        try {
            workWithExe(comp.toString()+ " a " + toCompress.getParent() + "\\" + toCompress.getName() + ".7z " + toCompress.getAbsolutePath(), null, null, false);
            Stream<Path> pat = Files.walk(Paths.get("resources/temp/"));
            List<Path> paths = pat.collect(Collectors.toList());

            for (Path f : paths){
                if (Arrays.asList(f.getFileName().toString().split("\\.")).contains("7z")) {
                    return f;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


}
