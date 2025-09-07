package com.baisebreno.learning_spring_api.domain.exceptions;

public class ProductPhotoNotFoundException extends EntityNotFoundException {
    public ProductPhotoNotFoundException(String message) {
        super(message);
    }

    public ProductPhotoNotFoundException(Long restaurantId, Long productId ){
        this(String.format("Product Photo not found for Product" +
                " of id %d for Restaurant id %d.", productId, restaurantId));
    }
}
