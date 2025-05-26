package org.example.authtesterapi.Constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = URLCorrectnessDelegate.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface URLCorrectnessProtocol {

    String message() default "URL is incorrect";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
