package com.github.johan.backstrom.entities.finance;

import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.standard.StandardAttribute;
import com.github.johan.backstrom.common.util.LuhnAlgorithm;
import com.github.johan.backstrom.entities.person.DataHelper;

import java.time.LocalDate;
import java.util.Map;

public class CreditCardBuilder {

    DocumentBuilder creditCard = new DocumentBuilder();

    Attribute<String> creditCardNumber = new StandardAttribute<>(
            "creditCardNumber",
            input -> createCardNumber()
    );

    Attribute<String> expireMonth = new StandardAttribute<>(
            "expireMonth",
            input -> String.valueOf(LocalDate.now().getMonth().getValue())
    );

    Attribute<String> expireYear = new StandardAttribute<>(
            "expireYear",
            input -> String.valueOf(LocalDate.now().getYear() + 1)
    );

    Attribute<String> cvc = new StandardAttribute<>(
            "cvc",
            input -> String.valueOf(DataHelper.getRandomNumber(100, 999))
    );

    public CreditCardBuilder() {
        creditCard.addAttribute(creditCardNumber);
        creditCard.addAttribute(expireMonth);
        creditCard.addAttribute(expireYear);
        creditCard.addAttribute(cvc);
    }

    private String createCardNumber() {
        String cardnumber =
                "4"
                        .concat(String.valueOf(DataHelper.getRandomNumber(1000000, 9999999)))
                        .concat(String.valueOf(DataHelper.getRandomNumber(1000000, 9999999)));
        return cardnumber.concat(String.valueOf(LuhnAlgorithm.getCheckDigit(cardnumber)));
    }

    public CreditCard build(){
        Map<String, Object> cc = creditCard.buildDataForEmptyAttributes().toMap();
        return new CreditCard();
    }

}
