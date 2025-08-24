package com.baisebreno.learning_spring_api.core.modelmapper;

import com.baisebreno.learning_spring_api.api.model.AddressModel;
import com.baisebreno.learning_spring_api.domain.model.Address;
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
        var modelMapper  =   new ModelMapper();

        var addressToAddressModelTypeMap = modelMapper.createTypeMap(
                Address.class, AddressModel.class);

        addressToAddressModelTypeMap.<String>addMapping(
                addressSource ->addressSource.getCity().getState().getName(),
                (addressTarget,value)-> addressTarget.getCity().setState(value));

        return modelMapper;
    }
}
