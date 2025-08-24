package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupsRepository extends JpaRepository<UserGroup,Long> {

}
