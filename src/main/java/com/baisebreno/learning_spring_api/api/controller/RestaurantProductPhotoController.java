package com.baisebreno.learning_spring_api.api.controller;

import com.baisebreno.learning_spring_api.api.model.input.ProductPhotoInput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping("/restaurants/{restaurantId}/products/{productId}/photo")
public class RestaurantProductPhotoController {

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updatePhoto(@PathVariable Long restaurantId,
                            @PathVariable Long productId,
                            @Valid ProductPhotoInput productPhotoInput){

        var fileName = UUID.randomUUID() + "_" + productPhotoInput.getFile().getOriginalFilename();

        var filePhoto = Path.of("C:\\Users\\joaob\\Desktop\\catalogo", fileName);

        System.out.println(filePhoto);
        System.out.println(productPhotoInput.getFile().getContentType());
        System.out.println(productPhotoInput.getDescription());
        try{

            productPhotoInput.getFile().transferTo(filePhoto);

        }catch (IllegalStateException e){
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
