package com.baisebreno.learning_spring_api.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

@Getter
@Setter
public class PermissionModel {
    private Long id;

    private String name;

    private String description;
}
