package com.baisebreno.learning_spring_api.api.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ProductPhotoModel {

    private String fileName;
    private String description;
    private String contentType;
    private Long size;

}
