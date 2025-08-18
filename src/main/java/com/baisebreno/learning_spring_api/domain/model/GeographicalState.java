package com.baisebreno.learning_spring_api.domain.model;

import com.baisebreno.learning_spring_api.core.validation.Groups;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "state")
public class GeographicalState {

    @NotNull(groups = Groups.GeoGraphicalStateId.class)
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;
}
