package com.baisebreno.learning_spring_api.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserWithPasswordInput extends UserInputModel {
    @NotBlank
    private String password;
}
