package com.baisebreno.learning_spring_api.api.model.input;

import com.baisebreno.learning_spring_api.core.validation.FileContentType;
import com.baisebreno.learning_spring_api.core.validation.FileSize;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductPhotoInput {

    @NotNull
    @FileContentType(allowed = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @FileSize(max = "20KB")
    private MultipartFile file;

    @NotBlank
    private String description;
}
