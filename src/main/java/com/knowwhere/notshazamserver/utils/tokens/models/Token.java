package com.knowwhere.notshazamserver.utils.tokens.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.knowwhere.notshazamserver.user.models.BaseUser;
import com.knowwhere.notshazamserver.utils.BaseEntity;
import com.knowwhere.notshazamserver.utils.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.Objects;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class Token extends BaseEntity {

    //10 minutes
    public final static int API_TOKEN_EXPIRY = 600 * 1000;

    //7 days
    public final static int REFRESH_TOKEN_EXPIRY = 24 * 3600 * 7;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "base_user_id")
    private BaseUser baseUser;

    private String apiToken;

    /**
     * Since JPA doesnt support having 2 natural ids this field isnt a NaturalId
     */
    private String refreshToken;

    @JsonIgnore
    private Date apiTokenExpiry;

    @JsonIgnore
    private Date refreshTokenExpiry;

    /**
     * with this key we can encrypt and decrypt user data.
     * Once the user wishes to quit the system only this value must be deleted. That way his/her data is inaccessible by us.
     */
    @JsonIgnore
    private String userDataEncryptionKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getApiTokenExpiry() {
        return apiTokenExpiry;
    }

    public void setApiTokenExpiry(Date apiTokenExpiry) {
        this.apiTokenExpiry = apiTokenExpiry;
    }

    public Date getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public void setRefreshTokenExpiry(Date refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    public String getUserDataEncryptionKey() {
        return userDataEncryptionKey;
    }

    public void setUserDataEncryptionKey(String userDataEncryptionKey) {
        this.userDataEncryptionKey = userDataEncryptionKey;
    }

    public void generateToken(){

        this.apiToken = StringUtils.toB64(Objects.requireNonNull(StringUtils.getSHA256(this.baseUser.getEmail() + this.baseUser.getPhone() + System.currentTimeMillis())));
        this.refreshToken = StringUtils.toB64(Objects.requireNonNull(StringUtils.getSHA256(this.baseUser.getEmail()+this.baseUser.getId()+System.currentTimeMillis())));

        this.apiTokenExpiry = new Date(System.currentTimeMillis()+API_TOKEN_EXPIRY);
        this.refreshTokenExpiry = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY);

    }

}
