package com.E3N.pix.application.mocks;

import com.E3N.pix.domain.modules.owner.account.AccountType;

import java.util.Arrays;
import java.util.Random;

public abstract class RandomAccountTypeMock {
    private final static Random random = new Random();

    public static AccountType getAccountType() {
        return Arrays.asList(AccountType.values()).get(random.nextInt(4) + 1);
    }
}
