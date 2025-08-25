package com.baisebreno.learning_spring_api.api.assembler.groups;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.GroupModel;
import com.baisebreno.learning_spring_api.api.model.input.GroupInputModel;
import com.baisebreno.learning_spring_api.domain.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class GroupModelAssembler {

    @Autowired
    GenericAssembler assembler;

    public GroupModel toModel(Group group){
        return assembler.toSubject(group, GroupModel.class);
    }

    public List<GroupModel> toCollectionModel(Collection<Group> all) {
        return all.stream()
                .map(this::toModel)
                .collect(toList());
    }

    public Group toDomainObject(GroupInputModel inputModel){
        return assembler.toSubject(inputModel, Group.class);
    }

    public void copyToDomainObject(GroupInputModel inputModel, Group foundGroup) {
        assembler.copy(inputModel,foundGroup);
    }
}
