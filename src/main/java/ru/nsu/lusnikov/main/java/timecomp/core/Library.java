package ru.nsu.lusnikov.main.java.timecomp.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Lpaq8;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Mcm;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.NanoZip;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Tangelo;
import ru.nsu.lusnikov.main.java.timecomp.compressors.impl.Zip7;
import ru.nsu.lusnikov.main.java.timecomp.seacher.StaticSchema;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@Getter
@Slf4j
public class Library {
    public static Vector<CompressorBase> compressor = new Vector<>();
    public static StaticSchema schema;
    public static Vector<Double> per = new Vector<>();
    public static Vector<Integer> places = new Vector<>();
    static {
        try {
            compressor.add(new NanoZip());
            compressor.add(new Lpaq8());
            compressor.add(new Mcm());
            compressor.add(new Tangelo());
            compressor.add(new Zip7());

            per.add(0.01);
            per.add(0.09);
            per.add(0.1);

            places.add(5);
            places.add(2);
            places.add(1);

            schema = new StaticSchema();

            Map<Integer, StaticSchema.RoundInfo> roundInfos = new HashMap<>();
            for (int i = 0; i < per.size(); i++) {
                roundInfos.put(i, new StaticSchema.RoundInfo((int)(per.get(i) * 100), places.get(i)));
            }
            schema.setRounds(roundInfos);

        } catch (Exception e) {
            log.error("Error during init library", e);
        }
    }


}
