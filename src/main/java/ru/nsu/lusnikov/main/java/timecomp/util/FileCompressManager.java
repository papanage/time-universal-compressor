package ru.nsu.lusnikov.main.java.timecomp.util;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Math.abs;

@Slf4j
public class FileCompressManager {
    //static Random random = new Random(Instant.now().hashCode());
    static Random random = new Random(1);
    public static int fixRandom = random.nextInt();


    /**
     * Производит одну итерацию раундовой схемы
     * @param compressor массив архиваторов
     * @param p процент для сжатия
     * @param file файл для сжатия
     * @param isCopy определяет нужно ли копировать фалйы в процессе.
     *               опция, которая вскоре будет удалена
     * @return
     * @throws IOException
     */
    public static Vector<CompressFile> compressOneRound(List<CompressorBase> compressor, Double p, Path file, boolean isCopy) throws IOException {
        ExecutorService service = Executors.newFixedThreadPool(compressor.size());
        Path f = getFirstPart(p, file);
        Vector<CompressFile> compressFiles = new Vector<>();
        HashMap<CompressorBase, Future<Path>> futures = new HashMap<>();
        for (CompressorBase co : compressor){
            Callable<Path> task = () -> co.compress(f, isCopy);
            Future<Path> res = service.submit(task);
            futures.put(co, res);
        }
        for (Map.Entry<CompressorBase, Future<Path>> entry : futures.entrySet()) {
            try {
                compressFiles.add(new CompressFile(entry.getValue().get().toFile().length(), entry.getValue().get(), entry.getKey()));
                log.trace(entry.getValue().get().toFile().length() + " " +  entry.getValue().get()+ " " +entry.getKey());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        log.trace("");
       // System.out.println();
        service.shutdown();
        compressFiles.sort((x,y)-> compare(x.file, y.file));
        return compressFiles;
    }

    /**
     * Удаляет директорию по имени.
     * Рекурсивно со всеми файлами
     * @param dir навзанеи папки
     */
    public static void deleteTemp(String dir){
        File par = new File(dir);
        for (File fd : Objects.requireNonNull(par.listFiles())){
            fd.delete();
        }
    }

    public static Path getFirstPart(double percent, Path file) throws IOException {
        File part = new File("resources/temp/" + file.getFileName()+ "." + Math.round(100*percent));
        try {

            if (percent == 1.0) {
                part = new File("resources/temp/" +file.getFileName());
                Files.copy(Paths.get(file.toUri()), Paths.get(part.toURI()));
                return part.toPath();
            }

            if (!part.createNewFile()){
                throw new IllegalArgumentException();
            }
            FileOutputStream outputStream = new FileOutputStream(part);

            FileInputStream inputStream = new FileInputStream(file.toFile());

            int size = (int)(percent*file.toFile().length());
            byte[] bytes = new byte[size];
            long off =  file.toFile().length() / size - 2;
            long h = abs(fixRandom) % off + 1;
            for (int i = 0; i < h; i++) {
                inputStream.read(bytes);
            }

            outputStream.write(bytes);
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            throw new IOException(e);
        }
        return part.toPath();
    }

    private  static byte compare(Path f1, Path f2){
        if (f1.toFile().length() == f2.toFile().length()) return 0;
        if (f1.toFile().length() < f2.toFile().length()) return -1;
        return 1;
    }
}
