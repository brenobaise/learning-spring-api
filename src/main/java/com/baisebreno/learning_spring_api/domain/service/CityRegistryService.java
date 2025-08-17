package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.CityNotFoundException;
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
    private GeographicalStateRegistryService stateService;


    public City save(City city) {
        Long stateId = city.getState().getId();

        GeographicalState state = stateService.findOne(stateId);

        city.setState(state);

        return cityRepository.save(city);
    }

    public void remove(Long cityId) {
        try {
            cityRepository.deleteById(cityId);

        } catch (EmptyResultDataAccessException e) {
            throw new CityNotFoundException(cityId);

        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(cityId);
        }
    }

    public City findOne(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(
                () -> new CityNotFoundException( cityId));
    }


}
