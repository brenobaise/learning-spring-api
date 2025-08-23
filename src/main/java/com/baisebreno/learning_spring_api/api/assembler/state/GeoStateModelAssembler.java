package com.baisebreno.learning_spring_api.api.assembler.state;

import com.baisebreno.learning_spring_api.api.model.GeoStateModel;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeoStateModelAssembler {
    @Autowired
    ModelMapper modelMapper;

    public GeoStateModel toModel(GeographicalState state){
        return modelMapper.map(state, GeoStateModel.class);
    }

    public List<GeoStateModel> toCollectionModel(List<GeographicalState> states){
        return states.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

    }
}
