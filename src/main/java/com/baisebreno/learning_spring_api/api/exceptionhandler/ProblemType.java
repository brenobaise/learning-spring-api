package com.baisebreno.learning_spring_api.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    ENTITY_NOT_FOUND("/entity-not-found", "Entity Not Found"),
    ENTITY_IN_USE("/entity-in-use", "Entity In Use, cannot be deleted"),
    BUSINESS_LOGIC_ERROR("/business-logic-error", "Business Logic Violation");
    private static final String BASE_URL = "https://algafood.co.uk";


    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.uri = BASE_URL + path;
        this.title = title;
    }
}
