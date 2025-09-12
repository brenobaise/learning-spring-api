package com.baisebreno.learning_spring_api.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class CityModel extends RepresentationModel<CityModel> {

    private Long id;
    private String name;
    private GeoStateModel state;
}
