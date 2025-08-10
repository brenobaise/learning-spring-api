package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository {

    List<City> getAll();
    City getById(Long id);
    City save(City state);
    void remove(Long id );
}
