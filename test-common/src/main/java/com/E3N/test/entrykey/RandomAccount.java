package com.E3N.test.entrykey;

import java.math.BigInteger;
import java.util.Random;

public abstract class RandomAccount {
    private static Random random = new Random();

    public static String randomAccountNumber() {
        BigInteger value = BigInteger.valueOf(random.nextInt(Integer.MAX_VALUE));
        return value.toString();
    }

    public static String randomBranch() {
        int value = random.nextInt(9998) + 1;
        return String.valueOf(value);
    }
}
