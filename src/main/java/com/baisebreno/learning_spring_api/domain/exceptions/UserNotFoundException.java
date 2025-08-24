package com.baisebreno.learning_spring_api.domain.exceptions;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long userId){
        this(String.format("User of id %d not found.",userId));
    }
}
