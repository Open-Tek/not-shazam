package com.knowwhere.notshazamserver.base;

import com.knowwhere.notshazamserver.base.core.WavFileHeader;
import com.knowwhere.notshazamserver.base.core.WavFileReader;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This thread must be a daemon thread, One that works in the background looking for a job in its blocking queue.
 * It holds a thread pool that it individually assigns jobs to
 */
public class WorkerThread extends Thread{
    private LinkedBlockingQueue<File> linkedBlockingQueue;
    private File target;

    public WorkerThread(File file){
        this.target = file;

    }

    @Override
    public void run(){
        try{
            while(true) {
                File newFileJob = this.linkedBlockingQueue.take();
                WavFileReader wavFileReader = new WavFileReader(newFileJob);
                int ret = wavFileReader.read();
                WavFileHeader header = wavFileReader.getWavFileHeader();
                int bufSize = header.getBufferLength();
                int i = 44;
                if( ret == 1 || ret == 2){
                    //mono channel ... read 2 bytes
                    short data ;
                    while( i < bufSize ){
                        data = header.getShortFromBuffer(i);
                        // todo bind data with music info
                        i+=2;
                    }
                }
                else if( ret == 2){
                    //stereo channel ... read 4 bytes
                    int data;
                    while( i< bufSize){
                        data = header.getIntFromBuffer(i);
                        //todo bind data with music info
                        i+=4;
                    }


                }else {
                    //error
                }
            }
        }catch ( InterruptedException ie){
            ie.printStackTrace();
        }
    }




}
