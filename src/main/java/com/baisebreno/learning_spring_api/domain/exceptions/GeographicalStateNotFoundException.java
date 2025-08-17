package com.baisebreno.learning_spring_api.domain.exceptions;


public class GeographicalStateNotFoundException extends EntityNotFoundException {
    public GeographicalStateNotFoundException(String message) {
        super(message);
    }

    public GeographicalStateNotFoundException(Long stateId) {
        this(String.format("No State with id %d, was found.", stateId));
    }
}
