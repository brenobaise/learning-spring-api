package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.filter.DailySalesFilter;
import com.baisebreno.learning_spring_api.domain.model.dto.DailySales;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SalesQueryService {
    List<DailySales> findDailySales(DailySalesFilter dailySalesFilter);

}
