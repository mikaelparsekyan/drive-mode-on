package com.project.drivemodeon.util.api;

import java.util.Map;

public interface ValidatorUtil {
    <E> boolean isValid(E entity);

    <E> Map<String, Object> violations(E entity);
}
