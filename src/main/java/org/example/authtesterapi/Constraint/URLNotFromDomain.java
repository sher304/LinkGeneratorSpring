package org.example.authtesterapi.Constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = URLValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface URLNotFromDomain {

    String message() default "URL must be HTTPS";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String protocol() default "https";
}
