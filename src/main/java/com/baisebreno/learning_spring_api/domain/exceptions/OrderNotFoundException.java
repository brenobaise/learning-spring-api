package com.baisebreno.learning_spring_api.domain.exceptions;

public class OrderNotFoundException extends EntityNotFoundException {
    public OrderNotFoundException(String orderCode){
      super(String.format("Order with id %s does not exist", orderCode));
    }
}
