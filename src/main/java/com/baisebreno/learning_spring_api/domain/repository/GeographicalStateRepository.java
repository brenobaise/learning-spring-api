package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GeographicalStateRepository {
    List<GeographicalState> getAll();
    GeographicalState getById(Long id);
    GeographicalState save(GeographicalState state);
    void remove(Long id  );
}
