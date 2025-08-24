package com.baisebreno.learning_spring_api.domain.exceptions;

public class ProductNotFoundException extends EntityNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long productId, Long restaurantId){
        this(String.format("Product of id %d for Restaurand id %d,  not found.", productId, restaurantId));
    }
}
