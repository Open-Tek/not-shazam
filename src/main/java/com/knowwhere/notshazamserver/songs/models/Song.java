package com.knowwhere.notshazamserver.songs.models;

import com.knowwhere.notshazamserver.utils.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Song extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

/*    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "songSet"
    )
    private Set<PcmValue> pcmValues;*/

    private String artist;

    public Song(){

    }


    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
/*
    public Set<PcmValue> getPcmValues() {
        return pcmValues;
    }

    public void setPcmValues(Set<PcmValue> pcmValues) {
        this.pcmValues = pcmValues;
    }*/
}
