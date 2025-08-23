package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.KitchenNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Kitchen;
import com.baisebreno.learning_spring_api.domain.repository.KitchenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service class is responsible for persisting changes on the Kitchen model.
 * This service can save an entity and remove by id.
 */
@Service
public class KitchenRegistryService {

    public static final String MESSAGE_KITCHEN_IN_USE = "Kitchen of id %d cannot be removed, it's being used.";

    @Autowired
    private KitchenRepository kitchenRepository;

    @Transactional
    public Kitchen save(Kitchen kitchen){
       return kitchenRepository.save(kitchen);
    }

    @Transactional
    public void remove(Long kitchenId ){
        try {
            kitchenRepository.deleteById(kitchenId);
            kitchenRepository.flush();
        }catch (EmptyResultDataAccessException e){
            throw new KitchenNotFoundException(kitchenId);

        }catch (DataIntegrityViolationException e){
            throw new EntityInUseException(
                    String.format(MESSAGE_KITCHEN_IN_USE, kitchenId)
            );
        }
    }

    /**
     * Finds a Kitchen record by id. Throws an EntityNotFoundException if it fails.
     * @param kitchenId the id of the given record.
     * @return A Kitchen object or an EntityNotFoundException
     */
    public Kitchen findOne(Long kitchenId){
        return kitchenRepository.findById(kitchenId).orElseThrow(
                () -> new KitchenNotFoundException( kitchenId));
    }
}
