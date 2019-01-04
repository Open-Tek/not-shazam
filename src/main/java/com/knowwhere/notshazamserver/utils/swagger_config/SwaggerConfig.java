package com.knowwhere.notshazamserver.utils.swagger_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@PropertySource("classpath:swagger.properties")
public class SwaggerConfig {

    private final static String API_VERSION = "1";
    private final static String LICENSE_TEXT = "License:)";

    private final static String TITLE = "ClassesRestfulAPI";
    private final static String DESCRIPTION = "An API FOR Classroom";

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title(SwaggerConfig.TITLE)
                .description(SwaggerConfig.DESCRIPTION)
                .version(SwaggerConfig.API_VERSION)
                .license(SwaggerConfig.LICENSE_TEXT).build();
    }


    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(this.apiInfo())
                .pathMapping("/").select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
