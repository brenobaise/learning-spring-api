package com.baisebreno.learning_spring_api.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Represents an Item inside an Order entity
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private BigDecimal unitPrice;
    private BigDecimal total;
    private Integer quantity;
    private String notes;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    public void calculateTotalPrice(){
        BigDecimal unitPrice = this.getUnitPrice();
        Integer quantity = this.getQuantity();

        if(unitPrice == null){
            unitPrice = BigDecimal.ZERO;
        }
        if(quantity == null){
            quantity = 0;
        }

        this.setTotal(unitPrice.multiply(new BigDecimal(quantity)));
    }


}
