package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.UserGroupNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.PaymentMethod;
import com.baisebreno.learning_spring_api.domain.model.Permission;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.model.UserGroup;
import com.baisebreno.learning_spring_api.domain.repository.UserGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserGroupRegistryService {
    @Autowired
    UserGroupsRepository repository;

    @Autowired
    PermissionRegistryService permissionRegistryService;

    public UserGroup findOne(Long groupId) {
        return  repository.findById(groupId)
                .orElseThrow(() ->
                    new UserGroupNotFoundException(groupId));
    }
    @Transactional
    public void addPermissionToGroup(Long groupId, Long permissionId) {
        UserGroup group = findOne(groupId);

        Permission permission = permissionRegistryService.findOne(permissionId);

        group.addPermission(permission);
    }

    @Transactional
    public void removePermissionFromGroup(Long groupId, Long permissionId) {
        UserGroup group = findOne(groupId);

        Permission permission = permissionRegistryService.findOne(permissionId);

        group.removePermission(permission);
    }
}
