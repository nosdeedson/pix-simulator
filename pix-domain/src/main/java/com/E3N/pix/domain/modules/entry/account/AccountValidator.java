package com.E3N.pix.domain.modules.entry.account;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class AccountValidator extends Validator {
    private final static String PROPERTY = "Account.";
    private final Account account;

    protected AccountValidator(Account account) {
        super(account.getNotification());
        this.account = account;
    }

    @Override
    public ValidationHandler validate() {
        if (this.account.getEntryKeys() == null) {
            validationHandler().append("At least one EntryKey should be informed", null, "Account.entryKey");
        }
        if (this.account.getEntryKeys() != null && !this.account.getEntryKeys().isEmpty()) {
            var entryKey = this.account.getEntryKeys().getLast();
            if (entryKey.getNotification() != null && entryKey.getNotification().hasError()) {
                validationHandler().relateNotificationToMe(PROPERTY,
                        entryKey.getNotification().getViolations()
                );
            }
        }
        if (this.account.getBranch().getNotification().hasError()) {
            validationHandler().relateNotificationToMe(
                    PROPERTY,
                    this.account.getBranch().getNotification().getViolations()
            );
        }
        if (this.account.getNumber().getNotification().hasError()) {
            validationHandler().relateNotificationToMe(
                    PROPERTY,
                    this.account.getNumber().getNotification().getViolations()
            );
        }
        if (this.account.getOpeningDate() == null) {
            validationHandler().append("OpeningDate is required.", null, "Account.openingDate");
        }
        if (this.account.getParticipant().getNotification().hasError()) {
            validationHandler().relateNotificationToMe(
                    PROPERTY,
                    this.account.getParticipant().getNotification().getViolations()
            );
        }
        if (this.account.getType() == null) {
            validationHandler().append("Account Type is required.", null, "Account.type");
        }
        return this.account.getNotification();
    }
}
