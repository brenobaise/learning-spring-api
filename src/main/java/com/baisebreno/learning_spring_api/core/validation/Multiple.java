package com.baisebreno.learning_spring_api.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This BeanValidation annotation is a method level annotation, it's has been implemented for documentation and learning purposes.
 * When used on a property, it expects the field to be a multiple of a given number.
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = {MultipleValidator.class})
public @interface Multiple {

    String message() default "Invalid Multiple";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int number();
}
