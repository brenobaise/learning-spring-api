package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders/{orderId}")
public class OrderStatusController {

    @Autowired
    OrderStatusService orderStatusService;

    @PutMapping("/confirmation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@PathVariable Long orderId){
        orderStatusService.confirm(orderId);
    }

    @PutMapping("/deliver")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deliver(@PathVariable Long orderId){
        orderStatusService.deliver(orderId);
    }

    @PutMapping("/cancellation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long orderId){
        orderStatusService.cancel(orderId);
    }

    /*



     */


}
