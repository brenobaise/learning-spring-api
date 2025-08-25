package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CustomJpaRepository<Order, Long> {

    @Query("from Order o join fetch  o.user join fetch o.restaurant r join fetch r.kitchen ")
    List<Order> findAll();

}
