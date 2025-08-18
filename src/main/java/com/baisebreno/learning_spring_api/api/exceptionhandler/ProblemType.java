package com.baisebreno.learning_spring_api.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MESSAGE_NOT_READABLE("/message-not-readable", "Message Not Readable"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource Not Found"),
    ENTITY_IN_USE("/entity-in-use", "Entity In Use, cannot be deleted"),
    BUSINESS_LOGIC_ERROR("/business-logic-error", "Business Logic Violation"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid Parameter"),
    SYSTEM_ERROR("/system-error", "System Error");
    private static final String BASE_URL = "https://algafood.co.uk";


    private String title;
    private String uri;

    ProblemType(String path, String title){
        this.uri = BASE_URL + path;
        this.title = title;
    }
}
