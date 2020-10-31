package ru.nsu.lusnikov.main.java.timecomp.fcm;

import ru.nsu.lusnikov.main.java.timecomp.comp.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.comp.CompressorBase;

import java.io.*;
import java.util.*;

public class FileCompressManager {
    File file;


    public FileCompressManager(File file){
        this.file = file;
    }

    public CompressFile start(Vector<CompressorBase> compressor, Vector<Double> per, Vector<Integer> places) throws Exception {
        assert (per.size() == places.size());
        assert  (places.lastElement() == 1);
        assert (per.lastElement() == 1.0);
        return startStaticRounds(compressor, per, places, false);

    }

    private CompressFile startStaticRounds(Vector<CompressorBase> compressor, Vector<Double> per, Vector<Integer> places, boolean isCheck) throws Exception {
        List<CompressorBase> c = new ArrayList<>(compressor);

        int pl_index = 0;
        double delta = 0;
        Vector<CompressFile> compressFiles = new Vector<>();
        for (Double p : per){
            if (p.equals(1.0) && isCheck) {
                System.out.println("delta = " + delta);
            }
            if (p.equals(1.0) && isCheck) {
                //System.out.println("delta = " + delta);
                compressFiles.get(0);
            }

            delta += p * places.get(pl_index);

            compressFiles = compressOneRound(c, p);

            c.clear();
            for (CompressFile compressFile : compressFiles.subList(0, places.get(pl_index++))){
               // System.out.println("winner: " + compressFile.compressorBase);
                c.add(compressFile.compressorBase);
            }
            deleteTemp(compressFiles.get(0).file.getParent());
        }

        return compressFiles.get(0);
    }

    public CompressFile startDynamicalRounds(Vector<CompressorBase> compressor, Vector<Double> per) {
        List<CompressorBase> c = new ArrayList<>(compressor);
        double delta = 0;
        double p = 0.01;
        Vector<CompressFile> compressFiles = new Vector<>();
        do {
            compressFiles = compressOneRound(c, p);
            if (compressFiles.size() == 1) {
                delta += p * c.size();
                System.out.println("delta = " + delta);
                deleteTemp(compressFiles.get(0).file.getParent());
                return compressFiles.get(0);
            }
            c.clear();
            c.addAll(AnalysUtils.getBest(compressFiles));
            deleteTemp(compressFiles.get(0).file.getParent());
            delta += p * c.size();
            p += 0.05;
        } while (true);
    }

    private Vector<CompressFile> compressOneRound(List<CompressorBase> compressor, Double p){

        File f = getFirstPart(p);
        Vector<CompressFile> compressFiles = new Vector<>();
        for (CompressorBase co : compressor){
            File file = new File(String.valueOf(co.compress(f)));
            compressFiles.add(new CompressFile(file, co, file.length()));
        }

        compressFiles.sort((x,y)-> compare(x.file, y.file));

        for (CompressFile cf : compressFiles){
            //System.out.println(cf.file.getName() + " " + cf.compressorBase + " " + cf.file.length());
        }
        return compressFiles;
    }

    private void deleteTemp(String dir){
        File par = new File(dir);
        for (File fd : Objects.requireNonNull(par.listFiles())){
            fd.delete();
        }
    }
    public File getFirstPart(double percent){
        File part = new File("resources/temp/file" + percent + ".txt");
        try {
            if (!part.createNewFile()){
                throw new IllegalArgumentException();
            }

            FileWriter myWriter = new FileWriter(part);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            while (line != null) {
                myWriter.write(line +"\n");

                if (percent != 1.0)
                    if ((double)part.length() / file.length() > percent){
                        myWriter.close();
                        break;
                    }
                line = reader.readLine();
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return part;
    }

    private  static byte compare(File f1, File f2){
        if (f1.length() == f2.length()) return 0;
        if (f1.length() < f2.length()) return -1;
        return 1;
    }
}
