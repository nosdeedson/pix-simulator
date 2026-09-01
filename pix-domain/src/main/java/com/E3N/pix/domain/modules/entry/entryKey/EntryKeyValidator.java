package com.E3N.pix.domain.modules.entry.entryKey;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;
import com.E3N.shared.utils.ValidateUUID;

public class EntryKeyValidator extends Validator {

    private final EntryKey entryKey;

    public EntryKeyValidator(EntryKey entryKey) {
        super(entryKey.getNotification());
        this.entryKey = entryKey;
    }

    @Override
    public ValidationHandler validate() {
        var key = entryKey.getKey();
        if (key == null) {
            validationHandler().append("Key is required", null, "entryKey");
        }
        if (key != null
                && key.getNotification() != null
                && key.getNotification().hasError()
        ) {
            validationHandler().append(key.getNotification());
        }
        if (entryKey.getReason() == null) {
            validationHandler().append("Reason is required.", null, "entryKey");
        }
        if (entryKey.getRequestId() == null || !ValidateUUID.isValid(entryKey.getRequestId().toString())) {
            validationHandler().append("Request Id is invalid or null.", null, "entryKey");
        }

        return validationHandler();
    }
}
