package com.baisebreno.learning_spring_api.domain.repository.filter;

import com.baisebreno.learning_spring_api.domain.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@Setter
@Getter
public class OrderFilter {
    private Long userId;
    private Long restaurantId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime initialCreatedDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endingCreatedDate;

    private Long deliveryRate;
    private Long total;
}
