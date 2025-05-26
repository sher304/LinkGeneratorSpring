package org.example.authtesterapi.Constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class URLValidator implements ConstraintValidator<URLNotFromDomain, String> {

    private String defaultProtocol;

    @Override
    public void initialize(URLNotFromDomain constraintAnnotation) { defaultProtocol = constraintAnnotation.protocol(); }

    @Override
    public boolean isValid(String protocol, ConstraintValidatorContext constraintValidatorContext) {
        return protocol.strip().toLowerCase().startsWith(defaultProtocol);
    }
}
