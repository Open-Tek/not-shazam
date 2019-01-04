package com.knowwhere.notshazamserver.base.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This file decodes a wav file.
 */
public class WavFileReader {

    private File targetFile;
    private WavFileHeader wavFileHeader;
    private int i = 44;

    public WavFileReader(File file){
        this.targetFile = file;
        if (!this.targetFile.exists() || !this.targetFile.canRead() )
            throw new RuntimeException("either this file doesnt exist or it cant be read");

    }

    /**
     * This method reads the wave file and returns the number of channels that this wav file has
     * @return int: if 1 is returned obtain the WavFileHeader and call getShortFromBuffer(), else call getIntegerFromB
     */
    public int read(){

        try{


            byte arr[] = Files.readAllBytes(this.targetFile.toPath());

            this.wavFileHeader = new WavFileHeader(arr);
            wavFileHeader.display();

            System.out.println("Done");
            return this.wavFileHeader.getNumChannels();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return -1;


    }

    public WavFileHeader getWavFileHeader() {
        return wavFileHeader;
    }




}
