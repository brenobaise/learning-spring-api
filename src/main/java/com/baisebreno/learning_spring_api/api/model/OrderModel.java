package com.baisebreno.learning_spring_api.api.model;

import com.baisebreno.learning_spring_api.domain.model.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderModel {

    private String orderCode;
    private BigDecimal subTotal;
    private BigDecimal deliveryRate;
    private BigDecimal total;
    private String status;

    private OffsetDateTime createdDate;
    private OffsetDateTime confirmedDate;
    private OffsetDateTime cancelledDate;
    private OffsetDateTime deliveredDate;

    private PaymentMethodModel paymentMethod;

    private RestaurantSummaryModel restaurant;

    private UserModel user;

    private List<ItemOrderModel> items = new ArrayList<>();

    private AddressModel deliveryAddress;

}
