package com.baisebreno.learning_spring_api.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class UserGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @JoinTable(name = "user_group_permissions",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @ManyToMany
    private List<Permission> permissions = new ArrayList<>();

    public void removePermission(Permission permission){
        getPermissions().remove(permission);
    }

    public void addPermission(Permission permission){
        getPermissions().add(permission);
    }
}
