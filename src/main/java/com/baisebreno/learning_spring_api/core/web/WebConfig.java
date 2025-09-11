package com.baisebreno.learning_spring_api.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*");
//                .allowedOrigins("/*")
//                .maxAge(20);

    }


    /**
     * generates an ETag value based on the content on the response.
     * This ETag is compared to the If-None-Match header of the request.
     * If these headers are equal, the response content is not sent, but rather a 304 "Not Modified" status instead.
     * @return
     */
    @Bean
    public Filter shallowEtagHeaderFilter(){
        return new ShallowEtagHeaderFilter();
    }
}
