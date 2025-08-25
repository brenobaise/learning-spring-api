package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.groups.GroupModelAssembler;
import com.baisebreno.learning_spring_api.api.model.GroupModel;
import com.baisebreno.learning_spring_api.api.model.input.GroupInputModel;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.GroupNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Group;
import com.baisebreno.learning_spring_api.domain.repository.GroupRepository;
import com.baisebreno.learning_spring_api.domain.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    GroupRepository repository;
    @Autowired
    GroupService groupService;

    @Autowired
    GroupModelAssembler modelAssembler;

    @GetMapping
    public List<GroupModel> getAll(){
        return modelAssembler.toCollectionModel(repository.findAll());
    }

    @GetMapping("/{id}")
    public GroupModel find(@PathVariable Long id){
        Group foundGroup = groupService.findOne(id);
        return modelAssembler.toModel(foundGroup);
    }

    @PostMapping()
    public GroupModel create(@RequestBody GroupInputModel inputModel){
        Group newGroup = modelAssembler.toDomainObject(inputModel);

        return modelAssembler.toModel(groupService.save(newGroup));

    }

    @PutMapping("/{id}")
    public GroupModel update(@PathVariable Long id, @Valid @RequestBody GroupInputModel inputModel){
        try{
            Group foundGroup = groupService.findOne(id);
            modelAssembler.copyToDomainObject(inputModel,foundGroup);
            return modelAssembler.toModel(groupService.save(foundGroup));
        }catch (GroupNotFoundException e){
            throw new BusinessException(e.getMessage());
        }



    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        groupService.remove(id);
    }
}

