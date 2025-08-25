package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.restaurant.RestaurantInputDisassembler;
import com.baisebreno.learning_spring_api.api.assembler.restaurant.RestaurantModelAssembler;
import com.baisebreno.learning_spring_api.api.model.PaymentMethodModel;
import com.baisebreno.learning_spring_api.api.model.RestaurantModel;
import com.baisebreno.learning_spring_api.api.model.input.RestaurantInputModel;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.CityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.KitchenNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.RestaurantNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.RestaurantRepository;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantRegistryService restaurantRegistryService;

    @Autowired
    private RestaurantModelAssembler restaurantModelAssembler;

    @Autowired
    private RestaurantInputDisassembler restaurantInputDisassembler;

    /**
     * This handler returns all {@link Restaurant} records in the database.
     *
     * @return a List of {@link Restaurant}
     */
    @GetMapping
    public List<RestaurantModel> getAll() {

        return restaurantModelAssembler.toCollectionModel(restaurantRepository.findAll());
    }

    /**
     * This handler searches the database for an {@link Restaurant} entity by a given id.
     *
     * @param id the id of the target entity.
     * @return {@code 200 | 404} status code
     */
    @GetMapping("/{id}")
    public RestaurantModel find(@PathVariable Long id) {
        Restaurant restaurant = restaurantRegistryService.findOne(id);
        return restaurantModelAssembler.toModel(restaurant);

    }

    /**
     * This handler adds a new {@link Restaurant} to the database.
     *
     * @param restaurantInputModel the entity to be added
     * @return It may return the added resource, or a bad request status code.
     */
    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public RestaurantModel add(@Valid @RequestBody RestaurantInputModel restaurantInputModel) {
        try {
            Restaurant restaurant = restaurantInputDisassembler.toDomainObject(restaurantInputModel);
            return restaurantModelAssembler.toModel(restaurantRegistryService.save(restaurant));
        } catch (KitchenNotFoundException | CityNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * This handler processes an update on a {@link Restaurant} entity by given id.
     *
     * @param id                   The id of the Entity, passed through the url.
     * @param restaurantInputModel The representational model carrying the new data to be updated.
     * @return It can return the updated model, a not found status code , or a bad request status code.
     */
    @PutMapping("/{id}")
    public RestaurantModel update(@Valid @PathVariable Long id, @Valid @RequestBody RestaurantInputModel restaurantInputModel) {
        try {
            Restaurant foundRestaurant = restaurantRegistryService.findOne(id);

            restaurantInputDisassembler.copyToDomainObject(restaurantInputModel, foundRestaurant);

            return restaurantModelAssembler.toModel(restaurantRegistryService.save(foundRestaurant));
        } catch (KitchenNotFoundException | CityNotFoundException e) {
            throw new BusinessException(e.getMessage());
        }

    }

    /**
     * Deletes a restaurant from the database.
     *
     * @param id the restaurant id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        restaurantRegistryService.remove(id);
    }

    /**
     * Sets a restaurant status as activated, in operation.
     *
     * @param id the restaurant id
     */
    @PutMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long id) {
        restaurantRegistryService.activate(id);
    }

    /**
     * Sets a restaurant status as deactivated, not in operation.
     *
     * @param id the restaurant id
     */
    @DeleteMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(@PathVariable Long id) {
        restaurantRegistryService.deactivate(id);
    }

    /**
     * Sets a restaurant as closed.
     *
     * @param restaurantId the restaurant id
     */
    @PutMapping("/{restaurantId}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeRestaurant(@PathVariable Long restaurantId) {
        restaurantRegistryService.close(restaurantId);
    }

    /**
     * Sets the restaurant as open.
     *
     * @param restaurantId the restaurant id
     */
    @PutMapping("/{restaurantId}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openRestaurant(@PathVariable Long restaurantId) {
        restaurantRegistryService.open(restaurantId);
    }

    /**
     * Activates all restaurants by the given id.
     *
     * @param restaurantIds a list of ids ex: [1,2,4]
     */
    @PutMapping("/activations")
    public void activateAll(@RequestBody List<Long> restaurantIds) {
        try {
            restaurantRegistryService.activateByBatch(restaurantIds);

        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * Deactivates all restaurants by the given id.
     *
     * @param restaurantIds a list of ids ex: [1,2,4]
     */
    @DeleteMapping("/deactivations")
    public void deactivateAll(@RequestBody List<Long> restaurantIds) {
        try {
            restaurantRegistryService.deactivateByBatch(restaurantIds);

        } catch (RestaurantNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * Finds all currently active restaurants.
     *
     * @return a list of activated restaurants.
     */
    @GetMapping("/activated")
    public List<RestaurantModel> currentlyActivated() {
        return restaurantModelAssembler.toCollectionModel(restaurantRegistryService.activated());
    }
}
