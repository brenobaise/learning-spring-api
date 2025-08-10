package com.baisebreno.learning_spring_api.domain.exceptions;

public class EntityInUseException extends RuntimeException {

    public EntityInUseException(String message){
        super(message);
    }
}
