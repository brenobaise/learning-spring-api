package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.groups.UserGroupModelAssembler;
import com.baisebreno.learning_spring_api.api.assembler.payment.PaymentMethodModelAssembler;
import com.baisebreno.learning_spring_api.api.model.PaymentMethodModel;
import com.baisebreno.learning_spring_api.api.model.UserGroupModel;
import com.baisebreno.learning_spring_api.api.model.input.PaymentMethodInputModel;
import com.baisebreno.learning_spring_api.api.model.input.UserGroupInputModel;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.UserGroupNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.PaymentMethod;
import com.baisebreno.learning_spring_api.domain.model.UserGroup;
import com.baisebreno.learning_spring_api.domain.repository.PaymentTypeRepository;
import com.baisebreno.learning_spring_api.domain.repository.UserGroupsRepository;
import com.baisebreno.learning_spring_api.domain.service.PaymentMethodService;
import com.baisebreno.learning_spring_api.domain.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class UseGroupController {

    @Autowired
    UserGroupsRepository repository;
    @Autowired
    UserGroupService groupService;

    @Autowired
    UserGroupModelAssembler modelAssembler;

    @GetMapping
    public List<UserGroupModel> getAll(){
        return modelAssembler.toCollectionModel(repository.findAll());
    }

    @GetMapping("/{id}")
    public UserGroupModel find(@PathVariable Long id){
        UserGroup foundGroup = groupService.findOne(id);
        return modelAssembler.toModel(foundGroup);
    }

    @PostMapping()
    public UserGroupModel create(@RequestBody UserGroupInputModel inputModel){
        UserGroup newGroup = modelAssembler.toDomainObject(inputModel);

        return modelAssembler.toModel(groupService.save(newGroup));

    }

    @PutMapping("/{id}")
    public UserGroupModel update(@PathVariable Long id, @Valid @RequestBody UserGroupInputModel inputModel){
        try{
            UserGroup foundGroup = groupService.findOne(id);
            modelAssembler.copyToDomainObject(inputModel,foundGroup);
            return modelAssembler.toModel(groupService.save(foundGroup));
        }catch (UserGroupNotFoundException e){
            throw new BusinessException(e.getMessage());
        }



    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        groupService.remove(id);
    }
}

