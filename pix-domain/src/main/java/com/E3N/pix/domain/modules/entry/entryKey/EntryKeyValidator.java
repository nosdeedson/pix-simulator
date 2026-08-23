package com.E3N.pix.domain.modules.entry.entryKey;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;
import com.E3N.pix.domain.valueObject.key.Key;
import com.E3N.shared.utils.ValidateUUID;

public class EntryKeyValidator extends Validator {

    private final EntryKey entryKey;

    public EntryKeyValidator(EntryKey entryKey) {
        super(entryKey.getNotification());
        this.entryKey = entryKey;
    }

    @Override
    public ValidationHandler validate() {
        if (entryKey.getKeys() != null) {
            for (Key key: entryKey.getKeys()){
                if (key.getNotification() != null
                ){
                    validationHandler().append(key.getNotification());
                }
            }
        }
        var account = entryKey.getAccount();
        if (account != null
                && account.getNotification() != null
                && account.getNotification().hasError()
        ) {
            validationHandler().append(account.getNotification());
        }
        var owner = entryKey.getOwner();
        if (owner != null
                && owner.getNotification() != null
                && owner.getNotification().hasError()
        ) {
            validationHandler().append(owner.getNotification());
        }
        if (entryKey.getReason() == null) {
            validationHandler().append("Reason is required.");
        }
        if (entryKey.getRequestId() == null) {
            validationHandler().append("Request Id is required.");
        }
        if (!ValidateUUID.isValid(entryKey.getRequestId().toString())){
            validationHandler().append("Request Id is invalid.");
        }
        return validationHandler();
    }
}
