package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.product.ProductModelAssembler;
import com.baisebreno.learning_spring_api.api.assembler.product.ProductPhotoModelAssembler;
import com.baisebreno.learning_spring_api.api.model.ProductPhotoModel;
import com.baisebreno.learning_spring_api.api.model.input.ProductPhotoInput;
import com.baisebreno.learning_spring_api.domain.model.Product;
import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;
import com.baisebreno.learning_spring_api.domain.service.CatalogueService;
import com.baisebreno.learning_spring_api.domain.service.ProductRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/restaurants/{restaurantId}/products/{productId}/photo")
public class RestaurantProductPhotoController {

    @Autowired
    CatalogueService catalogueService;

    @Autowired
    ProductRegistryService productRegistryService;

    @Autowired
    ProductPhotoModelAssembler productPhotoModelAssembler;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductPhotoModel updatePhoto(@PathVariable Long restaurantId,
                                         @PathVariable Long productId,
                                         @Valid ProductPhotoInput productPhotoInput) throws IOException {

        Product product = productRegistryService.findOne(restaurantId,productId);

        MultipartFile file = productPhotoInput.getFile();

        ProductPhoto photo = new ProductPhoto();
        photo.setProduct(product);
        photo.setDescription(productPhotoInput.getDescription());
        photo.setContentType(file.getContentType());
        photo.setSize(file.getSize());
        photo.setFileName(file.getOriginalFilename());

        ProductPhoto savedPhoto = catalogueService.save(photo, file.getInputStream());
        return productPhotoModelAssembler.toModel(savedPhoto);
    }
}
