package com.E3N.pix.domain.modules.ownership.owner;

import com.E3N.pix.domain.Entity;
import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.dto.UpdateEntryKeyDto;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.validation.Notification;
import com.E3N.pix.domain.valueObject.name.Name;
import com.E3N.pix.domain.valueObject.taxIdNumber.TaxIdNumber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            final String tradeName,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        this.tradeName = Name.getInstance(tradeName, TypePerson.LEGAL_PERSON);
        this(name, taxIdNumber, type, account);
    }

    private Owner(
            final UUID id,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final String name,
            final String tradeName,
            final String taxIdNumber,
            final TypePerson type,
            final List<Account> accounts
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = Name.getInstance(name, type);
        this.taxIdNumber = TaxIdNumber.getInstance(type, taxIdNumber);
        this.type = type;
        this.tradeName = Name.getInstance(tradeName, TypePerson.LEGAL_PERSON);
        this.accounts = accounts;
    }

    public static Owner getInstance(
            final String name,
            final String tradeName,
            final String taxIdNumber,
            final TypePerson type,
            final Account account
    ) {
        if (TypePerson.LEGAL_PERSON.equals(type)) {
            return new Owner(name, tradeName, taxIdNumber, type, account);
        }
        return new Owner(name, taxIdNumber, type, account);
    }

    public static Owner getInstance(
            final UUID id,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt,
            final String name,
            final String tradeName,
            final String taxIdNumber,
            final TypePerson type,
            final List<Account> accounts
    ) {
        return new Owner(
                id,
                createdAt,
                updatedAt,
                deletedAt,
                name,
                tradeName,
                taxIdNumber,
                type,
                accounts
        );
    }

    public boolean canNotHaveMoreKeys() {
        var qtdKeys = 0;
        for (Account acc : this.getAccounts()) {
            qtdKeys += acc.getEntryKeys().size();
        }
        if (this.type.equals(TypePerson.LEGAL_PERSON)) {
            return qtdKeys > 20;
        }
        return qtdKeys > 5;
    }

    public void addNewAccountOrNewKey(final Account newAccount) {
        var newKey = newAccount.getEntryKeys().getFirst();
        Optional<Account> accountAlreadyExist = this.accounts.stream()
                .filter(it ->
                        it.getNumber().getNumber().equals(newAccount.getNumber().getNumber())
                                && it.getBranch().getBranch().equals(newAccount.getBranch().getBranch())
                                && it.getParticipant().getParticipant().equals(newAccount.getParticipant().getParticipant())
                )
                .findFirst();
        if (accountAlreadyExist.isPresent()) {
            accountAlreadyExist.get().addKey(newKey);
        } else {
            this.addAccount(newAccount);
        }
    }

    public void update(UpdateEntryKeyDto dto) {
        this.notification = Notification.create();
        this.notification = (Notification) new OwnerValidator(this).validateUpdate(dto);
        if (!this.notification.hasError()) {
            EntryKey key = null;
            for (Account acc : this.getAccounts()) {
                var optionalEntryKey = acc.getEntryKeys().stream().filter(it -> it.isEqual(dto.key()))
                        .findAny();
                optionalEntryKey.ifPresent(entryKey -> acc.getEntryKeys().remove(entryKey));
                if (optionalEntryKey.isPresent()) {
                    key = optionalEntryKey.get();
                }
                if (acc.getEntryKeys().isEmpty()) {
                    acc.delete();
                }
            }
            var newAccount = Account.getInstance(dto.accountDto().branch(), dto.accountDto().number(),
                    dto.accountDto().participant(), dto.accountDto().type(), dto.accountDto().openingDate(),
                    key);
            this.setName(dto.name(), dto.typePerson());
            if (this.type.equals(TypePerson.LEGAL_PERSON)) this.setTradeName(dto.tradeName(), dto.typePerson());
            this.addAccount(newAccount);
        }
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

    private void addAccount(final Account newAccount) {
        this.accounts.add(newAccount);
        this.validate();
    }

    public Name getName() {
        return name;
    }

    private void setName(final String name, TypePerson type) {
        if (!name.equals(this.name.getName())) {
            this.name = Name.getInstance(name, type);
        }
    }

    public Name getTradeName() {
        return tradeName;
    }

    private void setTradeName(final String tradeName, TypePerson type) {
        if (!tradeName.equals(this.tradeName.getName())) {
            this.tradeName = Name.getInstance(tradeName, type);
        }
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
