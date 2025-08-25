package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.permission.PermissionModelAssembler;
import com.baisebreno.learning_spring_api.api.model.PermissionModel;
import com.baisebreno.learning_spring_api.core.validation.Groups;
import com.baisebreno.learning_spring_api.domain.model.Permission;
import com.baisebreno.learning_spring_api.domain.model.UserGroup;
import com.baisebreno.learning_spring_api.domain.service.PermissionRegistryService;
import com.baisebreno.learning_spring_api.domain.service.UserGroupRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups/{groupId}/permissions")
public class GroupPermissionController {
    @Autowired
    PermissionRegistryService permissionService;

    @Autowired
    UserGroupRegistryService userGroupRegistryService;

    @Autowired
    PermissionModelAssembler assembler;


    @GetMapping
    public List<PermissionModel> getAllPermissions(@PathVariable Long groupId){
        UserGroup group = userGroupRegistryService.findOne(groupId);
        return assembler.toCollectionModel(group.getPermissions());
    }

    @PutMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPermission(@PathVariable Long groupId, @PathVariable Long permissionId){
        userGroupRegistryService.addPermissionToGroup(groupId,permissionId);
    }

    @DeleteMapping("/{permissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePermission(@PathVariable Long groupId, @PathVariable Long permissionId){
        userGroupRegistryService.removePermissionFromGroup(groupId,permissionId);
    }
}
