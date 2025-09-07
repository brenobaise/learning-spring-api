package com.baisebreno.learning_spring_api.domain.service;
import static com.baisebreno.learning_spring_api.domain.service.PhotoStorageService.NewPhoto;
import com.baisebreno.learning_spring_api.domain.model.ProductPhoto;
import com.baisebreno.learning_spring_api.domain.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

@Service
public class CatalogueService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private PhotoStorageService photoStorageService;

    /**
     * Saves a new product photo, replacing any existing one for the same product. <br>
     * <p>
     * The method performs the following steps:
     * <ul>
     *   <li>Checks if a photo already exists for the given product and deletes it if found.</li>
     *   <li>Saves the new {@link ProductPhoto} entity to the database.</li>
     *   <li>Stores the associated file data in the {@link PhotoStorageService}.</li>
     * </ul>
     *
     * @param photo    the {@link ProductPhoto} entity to be saved (must reference an existing product)
     * @param fileData the binary content of the photo as an {@link InputStream}
     * @return the saved {@link ProductPhoto} entity
     */
    @Transactional
    public ProductPhoto save(ProductPhoto photo, InputStream fileData){
        Long restaurantId = photo.getRestaurantId();
        Long productId = photo.getProduct().getId();

        // generates a new file name in case there are multiple or different photos with the same name.
        String newFileName = photoStorageService.generateFileName(photo.getFileName());
        String existingFileName = null;

        // validates if there is a product at the given restaurant
        Optional<ProductPhoto> existingPhoto = productRepository
                .findPhotoById(restaurantId, productId);

        // if there is a current photo, delete it
        if(existingPhoto.isPresent()){
            existingFileName = existingPhoto.get().getFileName();
            productRepository.deletePhoto(existingPhoto.get());
        }

        // sets the new file name before saving onto the database
        photo.setFileName(newFileName);
        photo =  productRepository.save(photo);
        productRepository.flush();

        // creates a child NewPhoto class from PhotoStorageService,
        // to be saved in local storage
        NewPhoto newPhoto = NewPhoto.builder()
                .fileName(newFileName)
                .inputStream(fileData)
                .build();


        // Deletes previous file if it exists, saves the new photo
        photoStorageService.replace(existingFileName, newPhoto);

        return photo;
    }


}
