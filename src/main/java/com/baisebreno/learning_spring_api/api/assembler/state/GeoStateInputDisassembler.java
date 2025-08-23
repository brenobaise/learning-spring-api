package com.baisebreno.learning_spring_api.api.assembler.state;

import com.baisebreno.learning_spring_api.api.model.input.GeoStateInput;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeoStateInputDisassembler {
    @Autowired
    ModelMapper modelMapper;

    public GeographicalState toDomainObject(GeoStateInput geoStateInput){
        return modelMapper.map(geoStateInput, GeographicalState.class);
    }

    public void copyToDomainObject(GeoStateInput geoStateInput, GeographicalState state){
        modelMapper.map(geoStateInput,state);
    }
}
