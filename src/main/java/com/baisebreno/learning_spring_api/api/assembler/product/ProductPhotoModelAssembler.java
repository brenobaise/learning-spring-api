package com.baisebreno.learning_spring_api.api.assembler.product;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.ProductPhotoModel;
import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductPhotoModelAssembler {

    @Autowired
    GenericAssembler genericAssembler;

    public ProductPhotoModel toModel (ProductPhoto productPhoto){
        return  genericAssembler.toSubject(productPhoto, ProductPhotoModel.class);
    }
}
