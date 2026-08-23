package com.E3N.pix.domain.mocks;

import com.E3N.pix.domain.modules.entry.account.Account;
import com.E3N.pix.domain.modules.entry.account.AccountType;

public abstract class AccountMock {

    public static Account builder(
            final String branch,
            final String number,
            final String openingDate,
            final String participant,
            final AccountType type
            ) {
        return Account.getInstance(
                branch, number, participant, type, openingDate);
    }

}
