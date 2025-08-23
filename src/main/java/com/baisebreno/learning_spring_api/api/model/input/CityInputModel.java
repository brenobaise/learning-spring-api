package com.baisebreno.learning_spring_api.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CityInputModel {

    @NotNull
    private String name;

    @Valid
    @NotNull
    private GeoStateIdInput state;
}
