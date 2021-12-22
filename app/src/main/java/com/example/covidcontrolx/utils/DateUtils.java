package com.example.covidcontrolx.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    public static String parseISODate(String date, String pattern) throws ParseException {
        DateTimeFormatter inputFormatter, outputFormatter = null;
        LocalDate date2 = null;
        inputFormatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH);
        outputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH);
        date2 = LocalDate.parse(date, inputFormatter);
        return outputFormatter.format(date2);
    }
}
