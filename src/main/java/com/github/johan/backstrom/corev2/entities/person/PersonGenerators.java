package com.github.johan.backstrom.corev2.entities.person;

import com.github.johan.backstrom.corev2.DataCow;
import com.github.johan.backstrom.corev2.Generator;
import com.github.johan.backstrom.corev2.References;
import com.github.johan.backstrom.corev2.entities.address.Address;
import com.github.johan.backstrom.corev2.entities.finance.CreditCard;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static com.github.johan.backstrom.corev2.entities.person.Sex.male;
import static com.github.johan.backstrom.corev2.entities.util.DataHelper.*;

public class PersonGenerators {

    @Generator("sex")
    public Sex getRandomSex() {
        return Sex.values()[getRandomIndex(Sex.values().length)];
    }

    @Generator("phoneNumber")
    public String getRandomPhoneNumber() {
        return String.format("+46 (0)%s", getRandomZeroPaddedNumber(700000000, 769999999, 9));
    }

    @Generator("dateOfBirth")
    public LocalDate getDateOfBirth(){
        Month month = Month.of(getRandomNumber(1, 12));
        return LocalDate.of(
                getRandomNumber(1900, 2015),
                month,
                getRandomNumber(1, month.minLength())
        );
    }

    @Generator("givenName")
    public String getGivenName(
            @References("sex") Sex sex
    ) {
        if (male.equals(sex)) {
            return maleNames[getRandomIndex(maleNames.length)];
        } else {
            return femaleNames[getRandomIndex(femaleNames.length)];
        }
    }

    @Generator("lastName")
    public String getLastName() {
        return lastNames[getRandomIndex(lastNames.length)];
    }

    @Generator("creditCards")
    public List<CreditCard> getCreditCards() {
        List<CreditCard> cards = new ArrayList<>();
        int i = 0;
        while(i++ < getRandomNumber(1, 3)){
            cards.add(
                    DataCow.generateDairyFor(CreditCard.class).milkCow()
            );
        }
        return cards;
    }

    @Generator("address")
    public Address getAddress() {
        return DataCow.generateDairyFor(Address.class).milkCow();
    }

    private static final String[] maleNames = {
            "Adam",
            "Christopher",
            "Daniel",
            "Donald",
            "Eric",
            "Frank",
            "John",
            "Max",
            "Michael",
            "Robert",
            "Roger",
            "Tim",
    };

    private static final String[] femaleNames = {
            "Mariah",
            "Marie",
            "Stephanie",
            "Helena",
            "Christina",
            "Magdalena",
            "Jenny",
            "Anette",
            "Linda"
    };

    private static final String[] lastNames = {
            "Anderson",
            "Jackson",
            "Schroder",
            "Fermi",
            "Curie",
            "Wilson",
            "Richardson",
            "Jensen"
    };

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
}
