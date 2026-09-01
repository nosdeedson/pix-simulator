package com.E3N.pix.domain.modules.entry.owner;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class OwnerValidator extends Validator {
    private final String PROPERTY = "OWNER.";
    private final Owner owner;

    public OwnerValidator(Owner owner) {
        super(owner.getNotification());
        this.owner = owner;
    }

    @Override
    public ValidationHandler validate() {
        if (owner.getType() == null) {
            validationHandler().append("Person type is required.", null, PROPERTY);
        }
        if (owner.getName().getNotification() != null && owner.getName().getNotification().hasError()) {
            validationHandler().append(owner.getName().getNotification());
        }
        if (owner.getType() != null && owner.getType().equals(TypePerson.LEGAL_PERSON) && owner.getTradeName() == null) {
            validationHandler().append("TradeName is required for Legal person.", owner.getType().name(), PROPERTY);
        }
        if (owner.getType() != null && owner.getType().equals(TypePerson.LEGAL_PERSON)
                && owner.getTradeName() != null
                && owner.getTradeName().getNotification() != null
                && owner.getTradeName().getNotification().hasError()
        ) {
            validationHandler().relateNotificationToMe(PROPERTY, owner.getTradeName().getNotification().getViolations());
        }
        if (owner.getTaxIdNumber().getNotification() != null
                && owner.getTaxIdNumber().getNotification().hasError()
        ) {
            validationHandler().relateNotificationToMe(PROPERTY, owner.getTaxIdNumber().getNotification().getViolations());
        }
        if (owner.getAccounts() == null) {
            validationHandler().append("At least one account must be informed", null, "Owner.account");
        }
        if (owner.getAccounts() != null
                && owner.getAccounts().getLast().getNotification().hasError()
        ) {
            validationHandler().relateNotificationToMe(PROPERTY, owner.getAccounts().getLast().getNotification().getViolations());
        }
        return this.owner.getNotification();
    }
}
