package com.E3N.shared.utils;

import java.util.Arrays;

@SuppressWarnings("all")
public final class ValidateCnpj {

    private static final int MINUS_48 = 48;
    private static final int MODULE_11 = 11;

    @SuppressWarnings("all")
    public static boolean validate(final String cnpj) {
        int[] digits = conversionFromCharToInt(cnpj);
        return isValid(digits);
    }

    private static boolean isValid(int[] digits) {
        int secondDigitVerification = digits[digits.length - 1];
        int firstDigitVerification = digits[digits.length - 2];
        int firstSum = sum(Arrays.copyOfRange(digits,  0, (digits.length -2)), 2, 0);
        if (!validation(firstSum, firstDigitVerification)) return false;
        int secondSum = sum(Arrays.copyOfRange(digits, 0, (digits.length -1)), 2, 0);
        return validation(secondSum, secondDigitVerification);
    }

    private static int sum(int[] digits, int multiplicator, int acumulator) {
        if (digits.length == 1){
            acumulator += digits[0] * multiplicator;
            return acumulator;
        }
        acumulator += digits[digits.length - 1] * multiplicator;
        if (multiplicator == 9) multiplicator = 2;
        multiplicator++;
        digits = Arrays.copyOfRange(digits, 0, (digits.length - 1));
        return sum(digits, multiplicator, acumulator);
    }

    private static boolean validation(int sum, int digitToValidate) {
        int rest = sum % MODULE_11;
        if ( (MODULE_11 - rest) >= 10 && digitToValidate == 0) return true;
        return (MODULE_11 - rest) == digitToValidate;
    }

    private static int[] conversionFromCharToInt(final String cnpj) {
        int[] digits = new int[cnpj.length()];
        char[] chars = cnpj.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])){
                digits[i] = Character.getNumericValue(chars[i]);
            } else {
                digits[i] = (int) chars[i] - MINUS_48;
            }
        }
        return digits;
    }
}
