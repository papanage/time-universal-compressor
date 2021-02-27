package ru.nsu.lusnikov.main.java.timecomp.adaptive.impl;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.lusnikov.main.java.timecomp.adaptive.api.TimeUniversalCompressorAPI;
import ru.nsu.lusnikov.main.java.timecomp.compressors.commons.CompressorBase;

import java.io.*;
import java.nio.file.Path;

@Getter
@Setter
public class BlocksTimeUniversal extends TimeUniversalCompressorAPI {
    TimeUniversalCompressorAPI universalCompressor;
    Integer blockSize;

    @Override
    public  Path compress(Path toCompress, boolean isCopy)  {
        try {
            universalCompressor.setCompressors(compressors);
            InputStream in = new FileInputStream(toCompress.toFile());
            byte[] block = new byte[blockSize];

            File fileFromBlock = new File(BlocksTimeUniversal.class.getResource("/huawei/mixed").toURI());
            long h = 0;
            long m = System.currentTimeMillis();
            for (int i = 0; i < 50; i++) {
                File blockf = new File("resources/runt1/block" + i);
                blockf.createNewFile();

                in.read(block, 0, blockSize);
                FileOutputStream fos = new FileOutputStream(blockf);
                fos.write(block);
               // System.out.println(blockf.length() + " " + blockf.toPath().toAbsolutePath());
                //BechMarkManager.staticRoundsGeneral(fileFromBlock, universalCompressor);
            }
           // System.out.println("time with blocks = " + (double) (System.currentTimeMillis() - m));

            //System.out.println("for block size  = " + h);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

//        Vector<Double> per = new Vector<>();
//        per.add(0.01);
//        per.add(0.05);
//
//        Vector<Integer> places = new Vector<>();
//        places.add(2);
//        places.add(1);

        File file = new File("resources/huawei/mixed");
//        Vector<CompressorBase> compressors = new Vector<>();
//        compressors.add(new Zip7());
//        compressors.add(new NanoZip());
//        compressors.add(new Lpaq8());
//        compressors.add(new Mcm());
//        compressors.add(new Tangelo());
//       // compressors.add(new Fp8());
//        // compressors.add(new NanoZip());
//        StaticTimeUniversal staticTimeUniversal = new StaticTimeUniversal();
//
//        staticTimeUniversal.setPer(per);
//        staticTimeUniversal.setPlaces(places);
//        staticTimeUniversal.setCompressors(compressors);

        BlocksTimeUniversal blocksTimeUniversal = new BlocksTimeUniversal();
        blocksTimeUniversal.setBlockSize(32*1024);
        blocksTimeUniversal.setUniversalCompressor(new DynamicTimeUniversal());
        blocksTimeUniversal.compress(file.toPath(), false);
        System.exit(0);

        System.out.println("name, size, best, choose,  best_size, choose_size, ratio, best_time, tuc_time, choose_time");

        //blocksTimeUniversal.compress(file, true);


    }

    @Override
    public CompressorBase getBestInList(Path file) {
        return universalCompressor.getBestInList(file);
    }


    @Override
    public Path decompress(Path file) {
        return null;
    }
}
