package com.baisebreno.learning_spring_api.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Order entity inside the database.
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private BigDecimal subTotal;
    private BigDecimal deliveryRate;
    private BigDecimal total;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime confirmedDate;
    private LocalDateTime canceledDate;
    private LocalDateTime deliveredDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_customer_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "orders")
    private List<OrderItem> items = new ArrayList<>();

}
