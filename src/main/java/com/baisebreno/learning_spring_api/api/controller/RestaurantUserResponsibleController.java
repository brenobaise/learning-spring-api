package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.user.UserModelAssembler;
import com.baisebreno.learning_spring_api.api.model.UserModel;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import com.baisebreno.learning_spring_api.domain.service.UserRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/responsible")
public class RestaurantUserResponsibleController {

    @Autowired
    RestaurantRegistryService restaurantRegistryService;

    @Autowired
    UserRegistryService userRegistryService;

    @Autowired
    UserModelAssembler userModelAssembler;
    @GetMapping
    public List<UserModel> getAllResponsibleUsers(@PathVariable Long restaurantId){
        Restaurant restaurant = restaurantRegistryService.findOne(restaurantId);

        return userModelAssembler.toCollectionModel(restaurant.getResponsibleUsers());
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserInCharge(@PathVariable Long restaurantId, @PathVariable Long userId){
        restaurantRegistryService.addUserToRestaurant(restaurantId, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserInCharge(@PathVariable Long restaurantId, @PathVariable Long userId){
        restaurantRegistryService.removeUserFromRestaurant(restaurantId,userId);
    }
}
