package com.baisebreno.learning_spring_api.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantModel {

    private Long id;
    private String name;
    private BigDecimal deliveryRate;
    private KitchenModel kitchen;
    private Boolean isActive;
    private AddressModel address;
    private Boolean isOpen;

}
