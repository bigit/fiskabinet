package ru.antelit.fiskabinet.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InnValidator implements ConstraintValidator<InnConstraint, String> {

    private final int[] orgCoefs =       {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final int[] ipCoefs1 =    {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private final int[] ipCoefs2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.length() == 10) {
            int checksum = 0;
            for (int i = 0; i < 9; i++) {
                checksum += getNum(value, i) * orgCoefs[i];
            }
            checksum %= 11;
            checksum %= 10;
            return getNum(value, 9) == checksum;
        }

        if (value.length() == 12) {
            int checksum1 = 0;
            int checksum2 = 0;

            for (int i = 0; i < 10; i++) {
                checksum1 += getNum(value, i) * ipCoefs1[i];
            }
            checksum1 %= 11;

            for(int i=0; i < 11; i++) {
                checksum2 += getNum(value, i) * ipCoefs2[i];
            }
            checksum2 %= 11;

            return checksum1 == getNum(value, 10) && checksum2 == getNum(value, 11);
        }
        return false;
    }

    private int getNum(String value, int index) {
        return Character.getNumericValue(value.charAt(index));
    }
}