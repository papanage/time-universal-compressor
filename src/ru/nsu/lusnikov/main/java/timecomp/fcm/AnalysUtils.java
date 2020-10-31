package ru.nsu.lusnikov.main.java.timecomp.fcm;

import ru.nsu.lusnikov.main.java.timecomp.comp.CompressFile;
import ru.nsu.lusnikov.main.java.timecomp.comp.CompressorBase;

import java.util.ArrayList;
import java.util.Vector;

import static java.lang.Math.abs;

public class AnalysUtils {
    /**
     * get best
     * @param files
     * @return
     */
    public static ArrayList<CompressorBase> getBest(Vector<CompressFile> files) {
        //System.out.println("start k-means");
        ArrayList<CompressorBase> res = new ArrayList<>();
        for (CompressFile compressFile : k_means1d(files)) {
           // System.out.println(compressFile.compressorBase);
            res.add(compressFile.compressorBase);
        }
        return res;
    }

    private static Vector<CompressFile> k_means1d(Vector<CompressFile> files) {
        ArrayList<Long>  dist = new ArrayList<>();
        Vector<Vector<CompressFile>> classes = new Vector<>();
        Vector<Long> members = new Vector<>();
        members.add(files.get(0).size);
        members.add(files.lastElement().size);
        Vector<Long> new_members = new Vector<>(members);


        long diff;
        do {
            if (new_members.size() == 0) {
                break;
            }
            diff = 0;
            classes.clear();
            dist.clear();
            members = new Vector<>(new_members);
            new_members.clear();

            //compute distances between class represents
            for (int i = 0; i < members.size() - 1; i++) {
                dist.add((members.get(i + 1) + members.get(i))/2);
            }

            Vector<CompressFile> class_concrete = new Vector<>();
            Vector<CompressFile> class_concrete2 = new Vector<>();
            for (int i = 0; i < files.size(); i++) {
                if (files.get(i).size <= dist.get(0)) {
                    class_concrete.add(files.get(i));
                } else {
                    class_concrete2.add(files.get(i));
                }
            }
            if (class_concrete.size() != 0) {
                new_members.add(centralize(class_concrete));
                diff += abs(members.get(0) - new_members.get(0));
            }
            if (class_concrete2.size() != 0){
                new_members.add(centralize(class_concrete2));
                diff += abs(members.get(1) - new_members.get(1));
            }


            classes.add(class_concrete);
            classes.add(class_concrete2);
           // System.out.println("diff = " + diff);

        } while (diff > 10);
        return classes.get(0);
    }

    private static Long centralize(Vector<CompressFile> files) {
        long sum = 0;
        for (CompressFile compressFile : files) {
            sum += compressFile.size;
        }
        return  sum/files.size();
    }
}
