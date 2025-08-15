package com.baisebreno.learning_spring_api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name = "Name of the table in the database") we can customise based on what we need

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Kitchen {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "name of the column in the database") this is automatically mapped by the ORM,
//    but we can assign it if we already have some column with some data
    @JsonProperty("title")
//    @JsonIgnore hides the property from the return object, but JsonProperty takes precedence over it.
    private String name;


    @JsonIgnore
    @OneToMany(mappedBy = "kitchen")
    private List<Restaurant> restaurant = new ArrayList<>();

}
