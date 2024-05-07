package ru.antelit.fiskabinet.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InnValidator implements ConstraintValidator<InnConstraint, String> {

    private final int[] orgCoefs = {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final int[] ipCoefs1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final int[] ipCoefs2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        int checksum, checksum1, checksum2;
        int[] values = new int[value.length()];
        for (int i=0; i<values.length; i++) {
            values[i] = Character.getNumericValue(value.charAt(i));
        }
        if (values.length == 10) {
            checksum = 0;
            for (int i = 0; i < 9; i++) {
                checksum += values[i] * orgCoefs[i];
            }
            checksum = (checksum % 11) % 10;
            return values[9] == checksum;
        }

        if (value.length() == 12) {
            checksum1 = 0;
            checksum2 = 0;

            for (int i = 0; i < 10; i++) {
                checksum1 += values[i] * ipCoefs1[i];
            }
            checksum1 = (checksum1 % 11) % 10;

            for (int i = 0; i < 11; i++) {
                checksum2 += values[i] * ipCoefs2[i];
            }
            checksum2 = (checksum2 % 11) % 10;

            return checksum1 == values[10] && checksum2 == values[11];
        }
        return false;
    }
}