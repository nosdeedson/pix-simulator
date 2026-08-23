package com.E3N.pix.domain.modules.entry.owner;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.name.Name;
import com.E3N.pix.domain.valueObject.taxIdNumber.TaxIdNumber;
import com.E3N.shared.utils.DateHelper;

import java.time.Instant;

public class Owner extends Entity {
    private Instant keyOwnerShipDate;
    private Name name;
    private Name tradeName;
    private Instant openClaimCreateionDate;
    private TaxIdNumber taxIdNumber; // cpf/cnpj
    private TypePerson type;

    private Notification notification;

    private Owner(
            final String keyOwnerShipDate,
            final String name,
            final String taxIdNumber,
            final TypePerson type
    ) {
        super();
        this.keyOwnerShipDate = DateHelper.getDateFrom(keyOwnerShipDate, "dd/MM/yyyy");
        this.name = Name.getInstance(name, type);
        this.taxIdNumber = TaxIdNumber.getInstance(type, taxIdNumber);
        this.type = type;
        validate();
    }

    private Owner(
            final String keyOwnerShipDate,
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final String tradeName
    ) {
       this.tradeName = Name.getInstance(tradeName, TypePerson.LEGAL_PERSON);
       this(keyOwnerShipDate, name, taxIdNumber, type);
    }

    public static Owner getInstanceNaturalPerson(
            final String keyOwnerShipDate,
            final String name,
            final String taxIdNumber,
            final TypePerson type
    ) {
        return new Owner(keyOwnerShipDate, name, taxIdNumber, type);
    }

    public static Owner getInstanceLegalPerson(
            final String keyOwnerShipDate,
            final String name,
            final String taxIdNumber,
            final String tradeName,
            final TypePerson type
    ) {
        return new Owner(keyOwnerShipDate, name, taxIdNumber, type, tradeName);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new OwnerValidator(this).validate();
        if (this.notification.hasError()) {
            this.keyOwnerShipDate = null;
            this.name = null;
            this.taxIdNumber = null;
            this.type = null;
        } else {
            this.notification = null;
        }
    }

    public Instant getOpenClaimCreateionDate() {
        return openClaimCreateionDate;
    }

    public void setOpenClaimCreateionDate(Instant openClaimCreateionDate) {
        this.openClaimCreateionDate = openClaimCreateionDate;
    }

    public Instant getKeyOwnerShipDate() {
        return keyOwnerShipDate;
    }

    public Name getName() {
        return name;
    }

    public TaxIdNumber getTaxIdNumber() {
        return taxIdNumber;
    }

    public TypePerson getType() {
        return type;
    }

    public Name getTradeName() {
        return tradeName;
    }

    public Notification getNotification() {
        return notification;
    }
}
