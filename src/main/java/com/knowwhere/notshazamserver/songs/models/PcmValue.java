package com.knowwhere.notshazamserver.songs.models;

import com.knowwhere.notshazamserver.utils.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class PcmValue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private Long pcmValue;

    /*private int channelType;*/

    @ManyToMany(
        fetch = FetchType.EAGER,
            cascade = CascadeType.ALL

    )
    @JoinTable(
            name="pcm_songs",
            joinColumns = { @JoinColumn(name="pcm_id")},
            inverseJoinColumns = { @JoinColumn(name = "song_id")}
    )
    private Set<Song> songSet = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPcmValue() {
        return pcmValue;
    }

    public void setPcmValue(Long pcmValue) {
        this.pcmValue = pcmValue;
    }

    public Set<Song> getSongSet() {
        return songSet;
    }

    public void setSongSet(Set<Song> songSet) {
        this.songSet = songSet;
    }
}
