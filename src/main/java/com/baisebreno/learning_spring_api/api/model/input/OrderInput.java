package com.baisebreno.learning_spring_api.api.model.input;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class OrderInput {
    @Valid
    @NotNull
    private RestaurantIdInput restaurant;

    @Valid
    @NotNull
    @JsonProperty("address")
    private AddressInputModel deliveryAddress;

    @Valid
    @NotNull
    private PaymentMethodIdInput paymentType;

    @Valid
    @Size(min = 1)
    @NotNull
    private List<ItemOrderInput> items;


    @Override
    public String toString() {
        return "OrderInput{" +
                "restaurant=" + restaurant.toString() +
                ", address=" + deliveryAddress.toString() +
                ", paymentType=" + paymentType.toString() +
                ", items=" + items.toString() +
                '}';
    }
}
