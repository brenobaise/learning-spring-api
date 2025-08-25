package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.PermissionNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Permission;
import com.baisebreno.learning_spring_api.domain.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionRegistryService {
    @Autowired
    PermissionRepository permissionRepository;

    public Permission findOne(Long id){
        return permissionRepository.findById(id)
                .orElseThrow( () ->
                         new PermissionNotFoundException(id));
    }

}
