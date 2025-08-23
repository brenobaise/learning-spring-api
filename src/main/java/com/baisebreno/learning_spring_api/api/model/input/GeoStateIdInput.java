package com.baisebreno.learning_spring_api.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GeoStateIdInput {
    @NotNull
    private Long id;
}
