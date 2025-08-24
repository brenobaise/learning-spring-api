package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.RestaurantNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.PaymentMethod;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service is responsible for persisting changes on the Restaurant Model.
 * This class can save and remove an entity.
 */
@Service
public class RestaurantRegistryService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    KitchenRegistryService kitchenRegistryService;

    @Autowired
    CityRegistryService cityRegistryService;

    @Autowired
    PaymentMethodService paymentMethodService;

    public static final String MESSAGE_RESTAURANT_IN_USE = "Restaurant of id %d cannot be removed, it's being used.";


    @Transactional
    public Restaurant save(Restaurant restaurant) {

        Long kitchenId = restaurant.getKitchen().getId();
        Long cityId = restaurant.getAddress().getCity().getId();

        Kitchen kitchen = kitchenRegistryService.findOne(kitchenId);
        City city = cityRegistryService.findOne(cityId);

        restaurant.setKitchen(kitchen);
        restaurant.getAddress().setCity(city);


        return restaurantRepository.save(restaurant);
    }


    @Transactional
    public void remove(Long restaurantId) {
        try {
            restaurantRepository.deleteById(restaurantId);
            restaurantRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new RestaurantNotFoundException( restaurantId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(
                    String.format(MESSAGE_RESTAURANT_IN_USE, restaurantId));
        }
    }

    public Restaurant findOne(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new RestaurantNotFoundException(restaurantId));
    }

    /**
     * Changes the status of a Restaurant to active (true).
     * @param restaurantId the target id
     */
    @Transactional
    public void activate(Long restaurantId){
        Restaurant restaurant = findOne(restaurantId);
        restaurant.activate();
    }

    /**
     * Changes the status of a Restaurant to deactivated(false)
     * @param restaurantId the target id
     */
    @Transactional
    public void deactivate(Long restaurantId){
        Restaurant restaurant = findOne(restaurantId);
        restaurant.deactivate();
    }

    @Transactional
    public void removePaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = findOne(restaurantId);

        PaymentMethod paymentMethod = paymentMethodService.findOne(paymentMethodId);
        restaurant.removePaymentMethod(paymentMethod);
    }

    @Transactional
    public void addPaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = findOne(restaurantId);

        PaymentMethod paymentMethod = paymentMethodService.findOne(paymentMethodId);
        restaurant.addPaymentMethod(paymentMethod);
    }


}
