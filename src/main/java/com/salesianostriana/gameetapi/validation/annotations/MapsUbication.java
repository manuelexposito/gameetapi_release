package com.salesianostriana.gameetapi.validation.annotations;

import com.salesianostriana.gameetapi.validation.validators.MapsUbicationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MapsUbicationValidator.class)
public @interface MapsUbication {

    String message() default "Coordinadas incorrectas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
