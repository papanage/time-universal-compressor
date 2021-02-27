package ru.nsu.lusnikov;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.impl.StaticTimeUniversal;

import java.io.File;

import static ru.nsu.lusnikov.main.java.timecomp.core.Library.compressor;
import static ru.nsu.lusnikov.main.java.timecomp.core.Library.per;
import static ru.nsu.lusnikov.main.java.timecomp.core.Library.places;

public class Core {

    public static void main(String[] args) {
        if (args.length == 2) {
            Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(Level.INFO);
        }

        StaticTimeUniversal staticTimeUniversal = new StaticTimeUniversal();

        staticTimeUniversal.setPercentsForArchive(per);
        staticTimeUniversal.setPlaces(places);
        staticTimeUniversal.setCompressors(compressor);

        if ("c".equals(args[0])) {
            File toCompress = new File(args[1]);
           staticTimeUniversal.compress(toCompress.toPath(), false);
        }
    }
}
