package com.github.johan.backstrom.entities.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.standard.StandardAttribute;
import com.github.johan.backstrom.entities.Country;
import com.github.johan.backstrom.entities.finance.CreditCardBuilder;
import com.github.johan.backstrom.entities.util.DataHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonBuilder {

    DocumentBuilder person = new DocumentBuilder();

    Attribute<Country> countryOfResidence = new StandardAttribute<>(
            "countryOfResidence",
            input -> Country.getRandomCountry()
    );

    Attribute<Gender> gender = new StandardAttribute<>(
            "gender",
            input -> Gender.getRandomGender()
    );

    Attribute<String> givenName = new StandardAttribute<>(
            "givenName",
            input -> DataHelper.getRandomFirstName(input.get("gender"))
    );

    Attribute<String> lastName = new StandardAttribute<>(
            "lastName",
            input -> DataHelper.getRandomLastName()
    );

    Attribute<Integer> numberOfPhoneNumbers = new StandardAttribute<>(
            "numberOfPhoneNumbers",
            input -> DataHelper.getRandomNumber(1, 3)
    );

    Attribute<List<String>> phoneNumbers = new StandardAttribute<>(
            "phoneNumbers",
            input -> DataHelper.getRandomMobilePhoneNumbers(
                    input.get("numberOfPhoneNumbers"),
                    input.get("countryOfResidence")
            )
    );

    Attribute<List<Map<String, Object>>> creditCards = new StandardAttribute<>(
            "creditCards",
            input -> {
                List<Map<String, Object>> cards = new ArrayList<>();
                for (int i = 0; i < DataHelper.getRandomNumber(3); i++) {
                    cards.add(new CreditCardBuilder().toMap());
                }
                return cards;
            }
    );

    public PersonBuilder() {
        person.addAttribute(countryOfResidence);
        person.addAttribute(gender);
        person.addAttribute(givenName);
        person.addAttribute(lastName);
        person.addAttribute(numberOfPhoneNumbers);
        person.addAttribute(phoneNumbers);
        person.addAttribute(creditCards);

        person.addDependency(givenName, gender);
        person.addDependency(phoneNumbers, numberOfPhoneNumbers);
        person.addDependency(phoneNumbers, countryOfResidence);
    }

    public String build() {
        try {
            return new ObjectMapper().writeValueAsString(person.buildDataForEmptyAttributes().toMap());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public PersonBuilder setGender(Gender gender) {
        this.gender.setValue(gender);
        return this;
    }
}