package com.baisebreno.learning_spring_api.api.assembler.product;

import com.baisebreno.learning_spring_api.api.assembler.GenericAssembler;
import com.baisebreno.learning_spring_api.api.model.PaymentMethodModel;
import com.baisebreno.learning_spring_api.api.model.ProductModel;
import com.baisebreno.learning_spring_api.api.model.input.ProductInputModel;
import com.baisebreno.learning_spring_api.domain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
public class ProductModelAssembler {
    @Autowired
    GenericAssembler genericAssembler;

    public List<ProductModel> toCollectionModel(List<Product> products){
        return genericAssembler.toCollectionModel(products,ProductModel.class);
    }

    public ProductModel toModel(Product foundProduct) {
        return genericAssembler.toSubject(foundProduct, ProductModel.class);
    }

    public Product disassemble(ProductInputModel inputModel) {
        return genericAssembler.disassemble(inputModel, Product.class);
    }

    public void copy(ProductInputModel productInputModel, Product product){
        genericAssembler.copy(productInputModel, product);
    }
}
