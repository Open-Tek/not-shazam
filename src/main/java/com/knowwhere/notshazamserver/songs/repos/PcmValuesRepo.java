package com.knowwhere.notshazamserver.songs.repos;

import com.knowwhere.notshazamserver.songs.models.PcmValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PcmValuesRepo extends JpaRepository<PcmValue, Long> {
    PcmValue findByPcmValue(Long pcmValue);
}
