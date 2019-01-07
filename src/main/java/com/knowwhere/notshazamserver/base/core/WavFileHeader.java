package com.knowwhere.notshazamserver.base.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class WavFileHeader {
/*    private int riff;//1-4
    private int size;//5-8
    private int fileType;//9-12 always holds ascii of W-A-V-E
    private int formatChunkMarker;//format chunk marker, includes trailing null
    private int formatLength;
    short formatType ;//1 is PCM, 2 byte integer
    short numChannels ;//2 byte integer
    int sampleRate;//32 bit val, common vals are 44100, 48000 in Hz
    int byteRate;// sampleRate * bitsPerSample* channels / 8
    short blockAlign;//BitsPerSample*Channels/8
    short bitsPerSample;
    int dataChunk;//marks beginning of data
    int dataSize;//size of the data section*/
    private ByteBuffer byteBuffer;

    /**
     * The first 44 bytes of a WAV file , ie byte array[0-43] contains header information.
     * Starting from byte array[44], it holds pcm encoded values.
     * Depending on the value of numChannels, the number of bytes read from the byte array differs.
     * If numChannles is 1, then only 16 bit values need to be read.
     * If its val is 2 , 32 bit values need to be read.
     * The first 16 bits of this 32 bit is for channel 1, the other are for channel 2.
     *
     * TLDR AND PS- This constructor creates a byteBuffer of the given array, throws a RuntimeException if its null or its size < 44
     * and CONVERTS it to LITTLE ENDIAN.
     * @param arr: The byte array containing wav data.
     */
    public WavFileHeader(byte arr[]){
        if (arr == null || arr.length < 44)
            throw new RuntimeException("Illegal wav file");
        // this is being done since a java byte array is always BigEndian, but wav info is in LITTLE Endian.
        this.byteBuffer = ByteBuffer.wrap(arr);
        this.byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public void display(){
        System.out.println("riff "+this.getRiff());
        System.out.println("File Type "+this.getFileType());
        System.out.println("Format chunk marker "+this.getFormatChunkMarker());
        System.out.println("Format length "+this.getFormatLength());
        System.out.println("Format Type "+this.getFormatType());
        System.out.println("num channels "+this.getNumChannels());
        System.out.println("sample rate "+this.getSampleRate());
        System.out.println("Byte rate "+this.getByteRate());
        System.out.println("Block align "+this.getBlockAlign());
        System.out.println("Bits per sample "+this.getBitsPerSample());
        System.out.println("Data Chunk "+this.getDataChunk());
        System.out.println("Data size "+this.getDataSize());

    }

    public int getRiff() {
        return this.byteBuffer.getInt(0);
    }

    public int getSize() {
        return this.byteBuffer.getInt(4);
    }

    public int getFileType() {
        return this.byteBuffer.getInt(8);
    }

    public int getFormatChunkMarker() {
        return this.byteBuffer.getInt(12);
    }

    public int getFormatLength() {
        return this.byteBuffer.getInt(16);
    }

    public short getFormatType() {
        return this.byteBuffer.getShort(20);
    }

    public short getNumChannels() {
        return this.byteBuffer.getShort(22);
    }

    public int getSampleRate() {
        return this.byteBuffer.getInt(24);
    }

    public int getByteRate() {
        return this.byteBuffer.getInt(28);
    }

    public short getBlockAlign() {
        return this.byteBuffer.getShort(32);
    }

    public short getBitsPerSample() {
        return this.byteBuffer.getShort(34);
    }

    public int getDataChunk() {
        return this.byteBuffer.getInt(36);
    }

    public int getDataSize() {
        return this.byteBuffer.getInt(40);
    }

    /**
     * This method returns a byte of data from the specified index
     * @param index: The index of the byte arr
     * @return byte
     */
    public byte getByteFromBuffer(int index){
        return this.byteBuffer.get(index);
    }


    /**
     * This helper method is to be used to fetch short(2 byte vals) When numchannels = 1
     * @param index: The index of the byte arr
     * @return int
     */
    public short getShortFromBuffer(int index){
        return this.byteBuffer.getShort(index);
    }

    /**
     * This method is to be used to fetch int(4 byte vals) only when num channels = 2
     * @param index: The index of the byte arr
     * @return int
     */
    public int getIntFromBuffer(int index){

        return this.byteBuffer.getInt(index);
    }

    /**
     * This method retrieves 8byte values(long) when num channels are 4
     * @param index: The index of the byte arr
     * @return long
     */
    public long getLongFromBuffer(int index){
        return this.byteBuffer.getLong(index);
    }


    /**
     * This method returns the size of the internal byteBuffer being used.
     * @return int
     */
    public int getBufferLength(){
        return this.byteBuffer.capacity();
    }



}