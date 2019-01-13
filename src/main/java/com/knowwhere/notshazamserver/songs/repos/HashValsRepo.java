package com.knowwhere.notshazamserver.songs.repos;

import com.knowwhere.notshazamserver.songs.models.HashVals;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashValsRepo extends JpaRepository<HashVals, Long> {
    Optional<HashVals> findByHashValue(Long hashValue);
}
