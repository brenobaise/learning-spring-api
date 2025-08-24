package com.baisebreno.learning_spring_api.domain.exceptions;

public class UserGroupNotFoundException extends EntityNotFoundException {
    public UserGroupNotFoundException(String message) {
        super(message);
    }

    public UserGroupNotFoundException(Long groupId) {
        this(String.format("UserGroup of id %d not found.",groupId));
    }
}
