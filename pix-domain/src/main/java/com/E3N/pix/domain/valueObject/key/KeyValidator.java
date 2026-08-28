package com.E3N.pix.domain.valueObject.key;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;
import com.E3N.shared.utils.*;


public class KeyValidator extends Validator {
    private final static String PROPERTY = "EntryKey.key";
    private final Key key;

    public KeyValidator(Key key) {
        super(key.getNotification());
        this.key = key;
    }

    @Override
    public ValidationHandler validate() {
        switch (key.getType()) {
            case CPF -> {
                if (!ValidateCpf.validate(key.getKey())) {
                    createNewError();
                    return validationHandler();
                }
            }
            case EVP -> {
                if (!ValidateUUID.isValid(key.getKey())) {
                    createNewError();
                    return validationHandler();
                }
            }
            case CNPJ -> {
                if (!ValidateCnpj.validate(key.getKey())) {
                    createNewError();
                    return validationHandler();
                }
            }
            case EMAIL -> {
                if (!ValidateEmail.isValid(key.getKey())) {
                    createNewError();
                    return validationHandler();
                }
            }
            case PHONE -> {
                if (!ValidatePhone.isValid(key.getKey())) {
                    createNewError();
                    return validationHandler();
                }
            }
            default -> {
                createNewError();
                return validationHandler();
            }
        }
        return this.key.getNotification();
    }

    public void createNewError() {
        this.validationHandler().append(key.getKey() + " is invalid.", key.getKey(), PROPERTY);
    }
}
