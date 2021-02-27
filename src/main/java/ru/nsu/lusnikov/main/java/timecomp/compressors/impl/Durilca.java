package ru.nsu.lusnikov.main.java.timecomp.compressors.impl;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.MainCompressor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Durilca extends MainCompressor {

    public Durilca() throws Exception {
        super("/compressors/durilca/DURILCA.exe", "dur", false, new File("resources/compressors/durilca/"), " e " );
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
    }

    @Override
    public String toString() {
        return "DUR";
    }

    @Override
    protected String runGen(File toCompress) {
        return comp.toString() + args + toCompress.getName();
    }

    @Override
    public Path fcompress(Path toCompress, boolean isCopy) throws IllegalArgumentException {
            File file = new File("resources/compressors/durilca/"+ toCompress.getFileName());
            File res = new File("resources/temp/"+ toCompress.getFileName() + ".dur");
        try {
            Files.copy(toCompress, file.toPath());
            workWithExe(runGen(toCompress.toFile()), null, folderExecutaion, isNeedFlush);
            Stream<Path> pat = Files.walk(Paths.get(file.getParent()));
            List<Path> paths = pat.collect(Collectors.toList());
            for (Path f : paths) {
                if (Arrays.asList(f.getFileName().toString().split("\\.")).contains(extension)) {
                    Files.copy(f, res.toPath());
                    f.toFile().delete();
                    break;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        file.delete();
        return res.toPath();
    }
}
