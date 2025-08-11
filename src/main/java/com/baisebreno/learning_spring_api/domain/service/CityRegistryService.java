package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.City;
import com.baisebreno.learning_spring_api.domain.model.GeographicalState;
import com.baisebreno.learning_spring_api.domain.repository.CityRepository;
import com.baisebreno.learning_spring_api.domain.repository.GeographicalStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * This class holds the responsibility of persisting changes on the City model.
 * It can save an entity and remove it. It also has a {@link CityRepository} and {@link GeographicalStateRepository} as a dependency.
 */
@Service
public class CityRegistryService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private GeographicalStateRepository stateRepository;

    public City save(City city){
        Long stateId = city.getState().getId();
        GeographicalState state = stateRepository.findById(stateId)
                .orElseThrow( () -> new EntityNotFoundException(
                String.format("State with id %d not found",stateId)));

        city.setState(state);

        return cityRepository.save(city);
    }

    public void remove(Long cityId){
        try{
            cityRepository.deleteById(cityId);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException(
                    String.format("City with code %d , not found", cityId));
        }catch (DataIntegrityViolationException e){
            throw new EntityInUseException(
                    String.format("City with code %d , in use, it cannot be deleted", cityId));
        }
    }


}
