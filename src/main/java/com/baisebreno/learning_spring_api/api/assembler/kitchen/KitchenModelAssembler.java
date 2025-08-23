package com.baisebreno.learning_spring_api.api.assembler.kitchen;

import com.baisebreno.learning_spring_api.api.model.KitchenModel;
import com.baisebreno.learning_spring_api.api.model.input.KitchenInputModel;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class KitchenModelAssembler {

    @Autowired
    ModelMapper modelMapper;

    public KitchenModel toModel(Kitchen kitchen){
        return modelMapper.map(kitchen, KitchenModel.class);
    }

    public List<KitchenModel> toCollectionModel(List<Kitchen> all) {
        return all.stream()
                .map(this::toModel)
                .collect(toList());
    }
}
