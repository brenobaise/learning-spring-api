package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.baisebreno.learning_spring_api.domain.repository.GeographicalStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for persisting changes on the GeographicalState Model.
 * It can save and remove an entity.
 */
@Service
public class GeographicalStateRegistryService {

    @Autowired
    private GeographicalStateRepository stateRepository;

    public GeographicalState save(GeographicalState state) {
        return stateRepository.save(state);
    }

    public void remove(Long stateId){
        try{
            stateRepository.deleteById(stateId);

        }catch (EmptyResultDataAccessException e){

            throw new EntityNotFoundException(

                    String.format("State with id %d was not found",stateId));
        }catch (DataIntegrityViolationException e){

            throw new EntityInUseException(

                    String.format("State with id %d is in use, cannot be deleted",stateId));
        }
    }

}
