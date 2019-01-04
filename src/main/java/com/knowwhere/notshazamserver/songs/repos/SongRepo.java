package com.knowwhere.notshazamserver.songs.repos;

import com.knowwhere.notshazamserver.songs.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepo extends JpaRepository<Song, Long> {
    Song findByNameAndArtist(String name, String artist);
}
