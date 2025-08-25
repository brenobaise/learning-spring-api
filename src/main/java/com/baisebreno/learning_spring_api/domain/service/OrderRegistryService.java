package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.api.assembler.orders.OrderModelAssembler;
import com.baisebreno.learning_spring_api.api.model.OrderModel;
import com.baisebreno.learning_spring_api.domain.exceptions.OrderNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Order;
import com.baisebreno.learning_spring_api.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderRegistryService {

    @Autowired
    OrderRepository orderRepository;


    public Order findOne(Long orderId){
        return  orderRepository.findById(orderId)
                .orElseThrow(() ->  new OrderNotFoundException(orderId));
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }
}
