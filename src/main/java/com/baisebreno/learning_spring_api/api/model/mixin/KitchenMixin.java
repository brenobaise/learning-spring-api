package com.baisebreno.learning_spring_api.api.model.mixin;

import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class KitchenMixin {
    @JsonIgnore
    private List<Restaurant> restaurantList;
}
