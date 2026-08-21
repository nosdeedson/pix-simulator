package com.E3N.shared.utils;

import java.util.regex.Pattern;

public final class ValidatePhone {

    public static boolean isValid(final String phone){
        if (phone == null) return false;
        final String PHONE_REGEX =
                "^\\+55(1[1-9]|2[12478]|3[1-8]|4[1-9]|5[1345]|6[1-9]|7[1345789]|8[1-9]|9[1-9])9\\d{8}$";

        final Pattern pattern = Pattern.compile(PHONE_REGEX);
        return pattern.matcher(phone).matches();
    }
}
