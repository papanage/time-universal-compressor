package ru.nsu.lusnikov.main.java.timecomp.compressors.commons;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class MainCompressor  extends Compressor {

    protected Path delete = Paths.get("resources/runt/").toAbsolutePath();
    protected String extension;
    protected Boolean isNeedFlush;
    protected File folderExecutaion;
    protected String args;
    protected String run;

    public MainCompressor(
            String load,
            String extension,
            Boolean isNeedFlush,
            File folderExecutaion,
            String args) throws Exception {
        super(load);
        if (Optional.ofNullable(comp).isPresent()) isLoad = true;
        this.extension = extension;
        this.isNeedFlush = isNeedFlush;
        this.folderExecutaion = folderExecutaion;
        this.args = args;
    }

    protected String runGen(File toCompress) {
        return comp.toString() + args + toCompress.getAbsolutePath() + " " + toCompress.getAbsolutePath() + "." + extension;
    }


    protected String runDecompress(File toCompress) {
        return comp.toString() + " -d " + toCompress.getAbsolutePath() + " " + delete;
    }

    @Override
    public String toString() {
        return extension;
    }

    @Override
    public Path fcompress(Path toCompress, boolean isCopy) throws IllegalArgumentException {
        try {

            try {
                Files.delete(Paths.get(toCompress.toFile().getAbsolutePath() + "." + extension));
            } catch (Exception ignored){}

            workWithExe(runGen(toCompress.toFile()), null, folderExecutaion, isNeedFlush);
            if (toCompress.getParent() == null) return null;
            Stream<Path> pat = Files.walk(toCompress.getParent());
            List<Path> paths = pat.collect(Collectors.toList());
            for (Path f : paths) {
                if (Arrays.asList(f.getFileName().toString().split("\\.")).contains(extension)) {
                    return f;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Path fdecompress(Path toCompress) throws IllegalArgumentException {
            return null;
    }
}
