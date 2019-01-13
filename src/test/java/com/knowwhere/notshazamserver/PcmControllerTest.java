package com.knowwhere.notshazamserver;

import com.knowwhere.notshazamserver.songs.controllers.PcmController;
import com.knowwhere.notshazamserver.songs.models.Song;
import com.knowwhere.notshazamserver.songs.services.HashService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PcmController.class)
public class PcmControllerTest {
    @Autowired
    private MockMvc mockMvc;

    Song song =new Song("goat", "polyphia");


    @MockBean
    private HashService hashService;
    File testFile = new File("/home/rohan/Music/goat.wav");

    @Test
    public void testPcmSongInsert() throws Exception{
        MultipartFile file = new MockMultipartFile("Goat", Files.readAllBytes(testFile.toPath()));

        Mockito.when(
        hashService.insertSong(song, file)
        ).thenReturn(song);
    }




}
