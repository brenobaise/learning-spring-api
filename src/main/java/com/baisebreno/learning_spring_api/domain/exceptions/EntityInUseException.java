package com.baisebreno.learning_spring_api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to delete a resource that is being used, most likely a DataIntegrityException.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EntityInUseException extends RuntimeException {

    public EntityInUseException(String message){
        super(message);
    }
}
