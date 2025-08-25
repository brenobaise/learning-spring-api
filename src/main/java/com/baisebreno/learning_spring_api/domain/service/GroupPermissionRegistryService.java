package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.GroupNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Permission;
import com.baisebreno.learning_spring_api.domain.model.Group;
import com.baisebreno.learning_spring_api.domain.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupPermissionRegistryService {
    @Autowired
    GroupRepository repository;

    @Autowired
    PermissionRegistryService permissionRegistryService;

    public Group findOne(Long groupId) {
        return  repository.findById(groupId)
                .orElseThrow(() ->
                    new GroupNotFoundException(groupId));
    }
    @Transactional
    public void addPermissionToGroup(Long groupId, Long permissionId) {
        Group group = findOne(groupId);

        Permission permission = permissionRegistryService.findOne(permissionId);

        group.addPermission(permission);
    }

    @Transactional
    public void removePermissionFromGroup(Long groupId, Long permissionId) {
        Group group = findOne(groupId);

        Permission permission = permissionRegistryService.findOne(permissionId);

        group.removePermission(permission);
    }
}
