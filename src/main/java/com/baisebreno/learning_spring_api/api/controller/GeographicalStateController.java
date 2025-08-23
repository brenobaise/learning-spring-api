package com.baisebreno.learning_spring_api.api.controller;


import com.baisebreno.learning_spring_api.api.assembler.state.GeoStateInputDisassembler;
import com.baisebreno.learning_spring_api.api.assembler.state.GeoStateModelAssembler;
import com.baisebreno.learning_spring_api.api.model.GeoStateModel;
import com.baisebreno.learning_spring_api.api.model.input.GeoStateInput;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.baisebreno.learning_spring_api.domain.repository.GeographicalStateRepository;
import com.baisebreno.learning_spring_api.domain.service.GeographicalStateRegistryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/states")
public class GeographicalStateController {

    @Autowired
    private  GeographicalStateRepository stateRepository;

    @Autowired
    private GeographicalStateRegistryService stateRegistryService;

    @Autowired
    private GeoStateModelAssembler geoStateModelAssembler;
    @Autowired
    private GeoStateInputDisassembler geoStateInputDisassembler;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeoStateModel> getAll(){
        return geoStateModelAssembler.toCollectionModel(stateRepository.findAll());
    }

    /**
     * Searches the database for an {@link GeoStateModel} entity by a given id.
     * @param id the id of the target entity.
     * @return {@code 200  | 404} status code.
     */
    @GetMapping("/{id}")
    public GeoStateModel find(@PathVariable Long id){
        return  geoStateModelAssembler.toModel(stateRegistryService.findOne(id));
    }

    /**
     * Persists a new {@link GeoStateModel} entity into the database.
     * @param geoStateInput the representational model carrying the new entity.
     * @return {@code 200} status code.
     */
    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<GeoStateModel> create(@Valid @RequestBody GeoStateInput geoStateInput){
        GeographicalState state = geoStateInputDisassembler.toDomainObject(geoStateInput);
        return ResponseEntity.ok(geoStateModelAssembler.toModel(stateRegistryService.save(state)));
    }


    /**
     * Updates an entity by a given id.
     * @param id the id of the target entity.
     * @param geoStateInput the representational model carrying the new values.
     * @return {@code 200 | 404}
     */
    @PutMapping("/{id}")
    public GeoStateModel update(@PathVariable Long id, @Valid @RequestBody GeoStateInput geoStateInput){
        GeographicalState foundState = stateRegistryService.findOne(id);

            geoStateInputDisassembler.copyToDomainObject(geoStateInput, foundState);
            foundState = stateRegistryService.save(foundState);

        return geoStateModelAssembler.toModel(foundState);
    }

    /**
     * Removes an entity by a given id.
     * @param id the id of the target entity.
     * @return {@code  204 | 404 | 409}
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        stateRegistryService.remove(id);
    }
 }
