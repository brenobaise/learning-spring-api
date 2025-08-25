package com.baisebreno.learning_spring_api.domain.exceptions;

public class OrderNotFoundException extends EntityNotFoundException {
  public OrderNotFoundException(String message) {
    super(message);
  }

    public OrderNotFoundException(Long orderId){
      this(String.format("Order with id %d does not exist", orderId));
    }
}
