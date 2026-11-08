package com.E3N.pix.service.ownership.mocks;

import com.E3N.pix.domain.modules.ownership.account.Account;
import com.E3N.pix.domain.modules.ownership.account.AccountType;
import com.E3N.pix.domain.modules.ownership.entryKey.EntryKey;
import com.E3N.pix.domain.modules.ownership.entryKey.Reason;
import com.E3N.pix.domain.modules.ownership.owner.Owner;
import com.E3N.pix.domain.shared.TypeKey;
import com.E3N.pix.domain.shared.TypePerson;
import com.E3N.pix.domain.valueObject.key.Key;
import com.E3N.test.Owner.*;
import com.github.javafaker.Faker;

import java.text.Normalizer;
import java.util.UUID;

public abstract class OwnerMock {

    private static final Faker faker = new Faker();

    public static Owner mockOwner(EntryKey entryKey) {
        var name = faker.name().firstName() + " " + faker.name().lastName();
        name = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")         // remove accents
                .replaceAll("[^a-zA-Z ]", "")     // remove ', -, .
                .replaceAll("\\s+", " ")
                .trim();
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), entryKey);
        return Owner.getInstance(name, name, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
    }

    public static Owner mockOwnerPassingKey(String entryKey, TypeKey typeKey) {
        var name = faker.name().firstName() + " " + faker.name().lastName();
        name = Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")         // remove accents
                .replaceAll("[^a-zA-Z ]", "")     // remove ', -, .
                .replaceAll("\\s+", " ")
                .trim();
        var Key = EntryKey.getInstance(entryKey, typeKey, Reason.USER_REQUESTED, UUID.randomUUID().toString());
        var account = Account.getInstance(RandomAccountMock.randomBranch(), RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(), AccountType.CACC, RandomDateMock.getRandomStringDateWithoutTimeZone(), Key);
        return Owner.getInstance(name, name, RandomCpfMock.getRandomCFP(), TypePerson.NATURAL_PERSON, account);
    }
}
