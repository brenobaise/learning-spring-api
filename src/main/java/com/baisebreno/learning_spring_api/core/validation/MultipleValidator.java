package com.baisebreno.learning_spring_api.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class MultipleValidator implements ConstraintValidator<Multiple,Number> {

    private int numberMultiple;
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
    public void initialize(Multiple constraintAnnotation) {
        this.numberMultiple= constraintAnnotation.number();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        boolean valid = true;
        if(value != null){
            var decimalValue = BigDecimal.valueOf(value.doubleValue());
            var multipleDecimal = BigDecimal.valueOf(this.numberMultiple);
            var remaining = decimalValue.remainder(multipleDecimal);

            valid = BigDecimal.ZERO.compareTo(remaining) == 0;
        }
        return valid;
    }
}
