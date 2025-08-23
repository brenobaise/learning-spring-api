package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.kitchen.KitchenInputDisassembler;
import com.baisebreno.learning_spring_api.api.assembler.kitchen.KitchenModelAssembler;
import com.baisebreno.learning_spring_api.api.model.KitchenModel;
import com.baisebreno.learning_spring_api.api.model.input.KitchenInputModel;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import com.baisebreno.learning_spring_api.domain.service.KitchenRegistryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private KitchenModelAssembler kitchenModelAssembler;

    @Autowired
    private KitchenInputDisassembler kitchenInputDisassembler;

    @GetMapping()
    public List<KitchenModel> getAll() {
        return kitchenModelAssembler.toCollectionModel(kitchenRepository.findAll());
    }


    /**
     * Finds a {@link Kitchen} record based on a given id.
     * @param id the id of the target entity.
     * @return {@code 200 | 404 } status code
     */
    @GetMapping("{id}")
    private KitchenModel find(@PathVariable Long id) {
        return kitchenModelAssembler.toModel(kitchenRegistryService.findOne(id));
    }

    /**
     * Adds a new record of type {@link Kitchen} to the database.
     *
     * @param kitchen the representational model carrying the Entity data.
     * @return {@code 201} status code.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    private KitchenModel create(@Valid @RequestBody KitchenInputModel kitchen) {
        Kitchen newKitchen = kitchenInputDisassembler.toDomainObject(kitchen);
        return kitchenModelAssembler.toModel(kitchenRegistryService.save(newKitchen));

    }

    /**
     * Updates a {@link Kitchen} entity based on a given id.
     *
     * @param id      the id of the target entity.
     * @param kitchen The representational model carrying the new properties.
     * @return {@code 200 | 404} status code
     */
    @PutMapping("/{id}")
    private KitchenModel update(@PathVariable Long id,
                                @Valid @RequestBody KitchenInputModel kitchenInputModel) {
        Kitchen foundKitchen = kitchenRegistryService.findOne(id);

        // update the foundKitchen with new details
        kitchenInputDisassembler.copyToDomainObject(kitchenInputModel, foundKitchen);

        // persist changes to the database.
        return kitchenModelAssembler.toModel(kitchenRegistryService.save(foundKitchen));
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
