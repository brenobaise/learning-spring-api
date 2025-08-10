package com.baisebreno.learning_spring_api.api.controller;


import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.baisebreno.learning_spring_api.domain.repository.GeographicalStateRepository;
import com.baisebreno.learning_spring_api.domain.service.GeographicalStateRegistryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/states")
public class GeographicalStateController {

    @Autowired
    private  GeographicalStateRepository stateRepository;

    @Autowired
    private GeographicalStateRegistryService stateRegistryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeographicalState> getAll(){
        return stateRepository.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeographicalState> find(@PathVariable Long id){
        GeographicalState foundState = stateRepository.getById(id);

        if(foundState != null){
            return ResponseEntity.ok(foundState);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<GeographicalState> create(@RequestBody GeographicalState state){
        return ResponseEntity.ok(stateRegistryService.save(state));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeographicalState> update(@PathVariable Long id, @RequestBody GeographicalState state){
        GeographicalState foundState = stateRepository.getById(id);

        if(foundState != null){
            BeanUtils.copyProperties(state, foundState, "id");
            foundState = stateRegistryService.save(foundState);
            return ResponseEntity.ok(foundState);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id){
        try{
            stateRegistryService.remove(id);
            return ResponseEntity.noContent().build();

        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();

        }catch (EntityInUseException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
 }
