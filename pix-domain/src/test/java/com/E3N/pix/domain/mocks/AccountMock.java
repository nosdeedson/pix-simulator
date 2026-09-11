package com.E3N.pix.domain.mocks;

import com.E3N.pix.domain.modules.owner.account.Account;
import com.E3N.pix.domain.modules.owner.account.AccountType;
import com.E3N.pix.domain.modules.owner.entryKey.EntryKey;
import com.E3N.test.Owner.RandomAccountMock;
import com.E3N.test.Owner.RandomDateMock;
import com.E3N.test.Owner.RandomParticipant;

import java.util.Arrays;
import java.util.Random;

public abstract class AccountMock {
    private static final Random random = new Random();

    public static Account getAccountWithEntryKey(
            final String branch,
            final String number,
            final String openingDate,
            final String participant,
            final AccountType type,
            final EntryKey entryKey
    ) {
        return Account.getInstance(
                branch, number, participant, type, openingDate, entryKey
        );
    }

    public static Account getRandomAccountWithSpecificEntryKey(final EntryKey key) {
        return Account.getInstance(
                RandomAccountMock.randomBranch(),
                RandomAccountMock.randomAccountNumber(),
                RandomParticipant.getParticipant(),
                randomAccountType(),
                RandomDateMock.getRandomStringDateWithoutTimeZone(),
                key
        );
    }

    public static AccountType randomAccountType() {
        return Arrays.asList(AccountType.values()).get(random.nextInt(AccountType.values().length));
    }

    public static Account getInvalidOne() {
        return Account.getInstance(
                "123456",
                "123456ll",
                "123456789",
                null,
                "32/08/2026",
                EntryKeyMock.getEntryKeyEmail()
        );
    }

    public static Account getInvalidOneWithInvalidKey() {
        return Account.getInstance(
                "123456",
                "123456ll",
                "123456789",
                null,
                "32/08/2026",
                EntryKeyMock.getInvalidEntryKeyCnpj(null)
        );
    }
}
