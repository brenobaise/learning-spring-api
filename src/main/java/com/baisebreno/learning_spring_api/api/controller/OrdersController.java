package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.orders.OrderInputAssembler;
import com.baisebreno.learning_spring_api.api.assembler.orders.OrderModelAssembler;
import com.baisebreno.learning_spring_api.api.assembler.orders.OrderSummaryModelAssembler;
import com.baisebreno.learning_spring_api.api.model.OrderModel;
import com.baisebreno.learning_spring_api.api.model.OrderSummaryModel;
import com.baisebreno.learning_spring_api.api.model.input.OrderInput;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Order;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.service.FireOrderService;
import com.baisebreno.learning_spring_api.domain.service.OrderRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    OrderRegistryService orderRegistryService;

    @Autowired
    OrderSummaryModelAssembler orderSummaryModel;
    @Autowired
    OrderModelAssembler orderModelAssembler;

    @Autowired
    OrderInputAssembler orderInputAssembler;

    @Autowired
    FireOrderService orderService;

    @GetMapping
    public List<OrderSummaryModel> getAllOrders(){
        return orderSummaryModel.toCollectionModel(orderRegistryService.getAll());

    }
    @GetMapping("/{orderCode}")
    public OrderModel find(@PathVariable String orderCode){
        return orderModelAssembler.toModel(orderRegistryService.findOne(orderCode));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel newOrder(@Valid @RequestBody OrderInput orderInput){
        try{
            Order newOrder = orderInputAssembler.toDomain(orderInput);

            newOrder.setUser(new User());
            newOrder.getUser().setId(1L);

            newOrder = orderService.fire(newOrder);

            return orderModelAssembler.toModel(newOrder);
        }catch (EntityNotFoundException e){
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{orderId}")
    public void updateOrder(){}

    @DeleteMapping("/{orderId}")
    public void deleteOrder(){}
}
