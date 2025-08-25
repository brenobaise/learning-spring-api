package com.baisebreno.learning_spring_api.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemOrderModel {

    private Long id;
//    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private String notes;
}
