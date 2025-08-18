package com.baisebreno.learning_spring_api.core.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@AllArgsConstructor
@Getter
public class ValidationException extends RuntimeException {
    private BindingResult bindingResult;
    public ValidationException(String message) {
        super(message);
    }
}
