package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.repository.CityRepository;
import com.baisebreno.learning_spring_api.domain.service.CityRegistryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CityRegistryService cityRegistryService;

    @GetMapping
    public ResponseEntity<List<City>> getAll(){
        return ResponseEntity.ok(cityRepository.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> find(@PathVariable Long id){
        City city = cityRepository.getById(id);

        if(city != null){
            return ResponseEntity.ok(city);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody City city){
        try{
            cityRegistryService.save(city);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(city);
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> update(@PathVariable Long id, @RequestBody City city){
        City foundCity = cityRepository.getById(id);

        if(foundCity != null){
            BeanUtils.copyProperties(city, foundCity, "id");
            foundCity = cityRegistryService.save(foundCity);
            return ResponseEntity.ok(foundCity);
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){
        try{
            cityRegistryService.remove(id);
            return ResponseEntity.noContent().build();

        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }catch (EntityInUseException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

}
