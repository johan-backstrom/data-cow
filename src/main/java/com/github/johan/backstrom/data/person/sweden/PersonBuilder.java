package com.github.johan.backstrom.data.person.sweden;

import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.core.Randomness;
import com.github.johan.backstrom.common.standard.StandardAttribute;

public class PersonBuilder {

    DocumentBuilder person = new DocumentBuilder();

    public PersonBuilder(Randomness random){

    }

    public void setGivenName(String firstName) {
        Attribute<String> name = new StandardAttribute<>(
                "givenName",
                input -> DataHelper.getRandomFirstName(input.get("gender"))
        );
        person.addAttribute(name);
    }
}