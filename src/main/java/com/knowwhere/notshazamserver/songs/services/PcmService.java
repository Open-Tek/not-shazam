package com.knowwhere.notshazamserver.songs.services;

import com.knowwhere.notshazamserver.base.core.WavFileHeader;
import com.knowwhere.notshazamserver.base.model.Complex;
import com.knowwhere.notshazamserver.songs.models.PcmValue;
import com.knowwhere.notshazamserver.songs.models.Song;
import com.knowwhere.notshazamserver.songs.repos.PcmValuesRepo;
import com.knowwhere.notshazamserver.songs.repos.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class PcmService {

    @Autowired
    private PcmValuesRepo pcmValuesRepo;

    @Autowired
    private SongRepo songRepo;

    public Song insertSong(Song song, MultipartFile multipartFile) throws IOException {
        Song s = this.songRepo.findByNameAndArtist(song.getName(), song.getArtist());
        if (s == null) {
            s = this.songRepo.save(song);
        }
        new WorkerThread(s, multipartFile.getBytes());
        return s;

    }

    public Set<Song> getPcmSongs(Long pcmValues[]){
        PcmValue pcm = this.findByPcmValue(pcmValues[0]);
        if (pcm == null)
            return null;
        Set <Song>probableSongs = pcm.getSongSet();

        for(int i =1; i<pcmValues.length; i++){

            pcm = this.findByPcmValue(pcmValues[i]);
            Set <Song>others = pcm.getSongSet();
            //intersection
            probableSongs.retainAll(others);


        }

        return probableSongs;


    }

    public PcmValue findByPcmValue(Long pcmValue){
        return this.pcmValuesRepo.findByPcmValue(pcmValue);
    }


    /**
     * This thread must be a daemon thread, One that works in the background looking for a job in its blocking queue.
     * It holds a thread pool that it individually assigns jobs to
     */
    private class WorkerThread extends Thread{
        private byte fileContents[];
        private Song refSong;

        public WorkerThread(Song refSong, byte contents[]){
            this.refSong = refSong;
            this.fileContents = contents;
            this.start();
        }

        @Override
        public void run(){
            try{
                boolean tr = true;
                //while(true) {
                    WavFileHeader header = new WavFileHeader(this.fileContents);

                    System.out.println("Bits per sample "+header.getBitsPerSample());
                    System.out.println("Sample rate "+header.getSampleRate());

                    System.out.println("Chunk size "+header.getDataChunk());
                    System.out.println("For frame of 20ms num samples are"+(header.getSampleRate() * 20) );
                    System.out.println("Size arr "+this.fileContents.length);
                    System.out.println("wav header "+header.getBufferLength());

                    //performing fft

                    int ret = header.getNumChannels();
                    int bufSize = header.getBufferLength();
                    int bitsPerSample = header.getBitsPerSample();


                    //size of the array is 8 bits/num bits per channel * size , since were holding n bits per sample, and dont need the extra space
                    int sampleArraySize = (header.getBufferLength() * 8/ bitsPerSample );
                    double samples[] = new double[sampleArraySize];
                    this.prepareSamples(header, samples);

            }catch ( Exception ie){
                ie.printStackTrace();
            }
            System.out.println("done with uploading and binding");

        }

        private void prepareSamples(WavFileHeader header, double samples[]){
            int arrayIndex = 44;//since the first 44 bytes store header info
            int bitsPerSample = header.getBitsPerSample();
            double maxValueForSample = (1 << (bitsPerSample -1)) +0.0;

            for ( int i = 0; i< samples.length; i++){
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


        private Complex[][] performFourier(){
            Co
        }

        private void bindData(int data){
            System.out.println("data "+data);





            PcmValue pcm = PcmService.this.pcmValuesRepo.findByPcmValue((long)data);
            if ( pcm == null){
                pcm = new PcmValue();
                pcm.setPcmValue((long)data);
                pcm.setSongSet(new HashSet<Song>());
            }
            //Hibernate.initialize(pcm.getSongSet());
            pcm.getSongSet().add(this.refSong);
            try {
                PcmService.this.pcmValuesRepo.save(pcm);
            }catch (Exception e){

            }
        }






    }


}
