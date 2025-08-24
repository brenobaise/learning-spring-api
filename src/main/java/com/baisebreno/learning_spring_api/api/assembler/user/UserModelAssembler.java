package com.baisebreno.learning_spring_api.api.assembler.user;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.UserModel;
import com.baisebreno.learning_spring_api.api.model.input.UserInputModel;
import com.baisebreno.learning_spring_api.domain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserModelAssembler {
    @Autowired
    GenericAssembler assembler;


    public UserModel toModel(User user){
        return assembler.toSubject(user, UserModel.class);
    }

    public List<UserModel> toCollectionModel(List<User> users){
        return assembler.toCollectionModel(users,UserModel.class);
    }

    public User disassemble(UserInputModel userInputModel){
        return assembler.toSubject(userInputModel, User.class);
    }

    public void copyToDomainObject(UserInputModel inputModel, User user){
        assembler.copy(inputModel,user);
    }
}
