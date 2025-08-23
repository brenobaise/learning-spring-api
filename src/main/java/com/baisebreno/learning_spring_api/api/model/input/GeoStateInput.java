package com.baisebreno.learning_spring_api.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class GeoStateInput {
    @NotBlank
    private String name;

}
