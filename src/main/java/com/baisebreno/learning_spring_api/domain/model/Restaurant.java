package com.baisebreno.learning_spring_api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurant {


    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    private String name;

    private BigDecimal deliveryRate;

    @JsonIgnore
    @ManyToOne() // fetch = FetchType.LAZY only fetches when needed
//    @JsonIgnoreProperties("hibernateLazyInitializer")
    @JoinColumn(name = "kitchen_id", nullable = false)
    private Kitchen kitchen;

    @JsonIgnore
    @Embedded
    private Address address;

    @JsonIgnore
    @CreationTimestamp()
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime registeredDate;

    @JsonIgnore
    @UpdateTimestamp()
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime lastUpdatedDate;

//    @JsonIgnore
    @JoinTable(name = "restaurant_payment_types",
    joinColumns = @JoinColumn(name = "restaurant_id"),
    inverseJoinColumns = @JoinColumn(name = "payment_types_id"))
    @ManyToMany()
    private List<PaymentType> paymentTypes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private List<Product> products = new ArrayList<>();
}
