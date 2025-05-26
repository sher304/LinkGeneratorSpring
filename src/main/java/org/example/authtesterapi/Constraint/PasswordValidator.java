package org.example.authtesterapi.Constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordProtocol, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null || password.length() < 10) {
            buildViolation(constraintValidatorContext, "Password must be at least 10 characters long");
            return false;
        }

        int lower = 0, upper = 0, digit = 0, special = 0;

        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) lower++;
            else if (Character.isUpperCase(ch)) upper++;
            else if (Character.isDigit(ch)) digit++;
            else special++;
        }

        if (lower < 1) {
            buildViolation(constraintValidatorContext, "Password must contain at least 1 lowercase letter");
            return false;
        }
        if (upper < 2) {
            buildViolation(constraintValidatorContext, "Password must contain at least 2 uppercase letters");
            return false;
        }
        if (digit < 3) {
            buildViolation(constraintValidatorContext, "Password must contain at least 3 digits");
            return false;
        }
        if (special < 4) {
            buildViolation(constraintValidatorContext, "Password must contain at least 4 special characters");
            return false;
        }
        return true;
    }

    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
