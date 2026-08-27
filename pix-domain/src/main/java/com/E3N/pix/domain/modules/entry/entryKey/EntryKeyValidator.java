package com.E3N.pix.domain.modules.entry.entryKey;

import com.E3N.pix.domain.modules.entry.owner.Owner;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;
import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;
import com.E3N.pix.domain.valueObject.key.Key;
import com.E3N.pix.domain.valueObject.key.TypeKey;
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
            validationHandler().append("Key is required");
        }
        if (key != null
                && key.getNotification() != null
                && key.getNotification().hasError()
        ) {
            validationHandler().append(key.getNotification());
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
        if (entryKey.getRequestId() == null || !ValidateUUID.isValid(entryKey.getRequestId().toString())) {
            validationHandler().append("Request Id is invalid or null.");
        }
        if (key != null
                && owner != null
                && owner.getType() != null
                && key.getType() != null
                && key.getType().equals(TypeKey.CPF)
                && owner.getType().equals(TypePerson.NATURAL_PERSON)
                && !owner.getTaxIdNumber().getTaxIdNumber().equals(key.getKey())
        ) {
            validationHandler().append(this.getMessage(key, owner));
        }
        if (key != null
                && owner != null
                && owner.getType() != null
                && key.getType() != null
                && key.getType().equals(TypeKey.CNPJ)
                && owner.getType().equals(TypePerson.LEGAL_PERSON)
                && !owner.getTaxIdNumber().getTaxIdNumber().equals(key.getKey())
        ) {
            validationHandler().append(this.getMessage(key, owner));
        }
        return validationHandler();
    }

    private String getMessage(final Key key, final Owner owner) {
        var keyValue = key.getKey();
        var taxIdValue = owner.getTaxIdNumber() != null ? owner.getTaxIdNumber().getTaxIdNumber() : null;
        var typeKey = key.getType();
        return String.format(
                "Key %s should be equal to taxIdNumber %s when type key is %s",
                keyValue,
                taxIdValue,
                typeKey
        );
    }
}
