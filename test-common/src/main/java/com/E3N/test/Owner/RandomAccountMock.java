package com.E3N.test.Owner;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.Random;

public abstract class RandomAccountMock {
    private static final Random random = new Random();

    public static String randomAccountNumber() {
        BigInteger value = BigInteger.valueOf(random.nextInt(Integer.MAX_VALUE));
        return value.toString();
    }

    public static String randomBranch() {
        int value = random.nextInt(9998) + 1;
        return StringUtils.leftPad(String.valueOf(value), 4, "0");
    }
}
