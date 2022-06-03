package com.salesianostriana.gameetapi.validation.validators;

import com.salesianostriana.gameetapi.validation.annotations.MapsUbication;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MapsUbicationValidator implements ConstraintValidator<MapsUbication, String> {

    @Override
    public void initialize(MapsUbication constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        return s.matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");

    }
}