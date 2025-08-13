package com.baisebreno.learning_spring_api.infrastructure.repository.spec;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

public class RestaurantWithFreeDeliverySpec implements Specification<Restaurant> {


    @Override
    public Predicate toPredicate(Root<Restaurant> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {

        return criteriaBuilder.equal(root.get("deliveryRate"), BigDecimal.ZERO);
    }
}
