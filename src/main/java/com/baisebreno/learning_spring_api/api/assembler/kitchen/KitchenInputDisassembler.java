package com.baisebreno.learning_spring_api.api.assembler.kitchen;

import com.baisebreno.learning_spring_api.api.model.input.KitchenInputModel;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KitchenInputDisassembler {
    @Autowired
    ModelMapper modelMapper;

    public Kitchen toDomainObject(KitchenInputModel kitchenInputModel){
        return modelMapper.map(kitchenInputModel,Kitchen.class);
    }
    public void copyToDomainObject(KitchenInputModel kitchenInputModel, Kitchen kitchen){
        modelMapper.map(kitchenInputModel, kitchen);
    }

}
