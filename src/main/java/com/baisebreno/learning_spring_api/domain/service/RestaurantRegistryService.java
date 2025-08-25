package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.RestaurantNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.*;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    UserRegistryService userRegistryService;

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
     * Activates the status of a Restaurant
     * @param restaurantId the target id
     */
    @Transactional
    public void activate(Long restaurantId){
        Restaurant restaurant = findOne(restaurantId);
        restaurant.activate();
    }

    /**
     * Deactivates the status of a restaurant
     * @param restaurantId the target id
     */
    @Transactional
    public void deactivate(Long restaurantId){
        Restaurant restaurant = findOne(restaurantId);
        restaurant.deactivate();
    }

    /**
     * Removes a type of payment method from the specified restaurant.
     * @param restaurantId the target id
     * @param paymentMethodId the payment method id
     */
    @Transactional
    public void removePaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = findOne(restaurantId);

        PaymentMethod paymentMethod = paymentMethodService.findOne(paymentMethodId);
        restaurant.removePaymentMethod(paymentMethod);
    }

    /**
     * Adds a new type of payment method to the specified restaurant.
     * @param restaurantId the target id
     * @param paymentMethodId the payment method id
     */
    @Transactional
    public void addPaymentMethod(Long restaurantId, Long paymentMethodId){
        Restaurant restaurant = findOne(restaurantId);

        PaymentMethod paymentMethod = paymentMethodService.findOne(paymentMethodId);
        restaurant.addPaymentMethod(paymentMethod);
    }

    /**
     * Fetches a restaurant by id and closes the restaurant for business.
     * @param restaurantId the target id
     */
    @Transactional
    public void close(Long restaurantId) {
        Restaurant restaurant = findOne(restaurantId);
        restaurant.closeRestaurant();
    }

    /**
     * Fetches a restaurant by id and opens the restaurant for business.
     * @param restaurantId the target id
     */
    @Transactional
    public void open(Long restaurantId) {
        Restaurant restaurant = findOne(restaurantId);
        restaurant.openRestaurant();
    }

    /**
     * Fetches a restaurant and a user, adds the user as a responsible for the restaurant.
     * @param restaurantId the restaurant id
     * @param userId the user id
     */
    @Transactional
    public void addUserToRestaurant(Long restaurantId, Long userId) {
        Restaurant restaurant = findOne(restaurantId);
        User user = userRegistryService.findOne(userId);

        restaurant.addResponsibleUser(user);
    }

    /**
     * Fetches a restaurant and a user, removes the user as a responsible for the restaurant.
     * @param restaurantId the restaurant id
     * @param userId the user id
     */
    @Transactional
    public void removeUserFromRestaurant(Long restaurantId, Long userId) {
        Restaurant restaurant = findOne(restaurantId);
        User user = userRegistryService.findOne(userId);

        restaurant.removeResponsibleUser(user);
    }

    /**
     * Activates a list of restaurant by batch.
     * @param restaurantsIds a list of restaurant ids to be activated.
     */
    @Transactional
    public void activateByBatch(List<Long> restaurantsIds){
        restaurantsIds.forEach(this::activate);
    }


    /**
     * Deactivate a list of restaurants by batch.
     * @param restaurantIds a list of restaurant ids to be deactivated.
     */
    @Transactional
    public void deactivateByBatch(List<Long> restaurantIds) {
        restaurantIds.forEach(this::deactivate);
    }

    /**
     * Searches for all restaurants that are active.
     * @return a {@link List<Restaurant>} which are active.
     */
    public List<Restaurant> activated() {
        return restaurantRepository.findAllByIsActive(true);
    }
}
