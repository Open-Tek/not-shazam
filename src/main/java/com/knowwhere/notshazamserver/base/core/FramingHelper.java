package com.knowwhere.notshazamserver.base.core;

import com.knowwhere.notshazamserver.base.model.Complex;

public class FramingHelper {


    public final static int FRAME_CHUNK_SIZE = 4000;//K chunks


    public  static void prepareSamples(WavFileHeader header, double samples[]){
        int arrayIndex = 44;//since the first 44 bytes store header info
        int bitsPerSample = header.getBitsPerSample();
        double maxValueForSample = (1 << (bitsPerSample -1)) +0.0;

        for ( int i = 0; i< samples.length && arrayIndex<header.getBufferLength(); i++){
            long data = 0;
            switch( bitsPerSample ){
                case 8:
                    data = header.getByteFromBuffer(arrayIndex);
                    arrayIndex++;
                    break;
                case 16:
                    data = header.getShortFromBuffer(arrayIndex);
                    arrayIndex+=2;
                    break;

                case 32:
                    data = header.getIntFromBuffer(arrayIndex);
                    arrayIndex+=4;
                    break;

                case 64:
                    data = header.getLongFromBuffer(arrayIndex);
                    arrayIndex+=8;

            }
            samples[i] = data/ maxValueForSample;

        }
    }


    public static Complex[][] performFraming(WavFileHeader header, double[] samples){

        int sampledChunkSize = samples.length/ FRAME_CHUNK_SIZE;
        Complex arr[] = new Complex[FRAME_CHUNK_SIZE];//to be reused

        Complex result[][] = new Complex[sampledChunkSize][];


        for ( int times = 0; times < sampledChunkSize; times ++){
            /**
             * Think of a page table offset mechanism
             * times is the first page dir entry, mul by 4K, + offset i to obtain page.
             * Considering 4k frames at the moment
             */
            for( int i=0; i<FRAME_CHUNK_SIZE; i++)
                arr[i] = new Complex(samples[ (times * FRAME_CHUNK_SIZE) + i], 0.0);

            result[times] = FFT.fft(arr);
            System.out.println("FINISHED ITERATION "+times+ " of "+sampledChunkSize);
        }
        return result;



    }


}
