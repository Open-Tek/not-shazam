package com.knowwhere.notshazamserver.utils.tokens.services;

import com.knowwhere.notshazamserver.user.models.BaseUser;
import com.knowwhere.notshazamserver.user.services.BaseUserService;
import com.knowwhere.notshazamserver.utils.StringUtils;
import com.knowwhere.notshazamserver.utils.UnauthorizedAccessException;
import com.knowwhere.notshazamserver.utils.tokens.models.Token;
import com.knowwhere.notshazamserver.utils.tokens.repos.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private final static BaseUser KNOWWHERE_ADMIN;
    private final static String KNOWWHERE_KEY = "FCXE==ASDW13=AXPaxqw45231XA";
    static{
        KNOWWHERE_ADMIN =  new BaseUser();
        KNOWWHERE_ADMIN.setEmail("admin@knowwhere.com");
        KNOWWHERE_ADMIN.setId(14000605L);//:)

    }

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private BaseUserService baseUserService;

    /**
     * This method creates a Token for every time the user logs in.
     * @param loginModel- -> The email password combo
     * @return Token
     */
    public Token getUserToken(EmailLoginModel loginModel){
        BaseUser baseUser = this.baseUserService.getBaseUserByEmailAndPassword(loginModel.email, loginModel.password);

        Token token = this.tokenRepo.findByBaseUser(baseUser);
        if( token == null ){
            //creating a token instance since no API tokens exist for this user
            token = new Token();
            token.setBaseUser(baseUser);
            token.setUserDataEncryptionKey(StringUtils.getSHA256(System.currentTimeMillis()+baseUser.getId()+baseUser.getPassword()));

        }
        token.generateToken();
        this.tokenRepo.save(token);

        return token;


    }

    /**
     * This method returns a BaseUser instance given an APIToken
     * @param token- ->An API token
     * @return BaseUser
     */
    public BaseUser getUserByToken(String token){
        if ( token == null )
            return null;
        if( token.equals(KNOWWHERE_KEY))
            return KNOWWHERE_ADMIN;
        return this.tokenRepo.findByApiTokenAndApiTokenExpiryBefore(token, new Date(System.currentTimeMillis())).getBaseUser();
    }

    /**
     * This method refreshes user tokens given the refreshToken.
     * @param refreshTOKEN- -> A RefreshToken
     * @return Token
     */
    public Token refreshTokens(String refreshTOKEN){
        Token current = this.tokenRepo.findByRefreshTokenAndRefreshTokenExpiryBefore(refreshTOKEN, new Date(System.currentTimeMillis()));
        if (current == null)
            throw new UnauthorizedAccessException("Token");

        current.generateToken();
        return current;


    }




    /**
     * This class is a model for JSON that is to accepted as a POST request.
     */
    public static class EmailLoginModel{
        String email;
        String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = StringUtils.getSHA256(password+BaseUser.PASSWORD_SALT);
        }
    }

    /**
     * This model holds a RefreshToken
     */
    public static class RefreshTokenModel{
        String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

}
