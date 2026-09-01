package com.E3N.pix.domain.mocks.domain;

import com.E3N.pix.domain.modules.entry.account.Account;
import com.E3N.pix.domain.modules.entry.account.AccountType;
import com.E3N.pix.domain.modules.entry.entryKey.EntryKey;
import com.E3N.test.entrykey.RandomAccount;
import com.E3N.test.entrykey.RandomDate;
import com.E3N.test.entrykey.RandomParticipant;

import java.util.Arrays;
import java.util.Random;

public abstract class AccountMock {

    private static final Random random = new Random();

    //
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

    //
    public static Account getRandomAccountWithSpecificEntryKey(final EntryKey key) {
        return Account.getInstance(
                RandomAccount.randomBranch(),
                RandomAccount.randomAccountNumber(),
                RandomParticipant.getParticipant(),
                randomAccountType(),
                RandomDate.getRandomDate(),
                key
        );
    }

    //
    public static AccountType randomAccountType() {
        return Arrays.asList(AccountType.values()).get(random.nextInt(4) + 1);
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
}
