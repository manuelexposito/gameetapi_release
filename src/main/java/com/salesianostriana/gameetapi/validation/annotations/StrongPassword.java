package com.salesianostriana.gameetapi.validation.annotations;



import com.salesianostriana.gameetapi.validation.validators.StrongPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface StrongPassword {


    String message() default "La contraseña no cumple con los requisitos de validación especificados.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Indica la longitud mínima
     * @return
     */
    int min() default 2;

    /**
     * Indica la longitud máxima
     * @return
     */
    int max() default 8;

    /**
     * Indica si es obligatorio o no que tenga mayúsculas
     * @return
     */
    boolean hasUpper() default false;

    /**
     * Indica si es obligatorio o no que tenga minúsculas
     * @return
     */
    boolean hasLower() default false;

    /**
     * Indica si es obligatorio o no que tenga números
     * @return
     */
    boolean hasNumber() default false;

    /**
     * Indica si es obligatorio o no que tenga letras
     * @return
     */
    boolean hasAlpha() default false;

    /**
     * Indica si es obligatorio o no que tenga cualquier carácter especial
     * @return
     */
    boolean hasOther() default false;

}