package com.baisebreno.learning_spring_api.domain.exceptions;

public class KitchenNotFoundException extends EntityNotFoundException {
    public KitchenNotFoundException(String message) {
        super(message);
    }

    public KitchenNotFoundException(Long kitchenId){
        this(String.format("Kitchen of id %d not found.", kitchenId));
    }
}
