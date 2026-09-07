package com.E3N.shared.utils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;

public final class DateHelper {

    /**
     * examples of formats [ dd/MM/yyyy dd/MM/yyyy HH:mm:ss ]
     */
    public static Instant getDateFrom(final String dateString, final String format) {
        if (dateString == null) return null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        } catch (DateTimeException | IllegalArgumentException e) {
            return null;
        }
    }

    public static XMLGregorianCalendar fromInstant(final Instant day) {
        if (day == null) return null;

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day.toEpochMilli());
        return DatatypeFactory.newDefaultInstance().newXMLGregorianCalendar(calendar);
    }

}
