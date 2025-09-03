package com.baisebreno.learning_spring_api.api.model;

import com.baisebreno.learning_spring_api.api.model.view.RestaurantView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KitchenModel {

    @JsonView(RestaurantView.Summary.class)
    private Long id;
    @JsonView(RestaurantView.Summary.class)
    private String name;
}
