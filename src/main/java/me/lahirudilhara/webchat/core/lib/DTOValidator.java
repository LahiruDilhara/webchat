package me.lahirudilhara.webchat.core.lib;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class DTOValidator {
    private final Validator validator;

    public DTOValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public <T> void validate(T dto){
        Set<ConstraintViolation<T>> violations = this.validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("Validation failed for: ");
            for (ConstraintViolation<T> violation : violations) {
                stringBuilder.append(violation.getPropertyPath()).append(" ").append(violation.getMessage()).append("; ");
            }
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
