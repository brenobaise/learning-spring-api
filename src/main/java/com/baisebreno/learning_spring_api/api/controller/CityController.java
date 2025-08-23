package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.city.CityInputDisassembler;
import com.baisebreno.learning_spring_api.api.assembler.city.CityModelAssembler;
import com.baisebreno.learning_spring_api.api.exceptionhandler.Problem;
import com.baisebreno.learning_spring_api.api.model.CityModel;
import com.baisebreno.learning_spring_api.api.model.input.CityInputModel;
import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.GeographicalStateNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.repository.CityRepository;
import com.baisebreno.learning_spring_api.domain.service.CityRegistryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityRegistryService cityRegistryService;

    @Autowired
    private CityModelAssembler assembler;

    @Autowired
    private CityInputDisassembler disassembler;

    /**
     * Returns all {@link City} records from the database.
     *
     * @return a List of {@link City} entities.
     */
    @GetMapping
    public ResponseEntity<List<CityModel>> getAll() {
        return ResponseEntity.ok(assembler.toCollectionModel(cityRepository.findAll()));
    }

    /**
     * Searches for an entity with a given id.
     *
     * @param id the id of the target entity.
     * @return {@code 200 | 404}
     */
    @GetMapping("/{id}")
    public CityModel find(@PathVariable Long id) {
        return assembler.toModel(cityRegistryService.findOne(id));
    }

    /**
     * Persists a new {@link City} entity into the database.
     *
     * @param cityInputModel the representational model carrying the new entity.
     * @return {@code 201 | 404}
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CityModel create(@Valid @RequestBody CityInputModel cityInputModel) {
        try {
            City city = disassembler.toDomainObject(cityInputModel);
            return assembler.toModel(cityRegistryService.save(city));
        } catch (GeographicalStateNotFoundException e){
            throw new BusinessException(e.getMessage(),e);
        }
    }

    /**
     * Updates a {@link City} entity by a given id.
     *
     * @param id   the id of the target entity.
     * @param cityInputModel the representational model carrying the new fields.
     * @return {@code 200 | 404}
     */
    @PutMapping("/{id}")
    public CityModel update(@PathVariable Long id, @Valid @RequestBody CityInputModel cityInputModel) {
        try{
            City foundCity = cityRegistryService.findOne(id);

            disassembler.copyToDomainObject(cityInputModel, foundCity);

            return  assembler.toModel(cityRegistryService.save(foundCity));
        }catch (GeographicalStateNotFoundException e){
            throw new BusinessException(e.getMessage(),e);
        }
    }


    /**
     * Deletes an entity from the database by a given id.
     *
     * @param id the id of the target entity.
     * @return {@code 204 | 404 | 409} status code.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        cityRegistryService.remove(id);
    }

}
