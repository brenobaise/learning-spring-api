package com.baisebreno.learning_spring_api.domain.model;

import com.baisebreno.learning_spring_api.core.validation.DeliveryFee;
import com.baisebreno.learning_spring_api.core.validation.Groups;
import com.baisebreno.learning_spring_api.core.validation.Multiple;
import com.baisebreno.learning_spring_api.core.validation.ValueZeroIncludesDescription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ValueZeroIncludesDescription(
        fieldValue = "deliveryRate",
        descriptionField = "name",
        mandatoryDescription = "Free Delivery")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurant {

    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(name = "delivery_fee")
    @DeliveryFee
//    @Multiple(number = 5)
    private BigDecimal deliveryRate;

    @ConvertGroup(from = Default.class, to = Groups.KitchenId.class)
    @Valid // Enforces cascade validation
    @NotNull
    @ManyToOne() // fetch = FetchType.LAZY only fetches when needed
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

    @JsonIgnore
    @JoinTable(name = "restaurant_payment_method",
    joinColumns = @JoinColumn(name = "restaurant_id"),
    inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
    @ManyToMany()
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private List<Product> products = new ArrayList<>();
}
