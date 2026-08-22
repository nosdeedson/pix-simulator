package com.E3N.pix.domain.valueObject.number;

import com.E3N.pix.domain.ValueObject;
import com.E3N.pix.domain.validation.Notification;

public class AccountNumber extends ValueObject {
    /* Account Number*/
    private String number;
    private Notification notification;

    private AccountNumber(String number) {
        this.number = number;
        validate();
    }

    public static AccountNumber getInstance(final String number){
        return new AccountNumber(number);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new AccountNumberValidator(this).validate();
        if (this.notification.hasError()){
            this.number = null;
        } else {
            this.notification = null;
        }
    }

    public String getNumber() {
        return number;
    }

    public Notification getNotification() {
        return notification;
    }
}
