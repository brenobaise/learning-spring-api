package com.baisebreno.learning_spring_api.domain.repository;

import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;

public interface ProductRepositoryQueries {
    ProductPhoto save(ProductPhoto productPhoto);
    void deletePhoto(ProductPhoto productPhoto);
}
