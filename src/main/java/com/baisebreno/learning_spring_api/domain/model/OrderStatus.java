package com.baisebreno.learning_spring_api.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the current status of a given Order.
 */
public enum OrderStatus {
    CREATED("Created"),
    CONFIRMED("Confirmed", CREATED),
    DELIVERED("Delivered", CONFIRMED),
    CANCELLED("Cancelled", CREATED, CONFIRMED);


    @Getter
    private String description;
    private List<OrderStatus> previousStatus;

    OrderStatus(String description, OrderStatus... previousStatus){
        this.description = description;
        this.previousStatus = Arrays.asList(previousStatus);
    }


    public boolean cannotChangeTo(OrderStatus orderStatus){
        /*
        if the incoming order status is not in my previouStatus list, then
        it is not allowed to change OrderStatus
         */
        return !orderStatus.previousStatus.contains(this);
    }


}
