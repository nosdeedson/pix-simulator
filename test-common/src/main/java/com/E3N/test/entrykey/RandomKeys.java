package com.E3N.test.entrykey;

import java.util.Arrays;
import java.util.Random;

public abstract class RandomKeys {

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

    public static final String[] INVALID_NATURAL_PERSON_DOCUMENTS = {
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

    public static String randomNaturalPersonDocument() {
        return Arrays.asList(NATURAL_PERSON_DOCUMENTS).get(random.nextInt(19) + 1);
    }

    public static String randomInvalidNaturalPersonDocuments() {
        return Arrays.asList(INVALID_NATURAL_PERSON_DOCUMENTS).get(random.nextInt(9) + 1);
    }

    public static String randomLegalPersonDocument() {
        return Arrays.asList(LEGAL_PERSON_DOCUMENTS).get(random.nextInt(19) + 1);
    }

    public static String randomInvalidLegalPersonDocument(final Integer whichOne) {
        int which = whichOne == null ? random.nextInt() + 1 : whichOne;
        return Arrays.asList(INVALID_LEGAL_PERSON_DOCUMENTS).get(which);
    }
}
