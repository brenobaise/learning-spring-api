package com.baisebreno.learning_spring_api.infrastructure.repository;

import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;
import com.baisebreno.learning_spring_api.domain.repository.ProductRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProductRepositoryImpl implements  ProductRepositoryQueries{
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves a photo to a product, through the ProductRepository
     * @param productPhoto the file/photo
     * @return a ProductPhoto
     */
    @Transactional
    @Override
    public ProductPhoto save(ProductPhoto productPhoto) {
        return entityManager.merge(productPhoto);
    }

    /**
     * Deletes a photo inside the current entity.
     * @param productPhoto the photo to be removed
     */
    @Transactional
    @Override
    public void deletePhoto(ProductPhoto productPhoto) {
        entityManager.remove(productPhoto);
    }
}
