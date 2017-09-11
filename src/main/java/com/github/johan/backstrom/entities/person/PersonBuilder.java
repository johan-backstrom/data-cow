package com.github.johan.backstrom.entities.person;

import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.standard.StandardAttribute;
import com.github.johan.backstrom.entities.Country;

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

    Attribute<Integer> numberOfPhoneNumbers =  new StandardAttribute<>(
            "numberOfPhoneNumbers",
            input -> DataHelper.getRandomNumber(1, 3)
    );

    Attribute<List<String>> phoneNumbers =  new StandardAttribute<>(
            "phoneNumbers",
            input -> DataHelper.getRandomMobilePhoneNumbers(
                    input.get("numberOfPhoneNumbers"),
                    input.get("countryOfResidence")
            )
    );

    Attribute<Map<String, Object>> numberOfPhoneNumbers =  new StandardAttribute<>(
            "numberOfPhoneNumbers",
            input -> DataHelper.getRandomNumber(1, 3)
    );

    public PersonBuilder() {
        person.addAttribute(countryOfResidence);
        person.addAttribute(gender);
        person.addAttribute(givenName);
        person.addAttribute(lastName);
        person.addAttribute(numberOfPhoneNumbers);
        person.addAttribute(phoneNumbers);

        person.addDependency(givenName, gender);
        person.addDependency(phoneNumbers, numberOfPhoneNumbers);
        person.addDependency(phoneNumbers, countryOfResidence);
    }

    public String build(){
        return person.buildDataForEmptyAttributes().toString();
    }

    public PersonBuilder setGender(Gender gender){
        this.gender.setValue(gender);
        return this;
    }
}