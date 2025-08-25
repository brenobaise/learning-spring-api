package com.baisebreno.learning_spring_api.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(nullable = false)
    private OffsetDateTime registeredDate;

    @ManyToMany
    @JoinTable(name = "user_user_groups", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_group_id"))
    private Set<Group> groups = new HashSet<>();

    public boolean validPassword(String currentPassword){
        return  getPassword().equals(currentPassword);
    }

    public boolean notValidPassword(String currentPassword){
        return !validPassword(currentPassword);
    }

    public void addToGroup(Group group){
        getGroups().add(group);
    }

    public void removeFromGroup(Group group){
        getGroups().remove(group);
    }
}
