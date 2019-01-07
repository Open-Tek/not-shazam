package com.knowwhere.notshazamserver.base.core;

import com.knowwhere.notshazamserver.base.model.Complex;

/**
 * This class provided methods to perform fast fourier transform
 */
public class FFT {
    public static Complex[] fft(Complex[] complexArray){
        int size = complexArray.length;

        if ( size == 1)
            return new Complex[]{
                    complexArray[0]
            };

        if ( (size & 1) == 1) {
            // ideally an exception should be thrown for odd numbered lengths... making it even for the time being

            size += 1;
        }


        Complex[] evenIndexedHalf = new Complex[(size >> 1)];
        for( int i = 0; i< (size>>1); i++){
            evenIndexedHalf[i] = complexArray[2*i];
        }



        Complex[] q = fft(evenIndexedHalf);

        Complex[] oddIndexedHalf = evenIndexedHalf;//reusing
        //System.out.println("Arr size "+size+ " odd indexed "+oddIndexedHalf.length);
        for( int i = 0; i< (size>>1); i++){
            System.out.println(i+ " i "+(2*i +1)+" 2i+1 "+oddIndexedHalf.length+" odd len "+complexArray.length+" main len");
            oddIndexedHalf[i] = complexArray[2*i + 1];
        }

        Complex []r = fft(oddIndexedHalf);
        //combine p and q
        Complex result[] = new Complex[size];
        for ( int i =0 ; i< (size >> 1); i++){
            double ith = -2 * i * Math.PI / size;

            Complex wi = new Complex(Math.cos(ith), Math.sin(ith));
            result[i] = q[i].add(wi.mul(r[i]));
            result[i + (size>>1)] = q[i].sub(wi.mul(r[i]));

        }

        return result;

    }


}
