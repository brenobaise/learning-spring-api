package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.RestaurantModelAssembler;
import com.baisebreno.learning_spring_api.api.model.KitchenModel;
import com.baisebreno.learning_spring_api.api.model.RestaurantModel;
import com.baisebreno.learning_spring_api.api.model.input.KitchenIdInput;
import com.baisebreno.learning_spring_api.api.model.input.RestaurantInputModel;
import com.baisebreno.learning_spring_api.core.validation.ValidationException;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.KitchenNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.RestaurantNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantRegistryService restaurantRegistryService;

    @Autowired
    private SmartValidator smartValidator;

    @Autowired
    private RestaurantModelAssembler restaurantModelAssembler;

    /**
     * This handler returns all {@link Restaurant} records in the database.
     * @return a List of {@link Restaurant}
     */
    @GetMapping
    public List<RestaurantModel> getAll(){

        return restaurantModelAssembler.toCollectionModel(restaurantRepository.findAll());
    }

    /**
     * This handler searches the database for an {@link Restaurant} entity by a given id.
     * @param id the id of the target entity.
     * @return {@code 200 | 404} status code
     */
    @GetMapping("/{id}")
    public RestaurantModel find(@PathVariable Long id) {
        Restaurant restaurant = restaurantRegistryService.findOne(id);
        return restaurantModelAssembler.toModel( restaurant);

    }

    /**
     * This handler adds a new {@link Restaurant} to the database.
     * @param restaurantInputModel the entity to be added
     * @return It may return the added resource, or a bad request status code.
     */
    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public RestaurantModel add(@Valid @RequestBody RestaurantInputModel restaurantInputModel){
        try{
            Restaurant restaurant = toDomainObject(restaurantInputModel);
            return restaurantModelAssembler.toModel(restaurantRegistryService.save(restaurant));
        }catch (RestaurantNotFoundException e){
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * This handler processes an update on a {@link Restaurant} entity by given id.
     * @param id The id of the Entity, passed through the url.
     * @param restaurantInputModel The representational model carrying the new data to be updated.
     * @return It can return the updated model, a not found status code , or a bad request status code.
     */
    @PutMapping("/{id}")
    public RestaurantModel update(@Valid @PathVariable Long id, @Valid @RequestBody RestaurantInputModel restaurantInputModel){
           try {
               Restaurant restaurant = toDomainObject(restaurantInputModel);

               Restaurant foundRestaurant = restaurantRegistryService.findOne(id);

               BeanUtils.copyProperties(restaurant, foundRestaurant,
                       "id","paymentType","address","registeredDate","products");
               return restaurantModelAssembler.toModel(restaurantRegistryService.save(foundRestaurant));
           }catch (KitchenNotFoundException e){
               throw new BusinessException(e.getMessage());
           }

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        restaurantRegistryService.remove(id);
    }


    private Restaurant toDomainObject(RestaurantInputModel restaurantInputModel){
        Restaurant restaurant = new Restaurant();
        restaurant.setName(restaurantInputModel.getName());
        restaurant.setDeliveryRate(restaurantInputModel.getDeliveryFee());

        Kitchen kitchen = new Kitchen();
        kitchen.setId(restaurantInputModel.getKitchen().getId());

        restaurant.setKitchen(kitchen);
        return restaurant;
    }
}
