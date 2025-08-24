package com.baisebreno.learning_spring_api.api.assembler.restaurant;

import com.baisebreno.learning_spring_api.api.model.input.RestaurantInputModel;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Disassembler class responsible for converting API input models
 * ({@link RestaurantInputModel}) into domain entities ({@link Restaurant}).
 * <p>
 * This ensures that the controller layer deals with DTOs while the
 * domain layer works only with domain objects, keeping a clean
 * separation of concerns.
 * </p>
 */
@Component
public class RestaurantInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;


    /**
     * Converts a {@link RestaurantInputModel} into a {@link Restaurant} domain object.
     * <p>
     * This method maps only the fields required to construct a new {@link Restaurant}.
     * It also creates a {@link Kitchen} reference using only its identifier, so that
     * the association is properly established without fetching the entire entity.
     * </p>
     *
     * @param restaurantInputModel the input model provided by the API client (must not be {@code null})
     * @return a new {@link Restaurant} instance populated with the input data
     */
    public Restaurant toDomainObject(RestaurantInputModel restaurantInputModel) {
        return modelMapper.map(restaurantInputModel, Restaurant.class);
    }

    public void copyToDomainObject(RestaurantInputModel restaurantInputModel, Restaurant restaurant){
        // to prevent  org.hibernate.HibernateException: identifier of an instance of
        //		 domain.model.Restaurant was altered from 1 to 2
        restaurant.setKitchen(new Kitchen());

        if(restaurant.getAddress() != null){
            restaurant.getAddress().setCity(new City());
        }

         modelMapper.map(restaurantInputModel, restaurant);
    }
}
