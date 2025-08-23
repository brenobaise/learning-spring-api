package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.GeographicalStateNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.baisebreno.learning_spring_api.domain.repository.GeographicalStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class is responsible for persisting changes on the GeographicalState Model.
 * It can save and remove an entity.
 */
@Service
public class GeographicalStateRegistryService {

    @Autowired
    private GeographicalStateRepository stateRepository;

    public static final String MESSAGE_STATE_IN_USE = "State of id %d cannot be removed, it's being used.";


    public GeographicalState findOne(Long stateId){
        return stateRepository.findById(stateId).orElseThrow(
                () -> new GeographicalStateNotFoundException(stateId)
        );
    }

    @Transactional
    public GeographicalState save(GeographicalState state) {
        return stateRepository.save(state);
    }

    @Transactional
    public void remove(Long stateId){
        try{
            stateRepository.deleteById(stateId);
            stateRepository.flush();

        }catch (EmptyResultDataAccessException e){
            throw new GeographicalStateNotFoundException(stateId);

        }catch (DataIntegrityViolationException e){
            throw new EntityInUseException(
                    String.format(MESSAGE_STATE_IN_USE,stateId));
        }
    }

}
