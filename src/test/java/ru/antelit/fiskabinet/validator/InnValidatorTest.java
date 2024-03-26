package ru.antelit.fiskabinet.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.ConstraintValidatorContext;

class InnValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Test
    void testValidIpInn() {
        InnValidator v = new InnValidator();
        boolean result = v.isValid("561407821100", context);
        Assertions.assertTrue(result);
    }

    @Test
    void testValidOrgInn() {

    }

    @Test
    void testInvalidIpInn() {

    }

    @Test
    void testInvalidOrgInn() {

    }
}