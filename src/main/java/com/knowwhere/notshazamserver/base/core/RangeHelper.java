package com.knowwhere.notshazamserver.base.core;

import com.knowwhere.notshazamserver.base.model.Complex;

public class RangeHelper {
    /**
     * Since every song has frequencies varying between low c(32.7Hz) and high C(4186Hz) we cant analyze all frequencies.
     * Instead we do so in smaller steps, chosen based on the common frequency of import muiscal components and analyzing each seperately.
     * 30-40Hz , 40-80, 80-120 cover low tones say for the bass guitar,
     * 120-180Hz, 180-300Hz are for the middle tones,
     * Above 300 is for higher tones (for vocals and other instruments).
     * NOTE ----- For the moment ASSUMING 4 ranges only.
     */
    public final static int RANGE[] = {
            40,80,120,180,300
    };

    private final static int FUZ_FACTOR = 2;




    /**
     * Returns an index given the frequency. This helps in finding the range of frequencies we are iterested in.
     * @param frequency: -The frequency to be considered
     * @return int - An index to the RANGE array.
     */
    public static int getIndex(int frequency){
        int i=0;
        while( RangeHelper.RANGE[i] < frequency)
            i++;
        return i;
    }


    public static int[][] generateDataPoints(Complex [][]fourierTransformedComplexMatrix, long startinTimeInMillis, HashDataPointStoreCallback callback){
        long elapsedTime = startinTimeInMillis;
        double highscore[][] = new double[fourierTransformedComplexMatrix.length][RANGE.length];
        int points[][] = new int[fourierTransformedComplexMatrix.length][RANGE.length];



        for ( int time = 0; time < fourierTransformedComplexMatrix.length; time++, elapsedTime += startinTimeInMillis){

            for( int frequency = 40; frequency < 300; frequency++){

                double magnitude = Math.log(fourierTransformedComplexMatrix[time][frequency].abs() + 1);
                int index = getIndex(frequency);

                if (magnitude > highscore[time][index]){

                    highscore[time][index] = magnitude;


                    points[time][index] = frequency;
                }


            }


            long hash = RangeHelper.generateHash(points[time][0], points[time][1], points[time][2], points[time][3]);
            callback.bindHashToSong(hash, elapsedTime);


        }

        /**
         * The contents of this array would be arranged as so
         * the first index shows the frame index ie i or the sampled FT in time, the second index ie j shows the actual fourier transform for an instance of frequency
         */

        return points;
    }


    private final static long generateHash(long p1, long p2, long p3, long p4){
        return ((p4 - (p4 % FUZ_FACTOR)  )  * 100000000 + (p3 - (p3 % FUZ_FACTOR)) * 100000 + ( p2 - (p2 % FUZ_FACTOR )) * 100+ (p1-(p1 % FUZ_FACTOR)) );
    }

    /**
     * This callback is triggered everytime a hash for a song data point is computed.
     * The method must bind the song with the provided time and hash.
     */
    public static interface HashDataPointStoreCallback{
        public void bindHashToSong(long hash, long timeInMillis);
    }

}
