package com.E3N.pix.domain;

import com.E3N.shared.utils.ValidateCnpj;
import com.E3N.shared.utils.ValidateCpf;

import java.util.UUID;

public class Key {
    private final String key;
    private final TypeKey type;

    private Key(String key, TypeKey type) {
        this.key = key;
        this.type = type;
    }

    public static Key getInstance(final String key, final TypeKey type){
        return switch (type) {
            case CPF -> validateCpf(key);
            case EVP -> validateEVP(key);
            case CNPJ -> new Key(key, TypeKey.CNPJ);
            case EMAIL -> new Key(key, TypeKey.EMAIL);
            case PHONE -> new Key(key, TypeKey.PHONE);
            default -> null;
        };
    }

    public static Key validateCpf(final String key){
        var isValid = ValidateCpf.validate(key);
        Key k = null;
        if (isValid) k = new Key(key, TypeKey.CPF);
        return k;
    }

    public static Key validateEVP(final String key){
        try {
            var id = UUID.fromString(key);
            return new Key(key, TypeKey.EVP);
        } catch (IllegalArgumentException e){
            return null;
        }
    }

    public static Key validateCnpj(final String key){
        var isValid = ValidateCnpj.validate(key);
        Key k = null;
        if (isValid) k = new Key(key, TypeKey.CNPJ);
        return k;
    }

}
