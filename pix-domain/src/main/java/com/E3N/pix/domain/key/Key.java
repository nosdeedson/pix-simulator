package com.E3N.pix.domain.key;

import com.E3N.shared.utils.ValidateCnpj;
import com.E3N.shared.utils.ValidateCpf;

import java.util.regex.Pattern;

@SuppressWarnings("all")
public class Key {
    private final String key;
    private final TypeKey type;

    private Key(String key, TypeKey type) {
        this.key = key;
        this.type = type;
    }

    public static Key getInstance(final String key, final TypeKey type) {
        return switch (type) {
            case CPF -> validateCpf(key);
            case EVP -> validateEVP(key);
            case CNPJ -> validateCnpj(key);
            case EMAIL -> validateEmail(key);
            case PHONE -> validatePhone(key);
            default -> null;
        };
    }

    public static Key validateCpf(final String cpf) {
        if (cpf == null) return null;
        var isValid = ValidateCpf.validate(cpf);
        Key k = null;
        if (isValid) k = new Key(cpf, TypeKey.CPF);
        return k;
    }

    public static Key validateEVP(final String EVP) {
        if (EVP == null) return null;
        final String UUID_REGEX =
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        final Pattern pattern = Pattern.compile(UUID_REGEX);
        Key k = null;
        var isValid = pattern.matcher(EVP).matches();
        if (isValid) k = new Key(EVP, TypeKey.EVP);
        return k;
    }

    public static Key validateCnpj(final String cnpj) {
        if (cnpj == null) return null;
        var isValid = ValidateCnpj.validate(cnpj);
        Key k = null;
        if (isValid) k = new Key(cnpj, TypeKey.CNPJ);
        return k;
    }

    public static Key validateEmail(final String email) {
        if (email == null) return null;
        final String EMAIL_REGEX =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Key k = null;
        var isValid = pattern.matcher(email).matches();
        if (isValid) k = new Key(email, TypeKey.EMAIL);
        return k;
    }

    public static Key validatePhone(final String phone) {
        if (phone == null) return null;
        final String PHONE_REGEX =
                "^\\+55(1[1-9]|2[12478]|3[1-8]|4[1-9]|5[1345]|6[1-9]|7[1345789]|8[1-9]|9[1-9])9\\d{8}$";

        final Pattern pattern = Pattern.compile(PHONE_REGEX);
        Key k = null;
        var isValid = pattern.matcher(phone).matches();
        if (isValid) k = new Key(phone, TypeKey.PHONE);
        return k;
    }

    public TypeKey getType() {
        return type;
    }
}
