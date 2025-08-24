package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.BusinessException;
import com.baisebreno.learning_spring_api.domain.exceptions.UserNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.User;
import com.baisebreno.learning_spring_api.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
public class UserRegistryService {
    @Autowired
    UserRepository userRepository;

    public User findOne(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id));
    }

    @Transactional
    public User save(User newUser) {
        userRepository.detach(newUser); // -> removes the instance from the persistence context
        Optional<User> existingUser = userRepository.findByEmail(newUser.getEmail());

        // checks if the email exists AND check if existing user is not equal to the incoming new user request.
        if(existingUser.isPresent() && !existingUser.get().equals(newUser)){
            throw new BusinessException(
                    String.format("Email %s is already registered.", newUser.getEmail())
            );
        }
        return userRepository.save(newUser);
    }

    @Transactional
    public void remove(Long id) {

        userRepository.deleteById(id);
    }

    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword ){
        User foundUser = findOne(userId);

        if(foundUser.notValidPassword(currentPassword)){
            throw new BusinessException("Incorrect Password");
        }

        foundUser.setPassword(newPassword);
    }
}
