package com.baisebreno.learning_spring_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class DailySales {

    private Date date;
    private Long totalSales;
    private BigDecimal totalRevenue;


}
