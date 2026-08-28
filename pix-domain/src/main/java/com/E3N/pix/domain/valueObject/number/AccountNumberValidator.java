package com.E3N.pix.domain.valueObject.number;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class AccountNumberValidator extends Validator {
    private final static String PROPERTY = "Account.number";
    private final AccountNumber accountNumber;

    public AccountNumberValidator(AccountNumber accountNumber) {
        super(accountNumber.getNotification());
        this.accountNumber = accountNumber;
    }

    @Override
    public ValidationHandler validate() {
        if (this.accountNumber.getNumber() == null) {
            validationHandler().append("Account Number is required.", accountNumber.getNumber(), PROPERTY);
        } else if (!this.accountNumber.getNumber().matches("^[0-9]{3,20}[Xx]?$")) {
            validationHandler().append("Account Number is invalid.", accountNumber.getNumber(), PROPERTY);
        }
        return validationHandler();
    }
}
