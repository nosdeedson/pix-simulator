package com.E3N.shared.utils;

import java.util.regex.Pattern;

public final class ValidateKeyEVP {

    public static boolean isValid(final String EVP){
        if (EVP == null) return false;
        final String UUID_REGEX =
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        final Pattern pattern = Pattern.compile(UUID_REGEX);
        return pattern.matcher(EVP).matches();
    }
}
