package com.E3N.shared.utils;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateHelper {

    /**
     * examples of formats [ dd/MM/yyyy dd/MM/yyyy HH:mm:ss ]
     */
    public static Instant getDateFrom(final String dateString, final String format){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        } catch (DateTimeException | IllegalArgumentException e){
            return null;
        }
    }

}
