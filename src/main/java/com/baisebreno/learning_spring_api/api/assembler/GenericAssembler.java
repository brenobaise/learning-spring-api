package com.baisebreno.learning_spring_api.api.assembler;

import com.baisebreno.learning_spring_api.api.model.input.CityInputModel;
import com.baisebreno.learning_spring_api.api.model.input.UserInputModel;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.baisebreno.learning_spring_api.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenericAssembler {

    @Autowired
    ModelMapper modelMapper;

    public <T> T toSubject(Object source, Class<T> targetClass ){
        return modelMapper.map(source,targetClass);
    }

    public <S,T> List<T> toCollectionModel(Collection<? extends S> source, Class<T> targetClass){
        return source.stream()
                .map(element -> toSubject(element, targetClass))
                .collect(Collectors.toList());
    }

    public <S, T> void copy(S source, T target){
        modelMapper.map(source,target);
    }

    public <S,T> T disassemble(S source, Class<T> target){
        return toSubject(source,target);
    }

}
