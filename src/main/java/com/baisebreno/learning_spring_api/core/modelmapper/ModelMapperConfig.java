package com.baisebreno.learning_spring_api.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Register the {@link ModelMapper} class to Spring's context.
 * This library used to convert between different Java objects,
 * typically between DTOs (Data Transfer Objects) and entities.
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
