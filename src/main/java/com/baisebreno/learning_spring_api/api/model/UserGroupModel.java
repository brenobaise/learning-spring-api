package com.baisebreno.learning_spring_api.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserGroupModel {

    private Long id;

    private String name;

}
