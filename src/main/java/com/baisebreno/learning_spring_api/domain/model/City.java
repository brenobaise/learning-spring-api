package com.baisebreno.learning_spring_api.domain.model;

import com.baisebreno.learning_spring_api.core.validation.Groups;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.ConvertGroup;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Data
public class City {
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ConvertGroup(to = Groups.GeoGraphicalStateId.class)
    @Valid
    @NotNull
    @ManyToOne()
    @JoinColumn( nullable = false)
    private GeographicalState state;
}
