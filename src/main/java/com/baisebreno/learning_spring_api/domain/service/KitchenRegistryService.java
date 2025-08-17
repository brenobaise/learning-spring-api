package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * This service class is responsible for persisting changes on the Kitchen model.
 * This service can save an entity and remove by id.
 */
@Service
public class KitchenRegistryService {

    public static final String MESSAGE_KITCHEN_NOT_FOUND = "Kitchen of id %d not found.";
    public static final String MESSAGE_KITCHEN_IN_USE = "Kitchen of id %d cannot be removed, it's being used.";


    @Autowired
    private KitchenRepository kitchenRepository;

    /**
     * Finds a Kitchen record by id. Throws an EntityNotFoundException if it fails.
     * @param id the id of the given record.
     * @return A Kitchen object or an EntityNotFoundException
     */
    public Kitchen findOne(Long id){
        return kitchenRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(MESSAGE_KITCHEN_NOT_FOUND, id))
        );
    }

    public Kitchen save(Kitchen kitchen){
       return kitchenRepository.save(kitchen);
    }

    public void remove(Long id ){
        try {
            kitchenRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException(
                    String.format(MESSAGE_KITCHEN_NOT_FOUND, id)
            );
        }catch (DataIntegrityViolationException e){
            throw new EntityInUseException(
                    String.format(MESSAGE_KITCHEN_IN_USE, id)
            );
        }


    }

}
