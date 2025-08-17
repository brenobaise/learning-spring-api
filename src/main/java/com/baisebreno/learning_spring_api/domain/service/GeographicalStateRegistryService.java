package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.City;
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

    public static final String MESSAGE_STATE_NOT_FOUND = "State of id %d not found.";
    public static final String MESSAGE_CITY_IN_USE = "State of id %d cannot be removed, it's being used.";


    public GeographicalState findOne(Long id){
        return stateRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(MESSAGE_STATE_NOT_FOUND, id))
        );

    }

    public GeographicalState save(GeographicalState state) {
        return stateRepository.save(state);
    }

    public void remove(Long stateId){
        try{
            stateRepository.deleteById(stateId);

        }catch (EmptyResultDataAccessException e){

            throw new EntityNotFoundException(

                    String.format(MESSAGE_STATE_NOT_FOUND,stateId));
        }catch (DataIntegrityViolationException e){

            throw new EntityInUseException(

                    String.format(MESSAGE_CITY_IN_USE,stateId));
        }
    }

}
