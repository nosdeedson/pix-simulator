package com.E3N.shared.utils;

import java.util.regex.Pattern;

public final class ValidateEmail {

    public static boolean isValid(final String email){
        if(email == null) return false;
        final String EMAIL_REGEX =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }
}
