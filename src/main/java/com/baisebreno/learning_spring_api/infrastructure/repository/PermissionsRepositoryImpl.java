package com.baisebreno.learning_spring_api.infrastructure.repository;

import com.baisebreno.learning_spring_api.domain.model.Permission;
import com.baisebreno.learning_spring_api.domain.repository.PermissionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class PermissionsRepositoryImpl implements PermissionRepository {

    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<Permission> getAll() {
        return entityManager.createQuery("from Permission ", Permission.class)
                .getResultList();
    }

    @Override
    public Permission getById(Long id) {
        return entityManager.find(Permission.class, id);
    }

    @Transactional
    @Override
    public Permission save(Permission permission) {
        return entityManager.merge(permission);
    }

    @Transactional
    @Override
    public void remove(Permission permission) {
        permission = getById(permission.getId());

        entityManager.remove(permission);
    }
}
