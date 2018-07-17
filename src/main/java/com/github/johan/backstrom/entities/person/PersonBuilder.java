package com.github.johan.backstrom.entities.person;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.core.DataGeneration;
import com.github.johan.backstrom.common.standard.StandardAttribute;
import com.github.johan.backstrom.common.util.DefaultRandomnessImplementation;
import com.github.johan.backstrom.common.util.Randomness;
import com.github.johan.backstrom.entities.Country;
import com.github.johan.backstrom.entities.Countries;
import com.github.johan.backstrom.entities.finance.CreditCardBuilder;
import com.github.johan.backstrom.entities.util.DataHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonBuilder {

    private DocumentBuilder person = new DocumentBuilder();

    private Randomness randomness = new DefaultRandomnessImplementation();

    public PersonBuilder setRandomnessImplementation(Randomness randomnessImplementation){
        this.randomness = randomness;
        return this;
    }

    private Class countryImplementationClazz = Countries.class;

    public PersonBuilder setCountryImplementation(Country countryImplementationClass){
        this.countryImplementationClazz = countryImplementationClazz;
        return this;
    }

    private Attribute<Country> countryOfResidence = new StandardAttribute<>(
            "countryOfResidence",
            input -> {
                return Countries.getRandomCountry();
            }
    );

    public PersonBuilder setCountryOfResidenceGenerator(DataGeneration dataGeneration){
        countryOfResidence.setGenerator(dataGeneration);
        return this;
    }

    private Attribute<Gender> gender = new StandardAttribute<>(
            "gender",
            input -> Gender.getRandomGender()
    );

    private Attribute<String> givenName = new StandardAttribute<>(
            "givenName",
            input -> new NameHelper().getRandomFirstName(
                    (Gender)input.get("gender").getValue(),
                    (Country)input.get("countryOfResidence").getValue()
            )
    );

    public PersonBuilder setGivenNameGenerator(DataGeneration dataGeneration){
        givenName.setGenerator(dataGeneration);
        return this;
    }

    private Attribute<String> lastName = new StandardAttribute<>(
            "lastName",
            input -> new NameHelper().getRandomLastName(Countries.SWEDEN)
    );

    private Attribute<Integer> numberOfPhoneNumbers = new StandardAttribute<>(
            "numberOfPhoneNumbers",
            input -> DataHelper.getRandomNumber(1, 3)
    );

    private Attribute<List<String>> phoneNumbers = new StandardAttribute<>(
            "phoneNumbers",
            input -> DataHelper.getRandomMobilePhoneNumbers(
                    input.get("numberOfPhoneNumbers"),
                    input.get("countryOfResidence")
            )
    );

    private Attribute<List<Map<String, Object>>> creditCards = new StandardAttribute<>(
            "creditCards",
            input -> {
                List<Map<String, Object>> cards = new ArrayList<>();
                for (int i = 0; i < DataHelper.getRandomNumber(3); i++) {
                    cards.add(new CreditCardBuilder().toMap());
                }
                return cards;
            }
    );

    private PersonBuilder createDefaultModel(){
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
        person.addDependency(givenName, countryOfResidence);
        person.addDependency(lastName, countryOfResidence);
        return this;
    }

    public static PersonBuilder defaultModel(){
        return new PersonBuilder().createDefaultModel();
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