package com.example.covidcontrolx.utils;

import java.util.Locale;

public class CountryUtils {
    public static String getCountryCode(String countryName) { // get country code by country name
        String[] isoCountryCodes = Locale.getISOCountries();
        for (String code : isoCountryCodes) {
            Locale locale = new Locale("", code);
            if (countryName.equalsIgnoreCase(locale.getDisplayCountry())) {
                return code;
            }
        }
        return "";
    }
}
