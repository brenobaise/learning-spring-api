package com.baisebreno.learning_spring_api.api.model.mixin;

import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class CityMixin {
    @JsonIgnoreProperties(value = "name", allowGetters = true)
    private GeographicalState state;
}
