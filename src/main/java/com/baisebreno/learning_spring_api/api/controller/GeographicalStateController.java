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
import java.util.Optional;

@RestController
@RequestMapping("/states")
public class GeographicalStateController {

    @Autowired
    private  GeographicalStateRepository stateRepository;

    @Autowired
    private GeographicalStateRegistryService stateRegistryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeographicalState> getAll(){
        return stateRepository.findAll();
    }

    /**
     * Searches the database for an {@link GeographicalState} entity by a given id.
     * @param id the id of the target entity.
     * @return {@code 200  | 404} status code.
     */
    @GetMapping("/{id}")
    public GeographicalState find(@PathVariable Long id){
        return  stateRegistryService.findOne(id);
    }

    /**
     * Persists a new {@link GeographicalState} entity into the database.
     * @param state the representational model carrying the new entity.
     * @return {@code 200} status code.
     */
    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<GeographicalState> create(@RequestBody GeographicalState state){
        return ResponseEntity.ok(stateRegistryService.save(state));
    }


    /**
     * Updates an entity by a given id.
     * @param id the id of the target entity.
     * @param state the representational model carrying the new values.
     * @return {@code 200 | 404}
     */
    @PutMapping("/{id}")
    public GeographicalState update(@PathVariable Long id, @RequestBody GeographicalState state){
        GeographicalState foundState = stateRegistryService.findOne(id);

            BeanUtils.copyProperties(state, foundState, "id");
            foundState = stateRegistryService.save(foundState);

        return foundState;
    }

    /**
     * Removes an entity by a given id.
     * @param id the id of the target entity.
     * @return {@code  204 | 404 | 409}
     */
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
