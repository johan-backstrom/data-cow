package com.github.johan.backstrom.entities.util;

import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.util.DefaultRandomnessImplementation;
import com.github.johan.backstrom.common.util.Randomness;
import com.github.johan.backstrom.entities.Country;
import com.github.johan.backstrom.entities.person.Gender;
import com.google.common.base.Strings;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    private static Randomness random = new DefaultRandomnessImplementation();

    public static String getRandomFirstName(Attribute<Gender> genderAttribute) {
        return genderAttribute.getValue().equals(Gender.Male) ?
                maleSwedishNames[getRandomIndex(maleSwedishNames.length)]
                : femaleSwedishNames[getRandomIndex(femaleSwedishNames.length)];
    }

    public static String getRandomLastName() {
        return maleSwedishNames[getRandomIndex(maleSwedishNames.length)].concat("sson").replace("sss", "ss");

    }

    public static String getRandomStreet() {
        return streetPrefixes[getRandomIndex(streetPrefixes.length)].concat(streetTypes[getRandomIndex(streetTypes.length)]);
    }

    public static String getRandomstreetNumber() {
        return String.valueOf(getRandomNumber(150));
    }

    public static String getRandomStreetNumberSuffix() {
        return streetNumberSuffixes[getRandomIndex(streetNumberSuffixes.length)];
    }

    public static String getRandomAparmentNumber() {
        return getRandomZeroPaddedNumber(11, 16, 2).concat(getRandomZeroPaddedNumber(1, 6, 2));
    }

    public static String getRandomPostalCode() {
        return getRandomZeroPaddedNumber(100, 999, 3).concat(" ").concat(getRandomZeroPaddedNumber(10, 99, 2));
    }

    public static String getRandomCity() {
        return swedishCities[getRandomIndex(swedishCities.length)];
    }

    public static String getRandomLandlinePhoneNumber() {
        return String.format("%s%s", "+46", getRandomZeroPaddedNumber(800000000, 899999999, 9));
    }

    public static String getRandomMobilePhoneNumber(Country country) {
        return String.format("%s%s", country.getDialingPrefix(), getRandomZeroPaddedNumber(700000000, 769999999, 9));
    }

    public static List<String> getRandomMobilePhoneNumbers(Attribute<Integer> numberOfNumbers, Attribute<Country> country){
        List<String> phones = new ArrayList<>();
        for (int i = 1; i <= numberOfNumbers.getValue(); i++){
            phones.add(getRandomMobilePhoneNumber(country.getValue()));
        }
        return phones;
    }

    public static String getRandomDateOfBirth() {
        return String.format(
                "%s-%s-%s",
                getRandomZeroPaddedNumber(1900, 2015, 4),
                getRandomZeroPaddedNumber(1, 12, 2),
                getRandomZeroPaddedNumber(1, 31, 2)
        );
    }

    public static LocalDate getDateOfBirthFromLegalId(String legalId) throws ParseException {
        return LocalDate.of(
                Integer.valueOf(legalId.substring(0, 4)),
                Integer.valueOf(legalId.substring(4, 6)),
                Integer.valueOf(legalId.substring(6, 8))
        );
    }

    public static String getRandomLegalId() {
        return String.format(
                "%s%s%s%s",
                getRandomZeroPaddedNumber(1900, 2015, 4),
                getRandomZeroPaddedNumber(1, 12, 2),
                getRandomZeroPaddedNumber(1, 28, 2),
                getRandomZeroPaddedNumber(1, 9999, 4)
        );
    }

    public static <T> T sometimesNull(T input, int percentageNull) {
        if (getRandomNumber(100) <= percentageNull) {
            return null;
        } else {
            return input;
        }

    }

    public static boolean sometimesTrue(int percentage) {
        return getRandomNumber(100) <= percentage;
    }

    public static String getRandomCountryCode() {
        return "SE";
        // This needs to be parameterized in some way since we can't have a swedish person with norwegian phone and finnish address
        //return countryCodes[getRandomNumber(countryCodes.length)];
    }

    public static String getRandomLanguageCode() {
        return "sv";

        // This needs to be parameterized in some way since we can't have a swedish person with norwegian phone and finnish address
        //return languageCodes[getRandomNumber(languageCodes.length)];
    }

    private static String getRandomZeroPaddedNumber(int min, int max, int length) {
        return Strings.padStart(
                String.format("%d", random.getRandomInteger(min, max)),
                length,
                '0'
        );
    }

    public static Integer getRandomNumber(int max) {
        return random.getRandomInteger(0, max);
    }

    public static Integer getRandomNumber(int min, int max) {
        return random.getRandomInteger(min, max);
    }

    private static int getRandomIndex(int length) {
        return getRandomNumber(length - 1);
    }

    private final static String[] maleSwedishNames = {
            "Adam",
            "Bertil",
            "Cesar",
            "David",
            "Erik",
            "Filip",
            "Gustav",
            "Helge",
            "Ivar",
            "Johan",
            "Kalle",
            "Ludvig",
            "Martin",
            "Niklas",
            "Olof",
            "Petter",
            "Qvintus",
            "Rudolf",
            "Sigurd",
            "Tore",
            "Urban",
            "Viktor",
            "Wilhelm",
            "Xerxes",
            "Yngve",
            "Zäta",
            "Åke",
            "Ärlig",
            "Östen",
            // Some extra funny swedish names:
            "Tommy",
            "Johan",
            "Max",
            "Pär",
            "Ragnar",
            "Björn"
    };

    private final static String[] femaleSwedishNames = {
            "Mikaela",
            "Carolina",
            "Anna",
            "Erika",
            "Filippa",
            "Helena",
            "Ivana",
            "Johanna"
    };


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

    private final static String[] streetNumberSuffixes = {
            "A",
            "B",
            "C",
            ""
    };
}
