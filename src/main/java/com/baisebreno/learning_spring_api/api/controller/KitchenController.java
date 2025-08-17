package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.model.KitchenXmlModel;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.service.KitchenRegistryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/kitchens")
public class KitchenController {

    /**
     * This Repository is mostly used for retrieving data from the database.
     */
    @Autowired
    private KitchenRepository kitchenRepository;

    @Autowired
    private KitchenRegistryService kitchenRegistryService;

    @GetMapping()
    public List<Kitchen> getAll() {
        return kitchenRepository.findAll();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<KitchenXmlModel> getAllXmlFormatted() {
        return ResponseEntity.ok(new KitchenXmlModel(kitchenRepository.findAll()));
    }


    /**
     * Finds a {@link Kitchen} record based on a given id.
     * @param id the id of the target entity.
     * @return {@code 200 | 404 } status code
     */
    @GetMapping("{id}")
    private Kitchen find(@PathVariable Long id) {
        return kitchenRegistryService.findOne(id);
    }

    /**
     * Adds a new record of type {@link Kitchen} to the database.
     *
     * @param kitchen the representational model carrying the Entity data.
     * @return {@code 201} status code.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    private Kitchen create(@RequestBody Kitchen kitchen) {
        return kitchenRegistryService.save(kitchen);

    }

    /**
     * Updates a {@link Kitchen} entity based on a given id.
     *
     * @param id      the id of the target entity.
     * @param kitchen The representational model carrying the new properties.
     * @return {@code 200 | 404} status code
     */
    @PutMapping("/{id}")
    private Kitchen update(@PathVariable Long id, @RequestBody Kitchen kitchen) {
        Kitchen foundKitchen = kitchenRegistryService.findOne(id);

        // update the foundKitchen with new details
        BeanUtils.copyProperties(kitchen, foundKitchen, "id");

        // persist changes to the database.
        return kitchenRegistryService.save(foundKitchen);
    }


    /**
     * Removes a {@link Kitchen } entity from the database by using an id.
     *
     * @param id the id of the target entity.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        kitchenRegistryService.remove(id);
    }


}
