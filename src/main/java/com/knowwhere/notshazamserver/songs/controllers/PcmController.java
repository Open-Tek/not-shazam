package com.knowwhere.notshazamserver.songs.controllers;

import com.knowwhere.notshazamserver.songs.models.Song;
import com.knowwhere.notshazamserver.songs.services.PcmService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
public class PcmController {

    @Autowired
    private PcmService pcmService;

    @PostMapping("store/")
    @ApiOperation("This method stores and binds song data asyncronosly")
    @ApiResponse(message = "OK", code = 200, response = Song.class)
    public ResponseEntity<?> storeSong(@RequestBody Song song, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        if ( multipartFile.isEmpty())
            return ResponseEntity.badRequest().body("empty file");

        return ResponseEntity.ok().body(this.pcmService.insertSong(song, multipartFile));
    }

}
