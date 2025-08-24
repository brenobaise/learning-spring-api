package com.baisebreno.learning_spring_api.api.model;

import com.baisebreno.learning_spring_api.domain.model.City;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class AddressModel {

    private String postCode;

    private String street;

    private String number;

    private String county;

    private CitySummaryModel city;




}
