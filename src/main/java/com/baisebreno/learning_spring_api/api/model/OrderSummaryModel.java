package com.baisebreno.learning_spring_api.api.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderSummaryModel {

    private String orderCode;
    private BigDecimal subTotal;
    private BigDecimal deliveryRate;
    private BigDecimal total;
    private String status;
    private OffsetDateTime createdDate;
    private RestaurantSummaryModel restaurant;
    private UserModel user;
}
