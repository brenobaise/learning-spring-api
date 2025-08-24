package com.baisebreno.learning_spring_api.api.assembler.groups;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.UserGroupModel;
import com.baisebreno.learning_spring_api.api.model.input.UserGroupInputModel;
import com.baisebreno.learning_spring_api.domain.model.UserGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class UserGroupModelAssembler {

    @Autowired
    GenericAssembler assembler;

    public UserGroupModel toModel(UserGroup group){
        return assembler.toSubject(group, UserGroupModel.class);
    }

    public List<UserGroupModel> toCollectionModel(List<UserGroup> all) {
        return all.stream()
                .map(this::toModel)
                .collect(toList());
    }

    public UserGroup toDomainObject(UserGroupInputModel inputModel){
        return assembler.toSubject(inputModel, UserGroup.class);
    }

    public void copyToDomainObject(UserGroupInputModel inputModel, UserGroup foundGroup) {
        assembler.copy(inputModel,foundGroup);
    }
}
