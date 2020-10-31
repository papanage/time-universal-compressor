package ru.nsu.lusnikov.main.java.timecomp.comp;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;

public abstract class Compressor implements CompressorBase {
    boolean isLoad = false;
    File comp;

    public Compressor(String load) throws Exception{
       // System.out.println("try load " + getClass().getName()+ "...");
        comp =  Optional.ofNullable(getClass().getResource(load)).
                map(URL::getFile).
                map(File::new).
                orElseThrow(Exception::new);
       // System.out.println("success load " + getClass().getName());
        isLoad = true;
    }

    @Override
    public final Path compress(File toCompress) throws IllegalArgumentException {
        if (!isLoad) throw new IllegalArgumentException();
        return fcompress(toCompress);
    }

    protected abstract Path fcompress(File toCompress) throws IllegalArgumentException;

    protected void workWithExe(String execute,  String[] envp, File folder, boolean isNeedFlush) throws Exception{
        Process p;

        if (folder != null){
            if (envp != null){
                 p = Runtime.getRuntime().exec(execute, envp, folder);
            }
            else {
                 p = Runtime.getRuntime().exec(execute,null,  folder);
            }
        }
        else  {
            p = Runtime.getRuntime().exec(execute);
        }
        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(p.getErrorStream()));

        BufferedWriter stdOut = new BufferedWriter((new OutputStreamWriter((p.getOutputStream()))));



        StringBuilder response = new StringBuilder();
        StringBuilder errorStr = new StringBuilder();
        boolean alreadyWaited = false;

        while (p.isAlive()) {
            if(alreadyWaited) {
                if (isNeedFlush) {
                    stdOut.write("\n");
                    stdOut.flush();
                    stdOut.close();
                }
                String temp;

                while ((temp = stdInput.readLine()) != null) {
                    response.append(temp);
                }


                String errTemp;
                while ((errTemp = stdError.readLine()) != null) {
                    errorStr.append(errTemp);
                }
            }
            Thread.sleep(1000);
            alreadyWaited = true;
        }
        stdError.close();
        stdInput.close();
    }

}
