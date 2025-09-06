package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.filter.DailySalesFilter;
import com.baisebreno.learning_spring_api.domain.model.dto.DailySales;
import com.baisebreno.learning_spring_api.domain.service.SalesQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/insights")
public class InsightsController {

    @Autowired
    private SalesQueryService salesQueryService;


    @GetMapping("/daily-sales")
    public List<DailySales> getDailySales(DailySalesFilter filter){
        return salesQueryService.findDailySales(filter);

    }
}
