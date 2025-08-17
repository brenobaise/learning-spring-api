package com.baisebreno.learning_spring_api.domain.exceptions;

public class CityNotFoundException extends EntityNotFoundException {
    public CityNotFoundException(String message) {
        super(message);
    }

    public CityNotFoundException(Long cityId){
        this(String.format( "City of id %d not found.", cityId));
    }
}
