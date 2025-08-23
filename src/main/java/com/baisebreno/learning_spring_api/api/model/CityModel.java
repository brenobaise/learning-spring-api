package com.baisebreno.learning_spring_api.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityModel {

    private Long id;
    private String name;
    private GeoStateModel state;
}
