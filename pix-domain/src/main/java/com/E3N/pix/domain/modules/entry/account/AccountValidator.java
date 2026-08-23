package com.E3N.pix.domain.modules.entry.account;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class AccountValidator extends Validator {

    private final Account account;

    protected AccountValidator(Account account) {
        super(account.getNotification());
        this.account = account;
    }

    @Override
    public ValidationHandler validate() {
        if (this.account.getBranch().getNotification() != null && this.account.getBranch().getNotification().hasError()){
            validationHandler().append(this.account.getBranch().getNotification());
        }
        if (this.account.getNumber().getNotification() != null && this.account.getNumber().getNotification().hasError()){
            validationHandler().append(this.account.getNumber().getNotification());
        }
        if (this.account.getOpeningDate() == null) {
            validationHandler().append("OpeningDate is required.");
        }
        if (this.account.getParticipant().getNotification() != null && this.account.getParticipant().getNotification().hasError()){
            validationHandler().append(this.account.getParticipant().getNotification());
        }
        if(this.account.getType() == null){
            validationHandler().append("Account Type is required.");
        }
        return this.account.getNotification();
    }
}
