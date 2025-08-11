package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GeographicalStateRepository extends JpaRepository<GeographicalState, Long> {

}
