package com.E3N.shared.utils;

import java.util.UUID;

public final class ValidateUUID {

    public static boolean isValid(final String uuid) {
        try {
            if (uuid == null) return false;
            UUID.fromString(uuid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
