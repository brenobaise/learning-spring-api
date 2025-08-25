package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.EntityInUseException;
import com.baisebreno.learning_spring_api.domain.exceptions.GroupNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Group;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupRegistryService {
    private static final String MSG_GROUP_IN_USE = "Group of id %d is in use, it cannot be deleted.";
    @Autowired
    GroupRepository repository;


    /**
     * Searches the database for a {@link Group} by id.
     * @param groupId the target id
     * @return if it exists {@link Group}, otherwise it returns a {@link GroupNotFoundException}
     */
    public Group findOne(Long groupId){
        return repository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException(groupId));
    }

    /**
     * Persists a new Group to the database.
     * @param userGroup a {@link Group}
     * @return a {@link Group}
     */
    @Transactional
    public Group save(Group userGroup){
        return repository.save(userGroup);
    }

    /**
     * Deletes a group by id from the database.
     * @param groupId the target id
     */
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
