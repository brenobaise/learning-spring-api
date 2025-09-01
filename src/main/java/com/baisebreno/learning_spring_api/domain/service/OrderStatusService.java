package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.model.Order;
import com.baisebreno.learning_spring_api.domain.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class OrderStatusService {


    @Autowired
    FireOrderService orderService;

    @Transactional
    public void confirm(String orderCode){
        Order order = orderService.findOne(orderCode);
        order.confirm();

    }

    @Transactional
    public void deliver(String orderCode) {
        Order order = orderService.findOne(orderCode);
        order.deliver();
    }

    @Transactional
    public void cancel(String orderId) {
        Order order = orderService.findOne(orderId);

        order.cancel();
    }
}
