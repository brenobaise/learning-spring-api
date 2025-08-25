package com.baisebreno.learning_spring_api.api.assembler.permission;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.PermissionModel;
import com.baisebreno.learning_spring_api.api.model.ProductModel;
import com.baisebreno.learning_spring_api.api.model.input.ProductInputModel;
import com.baisebreno.learning_spring_api.domain.model.Permission;
import com.baisebreno.learning_spring_api.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionModelAssembler {
    @Autowired
    GenericAssembler assembler;

    public List<PermissionModel> toCollectionModel(List<Permission> permissions){
        return assembler.toCollectionModel(permissions,PermissionModel.class);
    }

    public PermissionModel toModel(Permission permission) {
        return assembler.toSubject(permission, PermissionModel.class);
    }

}
