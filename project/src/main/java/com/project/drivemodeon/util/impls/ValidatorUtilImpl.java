package com.project.drivemodeon.util.impls;

import com.project.drivemodeon.util.api.ValidatorUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

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
    public <E> Set<ConstraintViolation<E>> violations(E entity) {
        return validator.validate(entity);
    }
}
