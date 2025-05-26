package org.example.authtesterapi.Constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLCorrectnessDelegate implements ConstraintValidator<URLCorrectnessProtocol, String> {

    @Override
    public boolean isValid(String targetURL, ConstraintValidatorContext constraintValidatorContext) {
        if (targetURL == null || targetURL.isEmpty()
                || targetURL.endsWith(".")
                || targetURL.endsWith(",")
                || targetURL.endsWith("!")
                || targetURL.endsWith("?")
                ||!targetURL.contains(".")) {
            return false;
        }
        try {
            URL url = new URL(targetURL);
            System.out.println("THE RATGET URL: " + targetURL);
            url.toURI();
            return true;
        } catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
