package com.tafh.authjwt.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class ValidationService {

    @Autowired
    private Validator validator;

    public void validate(Object request) {
        log.error("validate called {}",request);
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            log.error("validate called {}",constraintViolations);
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
