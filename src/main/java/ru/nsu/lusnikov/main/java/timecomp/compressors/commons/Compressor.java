package ru.nsu.lusnikov.main.java.timecomp.compressors.commons;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;

@Slf4j
public abstract class Compressor implements CompressorBase {
    protected boolean isLoad = false;
    protected File comp;

    public Compressor(String load) throws Exception{
        log.trace("try load " + getClass().getName()+ "...");
//        comp =  Optional.ofNullable(getClass().getResource(load)).
//                map(URL::getFile).
//                map(File::new).
//                orElseThrow(Exception::new);

        comp = new File(load).getAbsoluteFile();
       log.trace("success load " + comp.toString());
        isLoad = true;
    }

    @Override
    public final Path compress(Path toCompress, boolean isCopy) throws IllegalArgumentException {
        if (!isLoad) throw new IllegalArgumentException();
        return fcompress(toCompress, isCopy);
    }

    @Override
    public Path decompress(Path toCompress) throws IllegalArgumentException {
        if (!isLoad) throw new IllegalArgumentException();
        return fdecompress(toCompress);
    }

    protected abstract Path fcompress(Path toCompress, boolean isCopy) throws IllegalArgumentException;
    protected abstract Path fdecompress(Path toCompress) throws IllegalArgumentException;

    protected  void workWithExe(String execute,  String[] envp, File folder, boolean isNeedFlush) throws Exception{
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
            Thread.sleep(10);
            alreadyWaited = true;
        }
        stdError.close();
        stdInput.close();
    }

}
