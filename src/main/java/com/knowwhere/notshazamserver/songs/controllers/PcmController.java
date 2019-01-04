package com.knowwhere.notshazamserver.songs.controllers;

import com.knowwhere.notshazamserver.songs.models.Song;
import com.knowwhere.notshazamserver.songs.services.PcmService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
public class PcmController {

    @Autowired
    private PcmService pcmService;

    @PostMapping("store/{name}/{artist}/")
    @ApiOperation("This method stores and binds song data asyncronosly")
    @ApiResponse(message = "OK", code = 200, response = Song.class)
    public ResponseEntity<?> storeSong(@PathVariable("name") String songName, @PathVariable("artist") String artist, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        if ( multipartFile.isEmpty())
            return ResponseEntity.badRequest().body("empty file");
        Song a = new Song(songName, artist);
        return ResponseEntity.ok().body(this.pcmService.insertSong(a, multipartFile));
    }

    @GetMapping
    public ResponseEntity<?> getSongSet(@RequestParam("pcm[]") Long []pcmValues){
        return ResponseEntity.ok().body(this.pcmService.getPcmSongs(pcmValues));
    }


}
