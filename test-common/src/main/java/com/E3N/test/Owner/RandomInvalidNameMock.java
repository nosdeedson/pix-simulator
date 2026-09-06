package com.E3N.test.Owner;

import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.Random;

public abstract class RandomInvalidNameMock {

    private static final String[] SPECIAL_CHARACTERS = {
            "!", "@", "#", "$", "%", "&", "*", "(", ")"
    };
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    public static String getInvalidName() {
        var defect = " " + Arrays.asList(SPECIAL_CHARACTERS).get(random.nextInt(SPECIAL_CHARACTERS.length - 1) + 1);
        return faker.name().fullName().replace(" ", defect);
    }

    public static String getInvalidCompanyName() {
        var whichOne = random.nextInt(SPECIAL_CHARACTERS.length + 1);
        if (whichOne == 9) return null;
        var defect = " " + Arrays.asList(SPECIAL_CHARACTERS).get(whichOne);
        return faker.company().name().replace(" ", defect);
    }
}
