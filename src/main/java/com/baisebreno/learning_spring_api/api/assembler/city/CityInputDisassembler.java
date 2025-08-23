package com.baisebreno.learning_spring_api.api.assembler.city;

import com.baisebreno.learning_spring_api.api.model.CityModel;
import com.baisebreno.learning_spring_api.api.model.input.CityInputModel;
import com.baisebreno.learning_spring_api.api.model.input.GeoStateInput;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityInputDisassembler {
    @Autowired
    ModelMapper modelMapper;

    public City toDomainObject(CityInputModel cityInputModel){
        return modelMapper.map(cityInputModel, City.class);
    }

    public void copyToDomainObject(CityInputModel cityInputModel, City city){
        // to prevent  org.hibernate.HibernateException: identifier of an instance of
        //		 domain.model.<class> was altered from 1 to 2
        city.setState(new GeographicalState());

        modelMapper.map(cityInputModel,city);
    }

}
