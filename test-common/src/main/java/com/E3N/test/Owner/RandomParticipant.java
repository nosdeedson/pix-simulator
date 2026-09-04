package com.E3N.test.Owner;

import java.util.Arrays;
import java.util.Random;

public abstract class RandomParticipant {

    private static final Random random = new Random();

    private static final String[] RANDOM_PARTICIPANT = {
            "8HTVVWKM",
            "M9ML8NWE",
            "S68KTW12",
            "4L26KY4K",
            "TEKDAVHW",
            "TWN3176R",
            "6R8159J3",
            "R0W15WWA",
            "CM7ENBRV",
            "JCCVLDBL",
            "41977322",
            "25597632",
            "47712068",
            "50872524",
            "65689286",
            "22925254",
            "35551069",
            "03241440",
            "59874051",
            "44251051"
    };

    public static String getParticipant() {
        int r = random.nextInt(19) + 1;
        return Arrays.asList(RANDOM_PARTICIPANT).get(r);
    }
}
