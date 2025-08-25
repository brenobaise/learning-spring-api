package com.baisebreno.learning_spring_api.domain.exceptions;

public class GroupNotFoundException extends EntityNotFoundException {
    public GroupNotFoundException(String message) {
        super(message);
    }

    public GroupNotFoundException(Long groupId) {
        this(String.format("Group of id %d not found.",groupId));
    }
}
