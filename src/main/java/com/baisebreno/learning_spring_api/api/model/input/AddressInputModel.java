package com.baisebreno.learning_spring_api.api.model.input;

import com.baisebreno.learning_spring_api.api.model.CitySummaryModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class AddressInputModel {
    @NotBlank
    private String postCode;

    @NotBlank
    private String street;

    @NotBlank
    private String number;

    @NotBlank
    private String county;

    @Valid
    @NotNull
    private CityIdInput city;

}
