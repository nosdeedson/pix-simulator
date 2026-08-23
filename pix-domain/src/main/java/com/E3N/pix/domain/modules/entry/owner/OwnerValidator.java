package com.E3N.pix.domain.modules.entry.owner;

import com.E3N.pix.domain.validation.ValidationHandler;
import com.E3N.pix.domain.validation.Validator;

public class OwnerValidator extends Validator {

    private final Owner owner;

    public OwnerValidator(Owner owner) {
        super(owner.getNotification());
        this.owner = owner;
    }

    @Override
    public ValidationHandler validate() {
        if (owner.getKeyOwnerShipDate() == null ){
            validationHandler().append("KeyOwnershipDate is required.");
        }
        if (owner.getType() == null){
            validationHandler().append("Person type is required.");
        }
        if (owner.getName().getNotification() != null && owner.getName().getNotification().hasError()){
            validationHandler().append(owner.getName().getNotification());
        }
        if (owner.getType() != null && owner.getType().equals(TypePerson.LEGAL_PERSON) && owner.getTradeName() == null){
            validationHandler().append("TradeName is required for Legal person.");
        }
        if (owner.getType() != null && owner.getType().equals(TypePerson.LEGAL_PERSON)
                && owner.getTradeName() != null
                && owner.getTradeName().getNotification() != null
                && owner.getTradeName().getNotification().hasError()
        ){
            validationHandler().append(owner.getTradeName().getNotification());
        }
        if (owner.getTaxIdNumber().getNotification() != null
                && owner.getTaxIdNumber().getNotification().hasError()){
            validationHandler().append(owner.getTaxIdNumber().getNotification());
        }
        return this.owner.getNotification();
    }
}
