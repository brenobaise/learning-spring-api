package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * This service is responsible for persisting changes on the Restaurant Model.
 * This class can save and remove an entity.
 */
@Service
public class RestaurantRegistryService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    KitchenRepository kitchenRepository;

    @Autowired
    KitchenRegistryService kitchenRegistryService;


    public static final String MESSAGE_RESTAURANT_NOT_FOUND = "Restaurant of id %d not found.";
    public static final String MESSAGE_RESTAURANT_IN_USE = "Restaurant of id %d cannot be removed, it's being used.";


    public Restaurant findOne(Long id) {
        return restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(MESSAGE_RESTAURANT_NOT_FOUND, id))
        );

    }

    public Restaurant save(Restaurant restaurant) {

        Long kitchenId = restaurant.getKitchen().getId();

        Kitchen kitchen = kitchenRegistryService.findOne(kitchenId);

        restaurant.setKitchen(kitchen);

        return restaurantRepository.save(restaurant);
    }



    public void remove(Long id) {
        try {
            restaurantRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(
                    String.format(MESSAGE_RESTAURANT_NOT_FOUND, id));
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(
                    String.format(MESSAGE_RESTAURANT_IN_USE, id));
        }
    }
}
