package com.baisebreno.learning_spring_api.core.validation;

import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Locale;

public class ValueZeroIncludesDescriptionValidator implements ConstraintValidator<ValueZeroIncludesDescription, Object> {
    private String fieldValue;
    private String descriptionField;
    private String mandatoryDescription;


    /**
     * Initializes the validator in preparation for
     * {@link #isValid(Object, ConstraintValidatorContext)} calls.
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     * <p>
     * The default implementation is a no-op.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    @Override
    public void initialize(ValueZeroIncludesDescription constraintAnnotation) {
        this.fieldValue = constraintAnnotation.fieldValue();
        this.descriptionField = constraintAnnotation.descriptionField();
        this.mandatoryDescription = constraintAnnotation.mandatoryDescription();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param objectToValidate   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
        boolean valid = true;

        try {
            BigDecimal value = (BigDecimal) BeanUtils.getPropertyDescriptor(objectToValidate.getClass(), fieldValue)
                    .getReadMethod().invoke(objectToValidate);

            String description = (String) BeanUtils.getPropertyDescriptor(objectToValidate.getClass(), descriptionField)
                    .getReadMethod().invoke(objectToValidate);

            // if value and description is not null and value is equal to 0
            if(value != null && BigDecimal.ZERO.compareTo(value) == 0 && description != null ){
                // value is true if the description is equal to the incoming descriptionField
                valid = description.toLowerCase(Locale.ROOT).contains(mandatoryDescription.toLowerCase(Locale.ROOT));
            }
            return valid;
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }
}
