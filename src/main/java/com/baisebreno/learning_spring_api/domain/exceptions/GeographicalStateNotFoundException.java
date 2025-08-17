package com.baisebreno.learning_spring_api.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GeographicalStateNotFoundException extends EntityNotFoundException {
    public GeographicalStateNotFoundException(String message) {
        super(message);
    }

    public GeographicalStateNotFoundException(Long stateId) {
        this(String.format("No State id of %d found", stateId));
    }
}
