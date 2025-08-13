package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.Restaurant;

import java.math.BigDecimal;
import java.util.List;

/**
 * This is a custom repository, it acts the same as a {@link org.springframework.data.jpa.repository.JpaRepository}
 * This interface declares custom queries to be implemented by the RestaurantRepositoryImpl.
 * This interface is then extended by the actual RestaurantRepository, together with the Jpa standard repository.
 */
public interface RestaurantRepositoryQueries {
     List<Restaurant> find(String name, BigDecimal initialDeliveryRate, BigDecimal finalDeliveryRate );
}
