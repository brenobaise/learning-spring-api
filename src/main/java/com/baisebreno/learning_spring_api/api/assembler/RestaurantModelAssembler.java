package com.baisebreno.learning_spring_api.api.assembler;

import com.baisebreno.learning_spring_api.api.model.KitchenModel;
import com.baisebreno.learning_spring_api.api.model.RestaurantModel;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler class responsible for converting {@link Restaurant} domain entities
 * into API representation models ({@link RestaurantModel}).
 * <p>
 * This class helps maintain a clean separation between the domain layer
 * and the API layer by providing transformation logic in one place.
 * </p>
 */
@Component
public class RestaurantModelAssembler {

    /**
     * Converts a single {@link Restaurant} domain object into a {@link RestaurantModel}.
     *
     * @param restaurant the domain object to be converted (must not be {@code null})
     * @return a {@link RestaurantModel} containing the transformed data
     */
    public RestaurantModel toModel(Restaurant restaurant) {
        RestaurantModel model = new RestaurantModel();
        KitchenModel kmodel = new KitchenModel();

        // Map Kitchen information
        kmodel.setId(restaurant.getKitchen().getId());
        kmodel.setName(restaurant.getKitchen().getName());

        // Map Restaurant information
        model.setId(restaurant.getId());
        model.setName(restaurant.getName());
        model.setDeliveryRate(restaurant.getDeliveryRate());
        model.setKitchen(kmodel);

        return model;
    }

    /**
     * Converts a collection of {@link Restaurant} domain objects into a list of {@link RestaurantModel}.
     * <p>
     * Uses {@link java.util.stream.Stream} API to perform the mapping in a clean and efficient way.
     * </p>
     *
     * @param restaurants the list of {@link Restaurant} domain entities
     * @return a list of {@link RestaurantModel} representing the API layer models
     */
    public List<RestaurantModel> toCollectionModel(List<Restaurant> restaurants) {
        return restaurants.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
