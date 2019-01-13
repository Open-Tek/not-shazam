package com.knowwhere.notshazamserver.songs.services;

import com.knowwhere.notshazamserver.base.core.FramingHelper;
import com.knowwhere.notshazamserver.base.core.RangeHelper;
import com.knowwhere.notshazamserver.base.core.WavFileHeader;
import com.knowwhere.notshazamserver.base.model.Complex;
import com.knowwhere.notshazamserver.songs.models.DataPoint;
import com.knowwhere.notshazamserver.songs.models.HashVals;
import com.knowwhere.notshazamserver.songs.models.Song;
import com.knowwhere.notshazamserver.songs.repos.HashValsRepo;
import com.knowwhere.notshazamserver.songs.repos.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
public class HashService {

    @Autowired
    private HashValsRepo hashValsRepo;

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
        HashVals pcm = this.findByPcmValue(pcmValues[0]);
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

    public HashVals findByPcmValue(Long pcmValue){
        return this.hashValsRepo.findByPcmValue(pcmValue);
    }


    /**
     * This thread must be a daemon thread, One that works in the background looking for a job in its blocking queue.
     * It holds a thread pool that it individually assigns jobs to
     */
    private class WorkerThread extends Thread implements RangeHelper.HashDataPointStoreCallback{
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

                    int ret = header.getNumChannels();
                    int bufSize = header.getBufferLength();
                    int bitsPerSample = header.getBitsPerSample();


                    //size of the array is 8 bits/num bits per channel * size , since were holding n bits per sample, and dont need the extra space
                    int sampleArraySize = (header.getBufferLength() * 8/ bitsPerSample );
                    double samples[] = new double[sampleArraySize];
                    FramingHelper.prepareSamples(header, samples);
                    Complex fourierTransformedData[][] = FramingHelper.performFraming(header, samples);

                    long startingTimeInMillis = (header.getSampleRate()* header.getBitsPerSample()/2* header.getNumChannels()) / FramingHelper.FRAME_CHUNK_SIZE * 1000;
                     int [][]points = RangeHelper.generateDataPoints(fourierTransformedData, startingTimeInMillis, this);


                System.out.println("SAMPLED ARRAY SIZE "+sampleArraySize);
            }catch ( Exception ie){
                ie.printStackTrace();
            }
            System.out.println("done with uploading and binding");

        }

        @Override
        public void bindHashToSong(long hash, long timeInMillis) {
            HashVals hashVals = HashService.this.hashValsRepo.findByHashValue(hash).orElseGet(() -> new HashVals(hash));
            hashVals.getDataPoints().add(new DataPoint(timeInMillis, this.refSong));

            HashService.this.hashValsRepo.save(hashVals);

        }
    }


}
