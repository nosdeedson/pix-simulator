package com.E3N.pix.service.ownership.mocks;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.test.Owner.*;
import com.github.javafaker.Faker;

import java.util.UUID;

public abstract class OwnerMock {

    private static final Faker faker = new Faker();

    public static Owner mockOwner(){
        var name = faker.name().fullName();
        var key = EntryKey.getInstance(RandomKeysMock.randomEmails(), TypeKey.EMAIL, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(), RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), key);
        return Owner.getInstance(name, name, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
    }
}
