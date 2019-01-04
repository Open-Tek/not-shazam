package com.knowwhere.notshazamserver.user.controllers;

import com.knowwhere.notshazamserver.user.models.BaseUser;
import com.knowwhere.notshazamserver.user.services.BaseUserService;
import com.knowwhere.notshazamserver.utils.Utils;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

@RestController
@RequestMapping("api/v1/user/")
public class BaseUserController {

    @Autowired
    private BaseUserService baseUserService;



    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody BaseUser baseUser){
        return ResponseEntity.ok().body(this.baseUserService.createBaseUser(baseUser));
    }



}
