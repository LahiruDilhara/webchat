package me.lahirudilhara.webchat.core.util;

import jakarta.validation.*;

import java.util.Set;

public class SchemaValidator {
    private static final Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> String validate(T dto){
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            return violation.getMessage();
        }
        return null;
    }
}
