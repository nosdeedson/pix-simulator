package com.E3N.pix.domain.valueObject.taxIdNumber;

import com.E3N.pix.domain.ValueObject;
import com.E3N.pix.domain.modules.entry.owner.TypePerson;
import com.E3N.pix.domain.validation.Notification;

public class TaxIdNumber extends ValueObject {
    private TypePerson typePerson;
    private String taxIdNumber;
    private Notification notification;

    private TaxIdNumber(TypePerson typePerson, String taxIdNumber) {
        this.typePerson = typePerson;
        this.taxIdNumber = taxIdNumber;
        this.validate();
    }

    public static TaxIdNumber getInstance(TypePerson typePerson, String taxIdNumber) {
        return new TaxIdNumber(typePerson, taxIdNumber);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new TaxIdNumberValidator(this).validate();
        if (this.notification.hasError()) {
            this.taxIdNumber = null;
            this.typePerson = null;
        }
    }

    public TypePerson getTypePerson() {
        return typePerson;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public Notification getNotification() {
        return notification;
    }
}
