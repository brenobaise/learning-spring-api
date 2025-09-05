package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.product.ProductModelAssembler;
import com.baisebreno.learning_spring_api.api.model.ProductModel;
import com.baisebreno.learning_spring_api.api.model.input.ProductInputModel;
import com.baisebreno.learning_spring_api.domain.model.Product;
import com.baisebreno.learning_spring_api.domain.model.Restaurant;
import com.baisebreno.learning_spring_api.domain.repository.ProductRepository;
import com.baisebreno.learning_spring_api.domain.service.ProductRegistryService;
import com.baisebreno.learning_spring_api.domain.service.RestaurantRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/products")
public class RestaurantProductsController {
    @Autowired
    RestaurantRegistryService restaurantRegistryService;

    @Autowired
    ProductModelAssembler assembler;

    @Autowired
    ProductRegistryService productRegistryService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping()
    public List<ProductModel> getAll(@PathVariable Long restaurantId,
                                     @RequestParam(required = false) boolean includeInactive){

        Restaurant foundRestaurant = restaurantRegistryService.findOne(restaurantId);
        List<Product> allProducts = null;

        if(includeInactive){
            allProducts = productRepository.findAllByRestaurant(foundRestaurant);

        }else {
            allProducts = productRepository.findActiveByRestaurant(foundRestaurant);
        }
        return assembler.toCollectionModel(allProducts);

    }

    @GetMapping("/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ProductModel findOne(@PathVariable Long restaurantId, @PathVariable Long productId){
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
