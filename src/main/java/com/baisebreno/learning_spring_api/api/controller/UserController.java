package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.user.UserModelAssembler;
import com.baisebreno.learning_spring_api.api.model.UserModel;
import com.baisebreno.learning_spring_api.api.model.input.PasswordInput;
import com.baisebreno.learning_spring_api.api.model.input.UserInputModel;
import com.baisebreno.learning_spring_api.api.model.input.UserWithPasswordInput;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.UserNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.repository.UserRepository;
import com.baisebreno.learning_spring_api.domain.service.UserRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserModelAssembler assembler;

    @Autowired
    UserRegistryService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<UserModel> getAll(){
        return assembler.toCollectionModel(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public UserModel find(@PathVariable Long id){
        User foundUser = userService.findOne(id);
        return assembler.toModel(foundUser);
    }

    @PostMapping()
    public UserModel create(@Valid @RequestBody UserWithPasswordInput inputModel){
        User newUser = assembler.disassemble(inputModel);

        return assembler.toModel(userService.save(newUser));
    }

    @PutMapping("/{id}")
    public UserModel update(@PathVariable Long id, @Valid @RequestBody UserInputModel inputModel){
        try{
            User foundUser = userService.findOne(id);
            assembler.copyToDomainObject(inputModel,foundUser);
            return assembler.toModel(userService.save(foundUser));
        }catch (UserNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordInput passwordInput){
        userService.updatePassword(id, passwordInput.getPassword(), passwordInput.getNewPassword());

    }

}
