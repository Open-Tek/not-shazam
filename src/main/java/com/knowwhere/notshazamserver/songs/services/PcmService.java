package com.knowwhere.notshazamserver.songs.services;

import com.knowwhere.notshazamserver.base.core.WavFileHeader;
import com.knowwhere.notshazamserver.songs.models.PcmValue;
import com.knowwhere.notshazamserver.songs.models.Song;
import com.knowwhere.notshazamserver.songs.repos.PcmValuesRepo;
import com.knowwhere.notshazamserver.songs.repos.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

        new WorkerThread(s, multipartFile.getBytes()).start();
        return s;

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

        }

        @Override
        public void run(){
            try{
                while(true) {
                    WavFileHeader header = new WavFileHeader(this.fileContents);

                    int ret = header.getNumChannels();
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
            }catch ( Exception ie){
                ie.printStackTrace();
            }
        }

        private void bindData(int data, Song song){
            PcmValue pcm = PcmService.this.pcmValuesRepo.findByPcmValue((long)data);
            if ( pcm!= null){
                pcm = new PcmValue();
                pcm.setPcmValue((long)data);

            }
            pcm.getSongSet().add(song);
            PcmService.this.pcmValuesRepo.save(pcm);

        }






    }


}