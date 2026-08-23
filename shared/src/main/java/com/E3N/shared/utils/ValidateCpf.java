package com.E3N.shared.utils;

import java.util.Arrays;

public final class ValidateCpf {
    private static final int MODULE_11 = 11;

    public static boolean validate(final String cpf){
        if(cpf == null) return false;
        if (!cpf.matches("\\d+") || cpf.length() != 11) return false;
        char[] chars = cpf.toCharArray();
        int[] digits = new int[11];
        for (int i = 0; i < chars.length; i++) {
            digits[i] = Character.getNumericValue(chars[i]);
        }
        return isValid(digits);
    }

    private static boolean isValid(int[] digits){
        int secondDigitVerification = digits[digits.length - 1];
        int firstDigitVerification = digits[digits.length - 2];
        int firstSum = sum(Arrays.copyOf(digits, digits.length - 2), 10, 0);
        if (!validation(firstSum, firstDigitVerification)) return false;
        int secondSum = sum(Arrays.copyOf(digits, digits.length - 1), 11, 0);
        return validation(secondSum, secondDigitVerification);
    }

    private static int sum(int[] digits, int multiplicator, int acumulator){
        if(multiplicator == 2){
            acumulator += (digits[0] * multiplicator);
            return acumulator;
        }
        acumulator += digits[0] * multiplicator;
        --multiplicator;
        digits = Arrays.copyOfRange(digits, 1, digits.length);
        return sum(digits, multiplicator, acumulator);
    }

    private static boolean validation(int sum, int digitToValidate){
        int rest = sum % MODULE_11;
        if( (MODULE_11 - rest) >= 10 && digitToValidate == 0) return  true;
        return (MODULE_11 - rest) == digitToValidate;
    }
}
