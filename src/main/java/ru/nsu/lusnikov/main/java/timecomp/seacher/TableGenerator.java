package ru.nsu.lusnikov.main.java.timecomp.seacher;

import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;
import ru.nsu.lusnikov.main.java.timecomp.util.FileCompressManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Vector;

public class TableGenerator {

    public static BookInfo generateTable(int min, int max, int step, Path file, Vector<? extends CompressorBase> compressors) throws IOException {
        BookInfo bookInfo = new BookInfo();
        for (int i = min; ;) {
            HashMap<String, Long> map= new HashMap<>();
            for (CompressorBase compressorBase : compressors) {
                Path part = FileCompressManager.getFirstPart(i / 100.0, file);
                Path compressed = compressorBase.compress(part, false);
                map.put(compressorBase.toString(), compressed.toFile().length());
                part.toFile().delete();
                compressed.toFile().delete();
            }
            bookInfo.getInfo().put(i, map);
            if (i == max) {
                i = 100;
            } else if (i == 100) {
                break;
            }
            else i = i + step;
        }
        bookInfo.setName(file.toFile().getName());
        return bookInfo;
    }

}
