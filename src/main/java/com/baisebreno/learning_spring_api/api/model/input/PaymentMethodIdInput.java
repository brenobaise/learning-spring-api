package com.baisebreno.learning_spring_api.api.model.input;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class PaymentMethodIdInput {

    @NotNull
    private Long id;
}
