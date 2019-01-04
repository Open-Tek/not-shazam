package com.knowwhere.notshazamserver.utils.tokens.controllers;

import com.knowwhere.notshazamserver.utils.tokens.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/token/")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<?> getTokenWithLoginCreds(@RequestBody TokenService.EmailLoginModel emailLoginModel){
        System.out.println(emailLoginModel.getPassword()+" "+emailLoginModel.getEmail());
        return ResponseEntity.ok().body(this.tokenService.getUserToken(emailLoginModel));
    }

    @PostMapping("/refresh/")
    public ResponseEntity<?> refreshLoginToken(@RequestBody TokenService.RefreshTokenModel refreshTokenModel){
        return ResponseEntity.ok().body(this.tokenService.refreshTokens(refreshTokenModel.getRefreshToken()));
    }

}
