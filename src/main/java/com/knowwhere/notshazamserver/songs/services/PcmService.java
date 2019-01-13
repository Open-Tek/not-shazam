package com.knowwhere.notshazamserver.songs.services;

import com.knowwhere.notshazamserver.base.core.FFT;
import com.knowwhere.notshazamserver.base.core.FramingHelper;
import com.knowwhere.notshazamserver.base.core.RangeHelper;
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
    private class WorkerThread extends Thread {
        private byte fileContents[];
        private Song refSong;



        /*
        The idea behind this is assuming you have a file sampled at 44100 Hz, having 2 channels and 2 byte data,
        it means that we have 44100 * 2 * 2 = 176 KB per second of sound.
        NOW we are going to be FRAMING 4K of these chunks
         */


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
                System.out.println("channels "+header.getNumChannels() );
                System.out.println("max sampling chunk size for 1000ms "+(header.getSampleRate()* header.getBitsPerSample()/2* header.getNumChannels()));
                    //performing fft

                    int ret = header.getNumChannels();
                    int bufSize = header.getBufferLength();
                    int bitsPerSample = header.getBitsPerSample();


                    //size of the array is 8 bits/num bits per channel * size , since were holding n bits per sample, and dont need the extra space
                    int sampleArraySize = (header.getBufferLength() * 8/ bitsPerSample );
                    double samples[] = new double[sampleArraySize];
                    FramingHelper.prepareSamples(header, samples);
                    Complex fourierTransformedData[][] = FramingHelper.performFraming(header, samples);
                /**
                 * The contents of this array would be arranged as so
                 * the first index shows the frame index ie i or the sampled FT in time, the second index ie j shows the actual fourier transform for an instance of frequency
                 */
                    int startingTimeInMillis = (header.getSampleRate()* header.getBitsPerSample()/2* header.getNumChannels()) / FramingHelper.FRAME_CHUNK_SIZE * 1000;
                     int [][]points = RangeHelper.generateDataPoints(fourierTransformedData, startingTimeInMillis);



                System.out.println("SAMPLED ARRAY SIZE "+sampleArraySize);
            }catch ( Exception ie){
                ie.printStackTrace();
            }
            System.out.println("done with uploading and binding");

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
