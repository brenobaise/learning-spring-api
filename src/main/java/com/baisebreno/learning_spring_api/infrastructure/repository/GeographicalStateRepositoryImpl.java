package com.baisebreno.learning_spring_api.infrastructure.repository;

import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.baisebreno.learning_spring_api.domain.repository.GeographicalStateRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class GeographicalStateRepositoryImpl implements GeographicalStateRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<GeographicalState> getAll() {
        return entityManager.createQuery("from GeographicalState ", GeographicalState.class).getResultList();
    }

    @Override
    public GeographicalState getById(Long id) {
        return entityManager.find(GeographicalState.class, id);
    }

    @Transactional
    @Override
    public GeographicalState save(GeographicalState state) {
        return entityManager.merge(state);
    }

    @Transactional
    @Override
    public void remove(Long id ) {
        GeographicalState state = getById(id);
        if(state == null){
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(state);
    }
}
