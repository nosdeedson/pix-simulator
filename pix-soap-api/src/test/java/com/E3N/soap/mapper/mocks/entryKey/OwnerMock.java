package com.E3N.soap.mapper.mocks.entryKey;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.account.AccountType;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.pix.domain.modules.owner.entryKey.Reason;
import com.E3N.pix.domain.modules.owner.owner.Owner;
import com.E3N.pix.domain.modules.owner.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.pix.service.owner.dto.OwnerDto;
import com.E3N.test.Owner.*;

import java.util.UUID;

public abstract class OwnerMock {

    public static Owner createOwner(TypePerson typePerson) {
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, "09/09/2026", key);
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
