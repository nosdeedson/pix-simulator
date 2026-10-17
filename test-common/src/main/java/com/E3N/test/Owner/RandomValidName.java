package com.E3N.test.Owner;

import com.github.javafaker.Faker;

import java.text.Normalizer;

public abstract class RandomValidName {
    private static final Faker faker = new Faker();

    public static String randomValidName(){
        var name =  faker.name().firstName() + " " + faker.name().lastName();
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")         // remove accents
                .replaceAll("[^a-zA-Z ]", "")     // remove ', -, .
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static String randomValidCompanyName(){
        return faker.company().name().replaceAll("[^a-zA-Z _-]", "");
    }
}
