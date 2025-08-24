package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.product.ProductModelAssembler;
import com.baisebreno.learning_spring_api.api.model.ProductModel;
import com.baisebreno.learning_spring_api.api.model.input.ProductInputModel;
import com.baisebreno.learning_spring_api.domain.model.Product;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.service.ProductRegistryService;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertFalseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/products")
public class RestaurantProducts {
    @Autowired
    RestaurantRegistryService restaurantRegistryService;

    @Autowired
    ProductModelAssembler assembler;

    @Autowired
    ProductRegistryService productRegistryService;



    @GetMapping()
    public List<ProductModel> getAllProducts(@PathVariable Long restaurantId){
        Restaurant foundRestaurant = restaurantRegistryService.findOne(restaurantId);
        return assembler.toCollectionModel(foundRestaurant.getProducts());

    }

    @GetMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductModel getOneProduct(@PathVariable Long restaurantId, @PathVariable Long productId){
        Product foundProduct = productRegistryService.findOne(restaurantId,productId);

        return assembler.toModel(foundProduct);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductModel create(@PathVariable Long restaurantId, @Valid @RequestBody ProductInputModel inputModel){
        Restaurant restaurant = restaurantRegistryService.findOne(restaurantId);
        Product product = assembler.disassemble(inputModel);
        product.setRestaurant(restaurant);

        product = productRegistryService.save(product);
        return assembler.toModel(product);
    }

    @PutMapping("/{productId}")
    public ProductModel update(@PathVariable Long restaurantId, @PathVariable Long productId,
                               @RequestBody @Valid ProductInputModel inputModel){
        Product foundProduct = productRegistryService.findOne(restaurantId,productId);

        assembler.copy(inputModel, foundProduct);

        foundProduct = productRegistryService.save(foundProduct);

        return assembler.toModel(foundProduct);

    }
}
