package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PermissionRepository {
    List<Permission> getAll();
    Permission getById(Long id);
    Permission save(Permission permission);
    void remove(Permission permission);
}
