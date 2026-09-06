package com.E3N.test.Owner;

import com.github.javafaker.Faker;

public abstract class RandomValidName {
    private static final Faker faker = new Faker();

    public static String randomValidName(){
        return faker.name().fullName().replaceAll("[^a-zA-Z ]", "");
    }

    public static String randomValidCompanyName(){
        return faker.company().name().replaceAll("[^a-zA-Z _-]", "");
    }
}
