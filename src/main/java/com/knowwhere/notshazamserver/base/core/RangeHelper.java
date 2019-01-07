package com.knowwhere.notshazamserver.base.core;

import com.knowwhere.notshazamserver.base.model.Complex;

public class RangeHelper {
    /**
     * Since every song has frequencies varying between low c(32.7Hz) and high C(4186Hz) we cant analyze all frequencies.
     * Instead we do so in smaller steps, chosen based on the common frequency of import muiscal components and analyzing each seperately.
     * 30-40Hz , 40-80, 80-120 cover low tones say for the bass guitar,
     * 120-180Hz, 180-300Hz are for the middle tones,
     * Above 300 is for higher tones (for vocals and other instruments).
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


    public static int[][] generateDataPoints(Complex [][]fourierTransformedComplexMatrix){

        double highscore[][] = new double[fourierTransformedComplexMatrix.length][RANGE.length];
        int points[][] = new int[fourierTransformedComplexMatrix.length][RANGE.length];

        for ( int time = 0; time < fourierTransformedComplexMatrix.length; time++){

            for( int frequency = 40; frequency < 300; frequency++){

                double magnitude = Math.log(fourierTransformedComplexMatrix[time][frequency].abs() + 1);
                int index = getIndex(frequency);

                if (magnitude > highscore[time][index]){

                    highscore[time][index] = magnitude;


                    points[time][index] = frequency;
                }


            }


            long hash = RangeHelper.generateHash(points[time][0], points[time][1], points[time][2], points[time][3]);
            //todo create an array of DataPoint here
            //todo also calculate the time for a sample of 4000 bytes



        }

        return points;
    }


    private final static long generateHash(long p1, long p2, long p3, long p4){
        return ((p4 - (p4 % FUZ_FACTOR)  )  * 100000000 + (p3 - (p3 % FUZ_FACTOR)) * 100000 + ( p2 - (p2 % FUZ_FACTOR )) * 100+ (p1-(p1 % FUZ_FACTOR)) );
    }

}
