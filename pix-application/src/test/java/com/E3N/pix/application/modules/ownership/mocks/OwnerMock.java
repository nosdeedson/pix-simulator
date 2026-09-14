package com.E3N.pix.application.modules.ownership.mocks;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.modules.ownership.owner.TypePerson;
import com.E3N.pix.domain.valueObject.key.TypeKey;
import com.E3N.test.Owner.*;

import java.util.UUID;

public abstract class OwnerMock {
    public static Owner getOwner(TypePerson typePerson) {
        var key = EntryKey.getInstance(
                RandomKeysMock.randomEVP(), TypeKey.EVP,
                Reason.USER_REQUESTED, UUID.randomUUID().toString()
        );
        var account = Account.getInstance(
                RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(), AccountType.CACC,
                "2026-09-10 12:00:00", key
        );
        if (TypePerson.LEGAL_PERSON.equals(typePerson)) {
            return Owner.getInstance(
                    RandomValidName.randomValidCompanyName(), RandomValidName.randomValidCompanyName(),
                    RandomCNPJMock.getRandomCNPJ(), TypePerson.LEGAL_PERSON, account
            );
        }
        return Owner.getInstance(
                RandomValidName.randomValidName(), null,
                RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account
        );
    }
}
