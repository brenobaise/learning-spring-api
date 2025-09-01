package com.baisebreno.learning_spring_api.core.modelmapper;

import com.baisebreno.learning_spring_api.api.model.AddressModel;
import com.baisebreno.learning_spring_api.api.model.input.OrderInput;
import com.baisebreno.learning_spring_api.domain.model.Address;
import com.baisebreno.learning_spring_api.domain.model.OrderItem;
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

        modelMapper.createTypeMap(OrderInput.class, OrderItem.class)
                .addMappings(mapper -> mapper.skip(OrderItem::setId));
        var addressToAddressModelTypeMap = modelMapper.createTypeMap(
                Address.class, AddressModel.class);

        addressToAddressModelTypeMap.<String>addMapping(
                addressSource ->addressSource.getCity().getState().getName(),
                (addressTarget,value)-> addressTarget.getCity().setState(value));

        return modelMapper;
    }
}
