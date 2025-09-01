package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders/{orderCode}")
public class OrderStatusController {

    @Autowired
    OrderStatusService orderStatusService;

    @PutMapping("/confirmation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@PathVariable String orderCode){
        orderStatusService.confirm(orderCode);
    }

    @PutMapping("/deliver")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deliver(@PathVariable String orderCode){
        orderStatusService.deliver(orderCode);
    }

    @PutMapping("/cancellation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable String orderCode){
        orderStatusService.cancel(orderCode);
    }

    /*



     */


}
