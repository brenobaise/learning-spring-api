package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.GroupNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Group;
import com.baisebreno.learning_spring_api.domain.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupService {
    private static final String MSG_GROUP_IN_USE = "Group of id %d is in use, it cannot be deleted.";
    @Autowired
    GroupRepository repository;


    public Group findOne(Long groupId){
        return repository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException(groupId));
    }

    @Transactional
    public Group save(Group userGroup){
        return repository.save(userGroup);
    }

    @Transactional
    public void remove(Long groupId){
        try{
            repository.deleteById(groupId);
            repository.flush();
        }catch (EmptyResultDataAccessException e){
            throw new GroupNotFoundException(groupId);
        }catch (DataIntegrityViolationException e){
            throw new EntityInUseException(
                    String.format(MSG_GROUP_IN_USE, groupId));
        }
    }





}
