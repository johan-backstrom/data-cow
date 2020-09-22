package com.github.johan.backstrom.corev2.entities.address;

import com.github.johan.backstrom.corev2.Generator;

import static com.github.johan.backstrom.corev2.entities.util.DataHelper.*;

public class AddressGenerators {

    @Generator("street")
    public String randomStreet(){
        return streetPrefixes[getRandomNumber(streetPrefixes.length-1)]
                .concat(streetTypes[getRandomNumber(streetTypes.length-1)]);
    }

    @Generator("streetNumber")
    public String randomStreetNumber(){
        return String.valueOf(getRandomNumber(150));
    }

    @Generator("postalCode")
    public String randomPostalCode(){
        return getRandomZeroPaddedNumber(100, 999, 3).concat(" ")
                .concat(getRandomZeroPaddedNumber(10, 99, 2));
    }

    @Generator("postalLocality")
    public String randomPostalLocality(){
        return swedishCities[getRandomIndex(swedishCities.length)];
    }

    private final static String[] streetPrefixes = {
            "Stora ",
            "Lilla ",
            "Gustav den III ",
            "Lena den II ",
            "Björn",
            "Kanin"
    };

    private final static String[] streetTypes = {
            "boulevarden",
            "gatan",
            "stigen",
            "vägen",
            "stradan"
    };

    private final static String[] swedishCities = {
            "Stockholm",
            "Göteborg",
            "Malmö"
    };



}
