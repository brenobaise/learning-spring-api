package com.baisebreno.learning_spring_api.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The purpose of this class is for learning only, it shows how to do conditional validation through annotation.
 * As example, the request is only 200, when the descriptionField is equal to the mandatoryField and the fieldValue is equal to hardcoded 0.
 *
 * In other words:
 * @ValueZeroIncludesDescription(
 *         fieldValue = "deliveryRate",   -> this has to be 0 for it to be a free delivery
 *         descriptionField = "name",     -> the incoming descriptionField has to be equal to
 *         mandatoryDescription = "Free Delivery") -> the mandatoryDescription
 *
 *         If there is Free Delivery inside name AND deliveryRate is 0, then "200" bean is valid.
 */
@Target({TYPE}) // enforces to class/interface only
@Retention(RUNTIME)
@Constraint(validatedBy = {ValueZeroIncludesDescriptionValidator.class})
public @interface ValueZeroIncludesDescription {

    String message() default "mandatory description invalid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

     String fieldValue();
     String descriptionField();
     String mandatoryDescription();


}
