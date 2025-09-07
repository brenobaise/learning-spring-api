package com.baisebreno.learning_spring_api.core.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {
    private List<String> validContentTypes;

    @Override
    public void initialize(FileContentType constraintAnnotation) {
        this.validContentTypes = Arrays.asList(constraintAnnotation.allowed());
    }


    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value == null || validContentTypes.contains(value.getContentType());
    }
}
