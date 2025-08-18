package com.baisebreno.learning_spring_api.core.validation;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.PositiveOrZero;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@PositiveOrZero
public @interface DeliveryFee {

    // overrides the default message from PositiveOrZero class to the message() of this annotation instead.
    @OverridesAttribute(constraint = PositiveOrZero.class, name = "message")
    String message() default "{DeliveryFee.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
