package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
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

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll(){
        return ResponseEntity.ok(restaurantRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> find(@PathVariable Long id){
        Optional<Restaurant> foundRestaurant = restaurantRepository.findById(id);

        if(foundRestaurant.isPresent()){
            return ResponseEntity.ok(foundRestaurant.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<?> add(@RequestBody Restaurant restaurant){
        try{
            restaurant = restaurantRegistryService.save(restaurant);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(restaurant);
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Restaurant restaurant){
        try {
           Restaurant foundRestaurant = restaurantRepository.findById(id)
                    .orElse(null);

            if (foundRestaurant != null) {
                BeanUtils.copyProperties(restaurant, foundRestaurant, "id");

                foundRestaurant = restaurantRegistryService.save(foundRestaurant);
                return ResponseEntity.ok(foundRestaurant);
            }

            return ResponseEntity.notFound().build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id,
                                   @RequestBody Map<String, Object> fields){

        Restaurant foundRestaurant = restaurantRepository.findById(id).orElse(null);

        if(foundRestaurant == null){
            return ResponseEntity.notFound().build();
        }


        merge(fields, foundRestaurant);

        return update(id, foundRestaurant);
    }

    private static void merge(Map<String, Object> fieldsOrigin, Restaurant restaurantTarget) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurant originRestaurant = objectMapper.convertValue(fieldsOrigin, Restaurant.class);

        System.out.println(originRestaurant);



        fieldsOrigin.forEach((nameProperty, valueProperty)-> {
            Field field = ReflectionUtils.findField(Restaurant.class, nameProperty);
            field.setAccessible(true);

            Object newValue = ReflectionUtils.getField(field, originRestaurant);


            ReflectionUtils.setField(field, restaurantTarget, newValue);


        });
    }
}
