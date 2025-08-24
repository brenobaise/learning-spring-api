package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.UserNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegistryService {
    @Autowired
    UserRepository userRepository;

    public User findOne(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
    }

    public User save(User newUser) {
        return userRepository.save(newUser);
    }

    public void update(Long id){

    }


    public void remove(Long id) {
        userRepository.deleteById(id);
    }
}
