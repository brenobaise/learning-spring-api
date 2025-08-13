package com.baisebreno.learning_spring_api.infrastructure.repository.spec;

import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Utility class that provides {@link Specification} factory methods
 * for building dynamic {@link Restaurant} query filters using the Criteria API.
 */
public class RestaurantSpecs  {

    /**
     * Creates a specification that filters restaurants with a delivery rate equal to zero.
     *
     * @return a {@link Specification} for filtering restaurants that offer free delivery
     */
    public static Specification<Restaurant> withFreeDeliveryRate() {
        return (root, query, builder) ->
                builder.equal(root.get("deliveryRate"), BigDecimal.ZERO);
    }

    /**
     * Creates a specification that filters restaurants whose name contains the given text.
     *
     * @param name the substring to search for in the restaurant name (case-sensitive)
     * @return a {@link Specification} for filtering restaurants by similar name
     */
    public static Specification<Restaurant> withSimilarName(String name) {
        return (root, query, builder) ->
                builder.like(root.get("name"), "%" + name + "%");
    }
}
