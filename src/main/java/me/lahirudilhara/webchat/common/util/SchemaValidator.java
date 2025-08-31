package me.lahirudilhara.webchat.common.util;

import jakarta.validation.*;

import java.util.Set;

public class SchemaValidator {
    private static final Validator validator =  Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> void validate(T dto){
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw new ValidationException(violation.getMessage());
        }
    }
}
