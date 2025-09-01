package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CustomJpaRepository<Order, Long> {

    Optional<Order> findByOrderCode(String code);

    @Query("from Order o join fetch  o.user join fetch o.restaurant r join fetch r.kitchen ")
    List<Order> findAll();

}
