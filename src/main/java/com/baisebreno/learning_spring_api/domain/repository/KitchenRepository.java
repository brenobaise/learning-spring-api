package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface KitchenRepository extends JpaRepository<Kitchen, Long> {
    /**
     * Queries the database for a {@link Kitchen} entity by its name.
     * @param name the name property of the entity.
     * @return A list of Kitchens matching the query param.
     */
    List<Kitchen> findAllByNameContaining(String name);


    Optional<Kitchen> findByName(String name);
}
