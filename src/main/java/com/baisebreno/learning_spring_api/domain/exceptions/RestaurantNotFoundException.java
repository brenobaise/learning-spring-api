package com.baisebreno.learning_spring_api.domain.exceptions;

public class RestaurantNotFoundException extends EntityNotFoundException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(Long restaurantId) {
        this(String.format("Restaurant of id %d not found.",restaurantId));
    }
}
