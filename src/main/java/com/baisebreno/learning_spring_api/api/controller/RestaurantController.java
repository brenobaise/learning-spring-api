package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.KitchenNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.RestaurantNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantRegistryService restaurantRegistryService;

    /**
     * This handler returns all {@link Restaurant} records in the database.
     * @return a List of {@link Restaurant}
     */
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll(){
        return ResponseEntity.ok(restaurantRepository.findAll());
    }

    /**
     * This handler searches the database for an {@link Restaurant} entity by a given id.
     * @param id the id of the target entity.
     * @return {@code 200 | 404} status code
     */
    @GetMapping("/{id}")
    public Restaurant find(@PathVariable Long id){
        return restaurantRegistryService.findOne(id);
    }

    /**
     * This handler adds a new {@link Restaurant} to the database.
     * @param restaurant the entity to be added
     * @return It may return the added resource, or a bad request status code.
     */
    @PostMapping()
    public Restaurant add(@RequestBody Restaurant restaurant){
        try{
            return restaurantRegistryService.save(restaurant);
        }catch (RestaurantNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * This handler processes an update on a {@link Restaurant} entity by given id.
     * @param id The id of the Entity, passed through the url.
     * @param restaurant The representational model carrying the new data to be updated.
     * @return It can return the updated model, a not found status code , or a bad request status code.
     */
    @PutMapping("/{id}")
    public Restaurant update(@PathVariable Long id, @RequestBody Restaurant restaurant){
           Restaurant foundRestaurant = restaurantRegistryService.findOne(id);

           BeanUtils.copyProperties(restaurant, foundRestaurant, "id","paymentType","address","registeredDate","products");

           try {
               return restaurantRegistryService.save(foundRestaurant);
           }catch (KitchenNotFoundException e){
               throw new BusinessException(e.getMessage());
           }

    }

    /**
     * This handler processes a partial update on a given {@link Restaurant} resource.
     * It uses an object mapper to differentiate what the user wants to change.
     * @param id The id of the target Entity
     * @param fields the json fields to be changed.
     * @return It may return the patched model or a not found status code.
     */
    @PatchMapping("/{id}")
    public Restaurant patch(@PathVariable Long id,
                                   @RequestBody Map<String, Object> fields){

        Restaurant foundRestaurant = restaurantRegistryService.findOne(id);

        merge(fields, foundRestaurant);

        return update(id, foundRestaurant);
    }

    /**
     *  * Merges a set of field values into an existing {@link Restaurant} entity instance.
     *  * <p>
     *  * This method is typically used for partial updates (e.g., PATCH requests),
     *  * where only specific fields are provided by the client and should be updated
     *  * on the target entity, leaving all other fields untouched.
     *  * </p>
     *  *
     *  @param fieldsOrigin    a map of field names to their new values, representing
     *                         the fields to update (e.g., {"name": "New Name", "deliveryFee": 5.99}).
     *  @param restaurantTarget the target Restaurant entity to be updated in-place.
     */
    private static void merge(Map<String, Object> fieldsOrigin, Restaurant restaurantTarget) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurant originRestaurant = objectMapper.convertValue(fieldsOrigin, Restaurant.class);

        fieldsOrigin.forEach((nameProperty, valueProperty)-> {
            Field field = ReflectionUtils.findField(Restaurant.class, nameProperty);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, originRestaurant);


            ReflectionUtils.setField(field, restaurantTarget, newValue);


        });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        restaurantRegistryService.remove(id);
    }
}
