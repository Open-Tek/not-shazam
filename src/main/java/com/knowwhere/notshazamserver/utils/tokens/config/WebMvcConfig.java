package com.knowwhere.notshazamserver.utils.tokens.config;

import com.knowwhere.notshazamserver.utils.tokens.interceptor.TokenInterceptorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@ComponentScan
@Configuration
public class WebMvcConfig  implements WebMvcConfigurer {

    private final ArrayList<String> PATHS_TO_BE_EXCLUDED = new ArrayList<>();
    {
        PATHS_TO_BE_EXCLUDED.add("/api/v1/token/**");
        PATHS_TO_BE_EXCLUDED.add("/configuration/ui");
        PATHS_TO_BE_EXCLUDED.add("/swagger-resources/**");
        PATHS_TO_BE_EXCLUDED.add("/swagger-ui.html");
        PATHS_TO_BE_EXCLUDED.add("/webjars/**");
    }

    @Bean
    public TokenInterceptorHandler createTokenInterceptorHandler(){
        return new TokenInterceptorHandler();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){
        System.out.println("REGI");
        registry.addInterceptor(this.createTokenInterceptorHandler()).excludePathPatterns(PATHS_TO_BE_EXCLUDED);
    }
}
