package com.baisebreno.learning_spring_api.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Order entity inside the database.
 */
@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "subtotal")
    private BigDecimal subTotal;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryRate;

    private BigDecimal total;

    @CreationTimestamp
    private OffsetDateTime createdDate;

    private OffsetDateTime confirmedDate;
    private OffsetDateTime cancelledDate;
    private OffsetDateTime deliveredDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "user_customer_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();

    @Embedded
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    public void calculateTotalPrice() {
        this.subTotal = getItems().stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.total = this.subTotal.add(this.deliveryRate);
    }

    public void defineDeliveryRate() {
        setDeliveryRate(getRestaurant().getDeliveryRate());
    }

    public void mapOrderToItems() {
        getItems().forEach(item -> item.setOrder(this));
    }
}
