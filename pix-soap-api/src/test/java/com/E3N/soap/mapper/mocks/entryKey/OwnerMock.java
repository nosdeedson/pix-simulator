package com.E3N.soap.mapper.mocks.entryKey;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.ownership.dto.OwnerDto;
import com.E3N.test.Owner.*;

import java.util.UUID;

public abstract class OwnerMock {

    private static EntryKey getEntryKey(Reason reason, TypeKey typeKey) {
        EntryKey key = null;
        switch (typeKey) {
            case EVP ->
                    key = EntryKey.getInstance(UUID.randomUUID().toString(), typeKey, reason, UUID.randomUUID().toString());
            case PHONE ->
                    key = EntryKey.getInstance(RandomKeysMock.randomPhone(), typeKey, reason, UUID.randomUUID().toString());
            case CNPJ ->
                    key = EntryKey.getInstance(RandomCNPJMock.getRandomCNPJ(), typeKey, reason, UUID.randomUUID().toString());
            case CPF ->
                    key = EntryKey.getInstance(RandomCpfMock.getRandomCFP(), typeKey, reason, UUID.randomUUID().toString());
            case EMAIL ->
                    key = EntryKey.getInstance(RandomKeysMock.randomEmails(), typeKey, reason, UUID.randomUUID().toString());
        }
        return key;
    }

    public static Owner createOwner(TypePerson typePerson, Reason reason, TypeKey typeKey) {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), key);
        if (typePerson.equals(TypePerson.LEGAL_PERSON)) {
            return Owner.getInstance(RandomValidName.randomValidCompanyName(), RandomValidName.randomValidCompanyName(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON, account);
        }
        return Owner.getInstance(RandomValidName.randomValidName(), null, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
    }

    public static Owner createOwner(TypePerson typePerson) {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), key);
        if (typePerson.equals(TypePerson.LEGAL_PERSON)) {
            return Owner.getInstance(RandomValidName.randomValidCompanyName(), RandomValidName.randomValidCompanyName(), RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON, account);
        }
        return Owner.getInstance(RandomValidName.randomValidName(), null, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
    }

    public static Owner createOwner(OwnerDto dto) {
        return Owner.getInstance(
                dto.name(),
                dto.tradeName(),
                dto.taxIdNumber(),
                dto.typePerson(),
                dto.account().toEntity()
        );
    }
}
