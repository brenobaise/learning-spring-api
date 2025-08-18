package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.exceptionhandler.Problem;
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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityRegistryService cityRegistryService;


    /**
     * Returns all {@link City} records from the database.
     *
     * @return a List of {@link City} entities.
     */
    @GetMapping
    public ResponseEntity<List<City>> getAll() {
        return ResponseEntity.ok(cityRepository.findAll());
    }

    /**
     * Searches for an entity with a given id.
     *
     * @param id the id of the target entity.
     * @return {@code 200 | 404}
     */
    @GetMapping("/{id}")
    public City find(@PathVariable Long id) {
        return cityRegistryService.findOne(id);
    }

    /**
     * Persists a new {@link City} entity into the database.
     *
     * @param city the representational model carrying the new entity.
     * @return {@code 201 | 404}
     */
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public City create(@Valid @RequestBody City city) {
        try {
            return cityRegistryService.save(city);
        } catch (GeographicalStateNotFoundException e){
            throw new BusinessException(e.getMessage(),e);
        }
    }

    /**
     * Updates a {@link City} entity by a given id.
     *
     * @param id   the id of the target entity.
     * @param city the representational model carrying the new fields.
     * @return {@code 200 | 404}
     */
    @PutMapping("/{id}")
    public City update(@PathVariable Long id, @Valid @RequestBody City city) {
        try{
            City foundCity = cityRegistryService.findOne(id);

            BeanUtils.copyProperties(city, foundCity, "id");

            return  cityRegistryService.save(foundCity);
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
