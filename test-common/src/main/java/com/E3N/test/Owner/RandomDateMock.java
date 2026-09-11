package com.E3N.test.Owner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

public abstract class RandomDateMock {

    private static final Random random = new Random();

    public static String getRandomDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate startDate = LocalDate.of(1970, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 8, 31);
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();
        long randomEpochDay = startEpochDay + random.nextLong(endEpochDay - startEpochDay + 1);
        LocalDate randomDate = LocalDate.ofEpochDay(randomEpochDay);
        return formatter.format(randomDate);
    }

    public static String getRandomStringDate() {
        var dateString = Arrays.asList(
                "2024-01-15T10:30:00Z",
                "2023-12-25T23:59:59Z",
                "2022-06-15T14:25:30.500Z",
                "2024-09-09T00:00:00Z",
                "2021-03-20T16:45:30.123Z",
                "2025-07-04T12:00:00Z",
                "2020-11-30T08:15:45.999Z",
                "2024-02-29T18:30:15Z",
                "2023-05-17T09:22:33.456Z",
                "2026-10-01T20:10:05.789Z"
        );
        return dateString.get(random.nextInt(dateString.size()));
    }
}
