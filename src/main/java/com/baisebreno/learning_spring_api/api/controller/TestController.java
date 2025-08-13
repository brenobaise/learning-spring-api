package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import com.baisebreno.learning_spring_api.infrastructure.repository.spec.RestaurantContainingName;
import com.baisebreno.learning_spring_api.infrastructure.repository.spec.RestaurantWithFreeDeliverySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private KitchenRepository kitchenRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping("/kitchens/by-name")
    public List<Kitchen> kitchensByName(@RequestParam String name){
        return kitchenRepository.findAllByNameContaining(name);
    }

    @GetMapping("/kitchens/one-by-name")
    public Optional<Kitchen> kitchenByName(@RequestParam String name){
        return kitchenRepository.findByName(name);
    }
    @GetMapping("/restaurants/by-delivery-rate")
    public List<Restaurant> restaurantsByDeliveryRate(@RequestParam BigDecimal initialRate, BigDecimal finalRate){
        return restaurantRepository.findByDeliveryRateBetween(initialRate, finalRate);
    }

    @GetMapping("/restaurants/by-name-kitchen-id")
    public List<Restaurant> findByNameContainingAndKitchenId( String name,  Long id){
        return restaurantRepository.findByNameContainingAndKitchenId(name, id);
    }
    @GetMapping("/restaurants/first-by-name")
    public Optional<Restaurant> firstByName( String name){
        return restaurantRepository.findFirstRestaurantByNameContaining(name);
    }

    @GetMapping("/restaurants/count-kitchen")
    public int countKitchen(Long id){
        return restaurantRepository.countByKitchenId(id);
    }

    @GetMapping("/restaurants/find")
    public List<Restaurant> find(String name, BigDecimal initialRate, BigDecimal finalRate){
        return restaurantRepository.find(name, initialRate,finalRate);
    }

    @GetMapping("/restaurants/by-name-and-delivery-rate")
    public List<Restaurant> findRestaurant(String name, BigDecimal initialRate, BigDecimal finalRate){
        return restaurantRepository.find(name, initialRate,finalRate);
    }


    /**
     * Uses the Specification classes to return all restaurants containing the name and a deliveryRate of 0.
     * @param name the name of the possible entity
     * @return a List of restaurants
     */
    @GetMapping("/restaurants/free-delivery")
    public List<Restaurant> findByFreeDelivery(String name){
        var withFreeDelivery = new RestaurantWithFreeDeliverySpec();
        var containingName = new RestaurantContainingName(name);
        return restaurantRepository.findAll(withFreeDelivery.and(containingName));
    }

}
