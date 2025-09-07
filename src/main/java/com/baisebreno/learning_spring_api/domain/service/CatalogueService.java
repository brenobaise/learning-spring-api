package com.baisebreno.learning_spring_api.domain.service;

import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;
import com.baisebreno.learning_spring_api.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CatalogueService {

    @Autowired
    ProductRepository productRepository;

    @Transactional
    public ProductPhoto save(ProductPhoto photo){

        Optional<ProductPhoto> foundPhoto = productRepository
                .findPhotoById(photo.getRestaurantId(), photo.getProduct().getId());


        // if there is a current photo, delete it
        foundPhoto.ifPresent(productPhoto -> productRepository.deletePhoto(productPhoto));


        return productRepository.save(photo);
    }


}
