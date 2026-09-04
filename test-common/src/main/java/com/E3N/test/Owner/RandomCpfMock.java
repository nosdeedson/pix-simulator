package com.E3N.test.Owner;

import java.util.Arrays;
import java.util.Random;

public abstract class RandomCpfMock {
    private static final Random random = new Random();

    public static final String[] NATURAL_PERSON_DOCUMENTS = {
            "88756715838",
            "32632502306",
            "84337186492",
            "95850922806",
            "57267372376",
            "61299482473",
            "11874476098",
            "94943637876",
            "36176832586",
            "36813175505",
            "30724501967",
            "04963391414",
            "58776735567",
            "91621117448",
            "21588714403",
            "64530495116",
            "05484159865",
            "13683263075",
            "17090774090",
            "26947723744",
    };

    private static final String[] INVALID_NATURAL_PERSON_DOCUMENTS = {
            "00000000000",
            "11111111111",
            "22222222222",
            "33333333333",
            "44444444444",
            "55555555555",
            "66666666666",
            "77777777777",
            "88888888888",
            "99999999999",
    };

    public static String getRandomCFP() {
        return Arrays.asList(NATURAL_PERSON_DOCUMENTS).get(random.nextInt(NATURAL_PERSON_DOCUMENTS.length - 1) + 1);
    }

    public static String getRandomInvalidCPF() {
        return Arrays.asList(INVALID_NATURAL_PERSON_DOCUMENTS).get(random.nextInt(INVALID_NATURAL_PERSON_DOCUMENTS.length - 1) + 1);
    }
}
