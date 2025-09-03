package com.baisebreno.learning_spring_api.api.model;

import com.baisebreno.learning_spring_api.api.model.view.RestaurantView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantModel {

    @JsonView(RestaurantView.Summary.class)
    private Long id;

    @JsonView(RestaurantView.Summary.class)
    private String name;

    @JsonView(RestaurantView.Summary.class)
    private BigDecimal deliveryRate;

    @JsonView(RestaurantView.Summary.class)
    private KitchenModel kitchen;

    private Boolean isActive;
    private AddressModel address;
    private Boolean isOpen;

}
