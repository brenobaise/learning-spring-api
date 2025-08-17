package com.baisebreno.learning_spring_api.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

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
    private Orders orders;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;


}
