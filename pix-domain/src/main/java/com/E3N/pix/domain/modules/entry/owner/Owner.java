package com.E3N.pix.domain.modules.entry.owner;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.modules.entry.account.Account;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.name.Name;
import com.E3N.pix.domain.valueObject.taxIdNumber.TaxIdNumber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Owner extends Entity {

    private Name name;
    private Name tradeName;
    private Instant openClaimCreationDate;
    private TaxIdNumber taxIdNumber; // cpf/cnpj
    private TypePerson type;
    private List<Account> accounts;

    private Notification notification;

    private Owner(
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        super();
        if (this.accounts == null)
            this.accounts = new ArrayList<>(5);
        this.accounts.add(account);
        this.name = Name.getInstance(name, type);
        this.taxIdNumber = TaxIdNumber.getInstance(type, taxIdNumber);
        this.type = type;
        validate();
    }

    private Owner(
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final String tradeName,
            final Account account
    ) {
        this.tradeName = Name.getInstance(tradeName, TypePerson.LEGAL_PERSON);
        this(name, taxIdNumber, type, account);
    }

    public static Owner getInstanceNaturalPerson(
            final String name,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        return new Owner(name, taxIdNumber, type, account);
    }

    public static Owner getInstanceLegalPerson(
            final String name,
            final String taxIdNumber,
            final String tradeName,
            final TypePerson type,
            final Account account
    ) {
        return new Owner(name, taxIdNumber, type, tradeName, account);
    }

    @Override
    protected void validate() {
        this.notification = Notification.create();
        this.notification = (Notification) new OwnerValidator(this).validate();
        if (this.notification.hasError()) {
            this.name = null;
            this.taxIdNumber = null;
            this.type = null;
        }
    }

    public Name getName() {
        return name;
    }

    public Name getTradeName() {
        return tradeName;
    }

    public Instant getOpenClaimCreationDate() {
        return openClaimCreationDate;
    }

    public TaxIdNumber getTaxIdNumber() {
        return taxIdNumber;
    }

    public TypePerson getType() {
        return type;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Notification getNotification() {
        return notification;
    }
}
