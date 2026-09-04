package com.E3N.test.Owner;

import java.util.Arrays;
import java.util.Random;

public abstract class RandomCNPJMock {

    private static final Random random = new Random();

    public static final String[] LEGAL_PERSON_DOCUMENTS = {
            "41977322000172",
            "25597632000105",
            "47712068000167",
            "50872524000140",
            "65689286000100",
            "22925254000153",
            "35551069000198",
            "03241440000129",
            "59874051000195",
            "44251051000161",
            "46VLY3XS000110",
            "KTGWB8YE000107",
            "NELMH6YJ000144",
            "1S0B4Z6B000105",
            "0985XE8T000188",
            "N4W4ZB5V000101",
            "RDRVJHS8000164",
            "S63BCM7L000133",
            "CMW0HZHA000135",
            "8DT74MNN000100",
    };

    public static final String[] INVALID_LEGAL_PERSON_DOCUMENTS = {
            "00000000000000",
            "11111111111111",
            "12345678000190",
            "99999999999999",
            "07526557000100",
            "45987321000112",
            "33444555000178",
            "14785236000144",
            "88777666000133",
            "55666777000199",
    };

    public static String getRandomCNPJ() {
        return Arrays.asList(LEGAL_PERSON_DOCUMENTS).get(random.nextInt(LEGAL_PERSON_DOCUMENTS.length - 1) + 1);
    }

    public static String getRandomInvalidCNPJ(final Integer whichOne) {
        int which = whichOne == null ? (random.nextInt(INVALID_LEGAL_PERSON_DOCUMENTS.length - 1) + 1) : whichOne;
        return Arrays.asList(INVALID_LEGAL_PERSON_DOCUMENTS).get(which);
    }
}
