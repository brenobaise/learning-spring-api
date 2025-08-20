package com.baisebreno.learning_spring_api.api.model.mixin;

import com.baisebreno.learning_spring_api.core.validation.DeliveryFee;
import com.baisebreno.learning_spring_api.core.validation.Groups;
import com.baisebreno.learning_spring_api.domain.model.Address;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.PaymentMethod;
import com.baisebreno.learning_spring_api.domain.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public abstract class RestaurantMixin {
    @JsonIgnoreProperties(value = "restaurants", allowGetters = true)
    private Kitchen kitchen;

    @JsonIgnore
    @Embedded
    private Address address;

//    @JsonIgnore
    private LocalDateTime registeredDate;

//    @JsonIgnore
    private LocalDateTime lastUpdatedDate;

    @JsonIgnore
    private List<PaymentMethod> paymentMethods;
    @JsonIgnore
    private List<Product> products;
}
