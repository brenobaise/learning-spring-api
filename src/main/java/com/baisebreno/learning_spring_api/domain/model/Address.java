package com.baisebreno.learning_spring_api.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    @Column(name = "address_district")
    private String county;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="address_city_id")
    private City city;
}
