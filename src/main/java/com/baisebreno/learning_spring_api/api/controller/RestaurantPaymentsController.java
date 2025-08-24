package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.payment.PaymentMethodModelAssembler;
import com.baisebreno.learning_spring_api.api.model.PaymentMethodModel;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/restaurants/{restaurantId}/payments")
public class RestaurantPaymentsController {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantRegistryService restaurantRegistryService;
    @Autowired
    private PaymentMethodModelAssembler paymentMethodModelAssembler;

    @GetMapping()
    public List<PaymentMethodModel> getAllPaymentMethods(@PathVariable Long restaurantId) {
        Restaurant restaurant = restaurantRegistryService.findOne(restaurantId);

        return paymentMethodModelAssembler.toCollectionModel(restaurant.getPaymentMethods());
    }

    @PutMapping("/{paymentTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPaymentType(@PathVariable Long restaurantId, @PathVariable Long paymentTypeId){
        restaurantRegistryService.addPaymentMethod(restaurantId,paymentTypeId);
    }

    @DeleteMapping("/{paymentTypeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePaymentType(@PathVariable Long restaurantId, @PathVariable Long paymentTypeId){
        restaurantRegistryService.removePaymentMethod(restaurantId,paymentTypeId);
    }
}
