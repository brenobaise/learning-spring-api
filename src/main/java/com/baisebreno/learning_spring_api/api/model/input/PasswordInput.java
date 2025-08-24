package com.baisebreno.learning_spring_api.api.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordInput {

    @NotBlank
    private String password;
    @NotBlank
    private String newPassword;
}
