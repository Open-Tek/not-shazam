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

import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class HashVals extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private Long hashValue;

    /*private int channelType;*/

    @OneToMany(fetch = FetchType.EAGER)
    private Set<DataPoint> dataPoints;

    public HashVals(){

    }

    public HashVals(Long hashValue, Set<DataPoint> dataPoints) {
        this.hashValue = hashValue;
        this.dataPoints = dataPoints;
    }

    public HashVals(Long hashValue){
        this(hashValue, new HashSet<>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHashValue() {
        return hashValue;
    }

    public void setHashValue(Long hashValue) {
        this.hashValue = hashValue;
    }

    public Set<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(Set<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
