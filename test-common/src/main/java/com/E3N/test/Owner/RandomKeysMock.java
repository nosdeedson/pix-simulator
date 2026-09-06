package com.E3N.test.Owner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class RandomKeysMock {

    private static final String[] EMAILS = {
            "gabriel.silva@gmail.com",
            "lucas.santos@yahoo.com",
            "julia.oliveira@outlook.com",
            "mariana.costa@hotmail.com",
            "felipe.almeida@icloud.com",
            "beatriz.lima@gmail.com",
            "pedro.rodrigues@yahoo.com",
            "larissa.martins@outlook.com",
            "thiago.gomes@hotmail.com",
            "camila.barbosa@icloud.com",
    };

    private static final String[] INVALID_EMAILS = {
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@example.com",
            "Joe Smith <email@example.com>",
            "email.example.com",
            "email@example@example.com",
            ".email@example.com",
            "email.@example.com",
            "email..email@example.com",
            "あいうえお@example.com",
            "email@example.com (Joe Smith)",
            "email@example",
            "email@-example.com",
            "email@example.web",
            "email@111.222.333.44444",
            "email@example..com",
            "Abc..123@example.com"
    };

    private static final String[] PHONE_NUMBER = {
            "+5511961774958",
            "+5521983214567",
            "+5531975438821",
            "+5541991237744",
            "+5561986200876",
            "+5571992441040",
            "+5585981123456",
            "+5519988471791",
            "+5516991277425",
            "+5534997354105",
    };

    private static final String[] uuids = {
            "a9cc23d1-238f-44d4-932e-dd24b12eeaa7",
            "28b941d3-e3b8-4dd0-b984-688a135d458a",
            "7ae8c48f-6c70-424b-a228-0f6321e9c141",
            "3ae7accb-138b-421d-ad91-f0e2446f5ff3",
            "95887584-ed40-4ed9-a244-9ae7aeb5a070",
            "af7f77fc-b6a3-4b83-96e8-375687d113a5",
            "d4d932ee-ed72-493f-ae1b-a4bbe8f61079",
            "751d92b6-6075-4350-bc05-5a9debb74ebd",
            "b916da84-7722-43fd-912f-e98135b32dd1",
            "c1afcdad-649f-4b0c-a937-c22671bbdc84",
    };

    private static final Random random = new Random();

    public static String randomEVP() {
        return Arrays.asList(uuids).get(random.nextInt(9) + 1);
    }

    public static String randomEmails() {
        return Arrays.asList(EMAILS).get(random.nextInt(9) + 1);
    }

    public static String randomInvalidEmails() {
        return Arrays.asList(INVALID_EMAILS).get(random.nextInt(16) + 1);
    }

    public static String randomPhone() {
        return Arrays.asList(PHONE_NUMBER)
                .get(random.nextInt(9) + 1);
    }

    public static String randomInvalidPhoneNumber(){
        List<String> invalidNumbers = Arrays.asList(
                "+55 (11) 8765-4321",      // Missing mandatory mobile 9th digit
                "+55 (21) 79123-4567",     // Mobile 9th digit is not a 9
                "+55 (11) 91234-56789",    // Too many digits
                "+55 (31) 1234-5678",      // Landline starting with invalid digit 1
                "+55 (51) 0234-5678",      // Landline starting with invalid digit 0
                "+55 (01) 99123-4567",     // Area code (DDD) 01 does not exist
                "+55 (20) 99123-4567",     // Area code (DDD) 20 does not exist
                "+55 (60) 99123-4567",     // Area code (DDD) 60 does not exist
                "+55 021 (11) 99123-4567", // Contains internal domestic carrier code (021)
                "015 55 (11) 99123-4567"   // Misplaced carrier code before country code
        );
        return invalidNumbers.get(random.nextInt(10));
    }

    public static String randomNaturalPersonDocument() {
        return RandomCpfMock.getRandomCFP();
    }

    public static String randomInvalidNaturalPersonDocuments() {
        return RandomCpfMock.getRandomInvalidCPF();
    }

    public static String randomLegalPersonDocument() {
        return RandomCNPJMock.getRandomCNPJ();
    }

    public static String randomInvalidLegalPersonDocument(final Integer whichOne) {
        return RandomCNPJMock.getRandomInvalidCNPJ(whichOne);
    }
}
