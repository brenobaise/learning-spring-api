package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.groups.GroupModelAssembler;
import com.baisebreno.learning_spring_api.api.model.GroupModel;
import com.baisebreno.learning_spring_api.domain.model.Group;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.service.GroupRegistryService;
import com.baisebreno.learning_spring_api.domain.service.UserRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/groups")
public class UserGroupController {

    @Autowired
    UserRegistryService userRegistryService;

    @Autowired
    GroupModelAssembler groupModelAssembler;

    /**
     * Verifies is the incoming userId exists.
     * if it does, returns all groups this user belongs to.
     * @param userId target id
     * @return a list of groups this user belongs to.
     */
    @GetMapping
    public List<GroupModel> getAll(@PathVariable Long userId){
        User user = userRegistryService.findOne(userId);
        return groupModelAssembler.toCollectionModel(user.getGroups());
    }

    @PutMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserToGroup(@PathVariable Long userId,@PathVariable Long groupId){
        userRegistryService.addToGroup(userId,groupId);
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserFromGroup(@PathVariable Long userId,@PathVariable Long groupId){
        userRegistryService.removeFromGroup(userId,groupId);
    }

}
