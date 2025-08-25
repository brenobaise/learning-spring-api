package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.orders.OrderModelAssembler;
import com.baisebreno.learning_spring_api.api.assembler.orders.OrderSummaryModelAssembler;
import com.baisebreno.learning_spring_api.api.model.OrderModel;
import com.baisebreno.learning_spring_api.api.model.OrderSummaryModel;
import com.baisebreno.learning_spring_api.domain.service.OrderRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    OrderRegistryService orderRegistryService;

    @Autowired
    OrderSummaryModelAssembler orderSummaryModel;,
    @Autowired
    OrderModelAssembler orderModelAssembler;

    @GetMapping
    public List<OrderSummaryModel> getAllOrders(){
        return orderSummaryModel.toCollectionModel(orderRegistryService.getAll());

    }
    @GetMapping("/{orderId}")
    public OrderModel find(@PathVariable Long orderId){
        return orderModelAssembler.toModel(orderRegistryService.findOne(orderId));
    }

    @PostMapping("/{orderId}")
    public void createNewOrder(){}

    @PutMapping("/{orderId}")
    public void updateOrder(){}

    @DeleteMapping("/{orderId}")
    public void deleteOrder(){}
}
