package com.hyeongarl.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UrlRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testRequest_valid() {
        UrlRequestDto dto = UrlRequestDto.builder()
                .url("https://example.com")
                .urlTitle("exampleTitle")
                .build();

        Set<ConstraintViolation<UrlRequestDto>> violations = validator.validate(dto);
        assertEquals(0, violations.size());
    }

    @Test
    public void testRequest_invalid() {
        UrlRequestDto dto = UrlRequestDto.builder()
                .url("invalidUrl")
                .urlTitle("exampleTitle")
                .build();

        Set<ConstraintViolation<UrlRequestDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}
