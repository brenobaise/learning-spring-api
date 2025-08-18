package com.baisebreno.learning_spring_api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when a query for a resource is not found in the database.
 */
public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message){
        super(message);
    }
}
