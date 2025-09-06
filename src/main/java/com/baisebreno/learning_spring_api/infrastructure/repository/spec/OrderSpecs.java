package com.baisebreno.learning_spring_api.infrastructure.repository.spec;

import com.baisebreno.learning_spring_api.domain.model.Order;
import com.baisebreno.learning_spring_api.domain.filter.OrderFilter;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import javax.persistence.criteria.Predicate;

public class OrderSpecs {
    public static Specification<Order> withFilter(OrderFilter orderFilter) {
        return (root, query, criteriaBuilder) -> {
            if(Order.class.equals(query.getResultType())){
                root.fetch("restaurant").fetch("kitchen");
                root.fetch("user");
            }

            var predicates = new ArrayList<Predicate>();

            if(orderFilter.getUserId() != null){
                predicates.add(criteriaBuilder.equal(root.get("user"), orderFilter.getUserId()));
            }

            if(orderFilter.getRestaurantId() != null){
                predicates.add(criteriaBuilder.equal(root.get("restaurant"), orderFilter.getRestaurantId()));
            }

            if(orderFilter.getInitialCreatedDate() != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"),
                        orderFilter.getInitialCreatedDate()));
            }

            if(orderFilter.getEndingCreatedDate() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"),
                        orderFilter.getEndingCreatedDate()));
            }

            if(orderFilter.getTotal() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("total"),
                        orderFilter.getTotal()));
            }

            if(orderFilter.getDeliveryRate() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deliveryRate"),
                        orderFilter.getDeliveryRate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
