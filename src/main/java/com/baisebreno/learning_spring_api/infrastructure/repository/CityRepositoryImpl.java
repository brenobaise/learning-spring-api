package com.baisebreno.learning_spring_api.infrastructure.repository;

import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.repository.CityRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class CityRepositoryImpl implements CityRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<City> getAll() {
        return entityManager.createQuery("from City ", City.class).getResultList();
    }

    @Override
    public City getById(Long id) {
        return entityManager.find(City.class, id);
    }

    @Transactional
    @Override
    public City save(City city) {
        return entityManager.merge(city);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        City city = getById(id);
        if(city == null){
            throw new EmptyResultDataAccessException(1);
        }
        entityManager.remove(city);
    }
}
