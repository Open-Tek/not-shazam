package com.knowwhere.notshazamserver.utils.tokens.interceptor;

import com.knowwhere.notshazamserver.user.models.BaseUser;
import com.knowwhere.notshazamserver.utils.tokens.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class TokenInterceptorHandler extends HandlerInterceptorAdapter {
    private final static String AUTH_HEADER_CONST = "Authorization";
    public final static String BASE_USER_KEY = "CurrentBaseUser";

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader(AUTH_HEADER_CONST);
        if(!request.getDispatcherType().toString().equals("REQUEST"))
            return true;
        System.out.println(request.getRequestURI());
        BaseUser user = this.tokenService.getUserByToken(authHeader);
        if( authHeader == null || user == null )
            try{
                System.out.println("unauthorized");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

                return false;
            }
            catch (IOException ioe){
                return false;
            }

        HttpSession httpSession = request.getSession();
            httpSession.setAttribute(BASE_USER_KEY, user);

        return true;


    }
}
