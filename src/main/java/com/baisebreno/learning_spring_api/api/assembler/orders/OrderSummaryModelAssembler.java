package com.baisebreno.learning_spring_api.api.assembler.orders;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.OrderModel;
import com.baisebreno.learning_spring_api.api.model.OrderSummaryModel;
import com.baisebreno.learning_spring_api.domain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class OrderSummaryModelAssembler {
    @Autowired
    GenericAssembler assembler;

    public OrderSummaryModel toModel(Order order){
        return assembler.toSubject(order, OrderSummaryModel.class);
    }

    public List<OrderSummaryModel> toCollectionModel(Collection<Order> orders){
        return assembler.toCollectionModel(orders, OrderSummaryModel.class);
    }

}
