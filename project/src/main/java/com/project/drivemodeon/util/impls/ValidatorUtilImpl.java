package com.project.drivemodeon.util.impls;

import com.project.drivemodeon.util.api.ValidatorUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ValidatorUtilImpl implements ValidatorUtil {
    private final Validator validator;

    public ValidatorUtilImpl() {
        this.validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Override
    public <E> boolean isValid(E entity) {
        return validator.validate(entity).isEmpty();
    }

    @Override
    public <E> Map<String, Object> violations(E entity) {
        Map<String, Object> violations = new LinkedHashMap<>();
        for (ConstraintViolation<E> violation : validator.validate(entity)) {
            violations.put(violation.getPropertyPath().toString(),
                    violation.getMessage());
        }
        return violations;
    }
}
