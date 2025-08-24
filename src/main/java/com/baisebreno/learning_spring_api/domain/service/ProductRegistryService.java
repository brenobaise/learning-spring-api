package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.exceptions.ProductNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Product;
import com.baisebreno.learning_spring_api.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductRegistryService {
    @Autowired
    ProductRepository productRepository;

    public Product findOne(Long restaurantId, Long productId) {
        return productRepository.findById(restaurantId,productId)
                .orElseThrow(() -> new ProductNotFoundException(productId, restaurantId));
    }

    public Product save(Product product){
        return productRepository.save(product);
    }

}
