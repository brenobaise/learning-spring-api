package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.assembler.product.ProductPhotoModelAssembler;
import com.baisebreno.learning_spring_api.api.model.ProductPhotoModel;
import com.baisebreno.learning_spring_api.api.model.input.ProductPhotoInput;
import com.baisebreno.learning_spring_api.domain.exceptions.EntityNotFoundException;
import com.baisebreno.learning_spring_api.domain.model.Product;
import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;
import com.baisebreno.learning_spring_api.domain.service.CatalogueService;
import com.baisebreno.learning_spring_api.domain.service.PhotoStorageService;
import com.baisebreno.learning_spring_api.domain.service.ProductRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/products/{productId}/photo")
public class RestaurantProductPhotoController {

    @Autowired
    CatalogueService catalogueService;

    @Autowired
    ProductRegistryService productRegistryService;

    @Autowired
    ProductPhotoModelAssembler productPhotoModelAssembler;

    @Autowired
    PhotoStorageService photoStorageService;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductPhotoModel updatePhoto(@PathVariable Long restaurantId,
                                         @PathVariable Long productId,
                                         @Valid ProductPhotoInput productPhotoInput) throws IOException {

        // identifies if a product exists
        Product product = productRegistryService.findOne(restaurantId, productId);

        MultipartFile file = productPhotoInput.getFile();

        // maps a new photo with the incoming data
        ProductPhoto photo = new ProductPhoto();
        photo.setProduct(product);
        photo.setDescription(productPhotoInput.getDescription());
        photo.setContentType(file.getContentType());
        photo.setSize(file.getSize());
        photo.setFileName(file.getOriginalFilename());

        // saves it
        ProductPhoto savedPhoto = catalogueService.save(photo, file.getInputStream());
        return productPhotoModelAssembler.toModel(savedPhoto);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductPhotoModel getPhoto(@PathVariable Long restaurantId,
                                      @PathVariable Long productId) {
        ProductPhoto productPhoto = catalogueService.findOne(restaurantId, productId);

        return productPhotoModelAssembler.toModel(productPhoto);
    }

    @GetMapping()
    public ResponseEntity<InputStreamResource> servePhoto(
            @PathVariable Long restaurantId,
            @PathVariable Long productId,
            @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {

        try {
            ProductPhoto productPhoto = catalogueService.findOne(restaurantId, productId);

            MediaType mediaTypePhoto = MediaType.parseMediaType(productPhoto.getContentType());
            List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);


            verifyMediaTypeCompatibility(mediaTypePhoto, acceptedMediaTypes);

            InputStream inputStream = photoStorageService.getFile(productPhoto.getFileName());


            return ResponseEntity.ok()
                    .contentType(mediaTypePhoto)
                    .body(new InputStreamResource(inputStream));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void verifyMediaTypeCompatibility(MediaType mediaTypePhoto, List<MediaType> acceptedMediaTypes)
            throws HttpMediaTypeNotAcceptableException {

        boolean compatible = acceptedMediaTypes.stream()
                .anyMatch(acceptedMediaType -> acceptedMediaType.isCompatibleWith(mediaTypePhoto));

        if(!compatible){
            throw new HttpMediaTypeNotAcceptableException(acceptedMediaTypes);
        }
    }
}
