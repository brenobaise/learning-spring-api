package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.RestaurantNotFoundException;
import com.baisebreno.learning_spring_api.domain.exceptions.UserGroupNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.UserGroup;
import com.baisebreno.learning_spring_api.domain.repository.UserGroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserGroupService {
    private static final String MSG_GROUP_IN_USE = "UserGroup of id %d is in use, it cannot be deleted.";
    @Autowired
    UserGroupsRepository repository;


    public UserGroup findOne(Long groupId){
        return repository.findById(groupId).orElseThrow(
                () -> new UserGroupNotFoundException(groupId));
    }

    @Transactional
    public UserGroup save(UserGroup userGroup){
        return repository.save(userGroup);
    }

    @Transactional
    public void remove(Long groupId){
        try{
            repository.deleteById(groupId);
            repository.flush();
        }catch (EmptyResultDataAccessException e){
            throw new UserGroupNotFoundException(groupId);
        }catch (DataIntegrityViolationException e){
            throw new EntityInUseException(
                    String.format(MSG_GROUP_IN_USE, groupId));
        }
    }





}
