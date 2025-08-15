package com.baisebreno.learning_spring_api.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data // gens all getters/setters/tostring/equals
@Embeddable
public class Address {

    @Column(name = "address_postcode")
    private String postCode;

    @Column(name = "address_street")
    private String street;

    @Column(name = "address_number")
    private String number;

    @Column(name = "address_street_ext")
    private String streetExtension;

    @Column(name = "address_county")
    private String county;

    @ManyToOne()
    @JoinColumn(name ="address_city_id")
    private City city;
}
