package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.model.KitchenXmlModel;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
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
    public List<Kitchen> getAll(){
        return kitchenRepository.findAll();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<KitchenXmlModel> getAllXmlFormatted(){
        return ResponseEntity.ok(new KitchenXmlModel(kitchenRepository.findAll()));
    }


    @GetMapping("{id}")
    private ResponseEntity<Kitchen> find(@PathVariable Long id){
        Optional<Kitchen> kitchen = kitchenRepository.findById(id);

        if(kitchen.isPresent()){
            return ResponseEntity.ok(kitchen.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    private Kitchen create(@RequestBody Kitchen kitchen){
        return kitchenRegistryService.save(kitchen);

    }

    @PutMapping("/{id}")
    private ResponseEntity<Kitchen> update(@PathVariable Long id, @RequestBody Kitchen kitchen){
        Optional<Kitchen> foundKitchen = kitchenRepository.findById(id);
        if(foundKitchen.isPresent()){

            // update the foundKitchen with new details
            BeanUtils.copyProperties(kitchen, foundKitchen.get(),"id");

            // persist to the database and save it to an instance, the changes
            Kitchen savedKitchen = kitchenRegistryService.save(foundKitchen.get());
            return ResponseEntity.ok(savedKitchen);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Kitchen> delete(@PathVariable Long id){
        try {
            kitchenRegistryService.remove(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (EntityInUseException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }


}
