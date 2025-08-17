package com.baisebreno.learning_spring_api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when attempting to delete a resource that is being used, most likely a DataIntegrityException.
 */
public class EntityInUseException extends BusinessException {

    public EntityInUseException(String message){
        super(message);
    }
    public EntityInUseException(Long cityId) {
        this(String.format("Cannot delete this Entity as it's in use id: %d", cityId));
    }

}
