package com.knowwhere.notshazamserver.utils.tokens.repos;

import com.knowwhere.notshazamserver.user.models.BaseUser;
import com.knowwhere.notshazamserver.utils.tokens.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepo extends JpaRepository<Token, Long> {

    Token findByApiTokenAndApiTokenExpiryBefore(String apiToken, Date currentDateTime);
    Token findByRefreshTokenAndRefreshTokenExpiryBefore(String refreshToken, Date currentDateTime);

    Token findByBaseUser(BaseUser baseUser);

}
