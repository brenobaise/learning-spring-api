package com.baisebreno.learning_spring_api.api.model.input;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@ToString
public class ItemOrderInput {
    @NotNull
    @JsonProperty("id")                 // primary JSON name
    @JsonAlias({"productId"})           // also accept "productId" on input
    private Long productId;

    @NotNull
    @PositiveOrZero
    private Integer quantity;

    private String notes;


}
