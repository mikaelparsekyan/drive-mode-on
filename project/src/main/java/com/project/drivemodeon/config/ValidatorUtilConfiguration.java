package com.project.drivemodeon.config;

import com.project.drivemodeon.util.api.ValidatorUtil;
import com.project.drivemodeon.util.impl.ValidatorUtilImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorUtilConfiguration {

    @Bean
    public ValidatorUtil getValidator() {
        return new ValidatorUtilImpl();
    }
}
