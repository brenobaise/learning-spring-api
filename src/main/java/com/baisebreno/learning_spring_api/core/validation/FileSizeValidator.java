package com.baisebreno.learning_spring_api.core.validation;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
    private DataSize maxSize;
    @Override
    public void initialize(FileSize constraintAnnotation) {
        // converts the String representing the size "500KB" to an actual DataSize
        this.maxSize = DataSize.parse(constraintAnnotation.max());
    }

    /**
     * The file is valid for upload if its size in bytes is <= than the maxSize.
     * @param value the current file being validated
     * @param context the String representing the constraint of the validation.
     * @return true if it's valid.
     */
    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value == null || value.getSize() <= this.maxSize.toBytes();
    }
}
