package com.baisebreno.learning_spring_api.api.assembler.orders;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.OrderModel;
import com.baisebreno.learning_spring_api.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OrderModelAssembler {
    @Autowired
    GenericAssembler assembler;

    public OrderModel toModel(Order order){
        return assembler.toSubject(order, OrderModel.class);
    }

    public List<OrderModel> toCollectionModel(Collection<Order> orders){
        return assembler.toCollectionModel(orders, OrderModel.class);
    }

}
