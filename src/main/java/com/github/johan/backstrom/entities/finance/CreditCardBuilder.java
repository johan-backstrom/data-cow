package com.github.johan.backstrom.entities.finance;

import com.github.johan.backstrom.common.DocumentBuilder;
import com.github.johan.backstrom.common.core.Attribute;
import com.github.johan.backstrom.common.standard.StandardAttribute;
import com.github.johan.backstrom.common.util.LuhnAlgorithm;
import com.github.johan.backstrom.entities.util.DataHelper;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.util.Map;

public class CreditCardBuilder {

    DocumentBuilder creditCard = new DocumentBuilder();

    Attribute<CreditCardNetwork> creditCarNetwork = new StandardAttribute<>(
            "creditCardNetwork",
            input -> CreditCardNetwork.getRandomCreditCardNetwork()
    );

    Attribute<String> creditCardNumber = new StandardAttribute<>(
            "creditCardNumber",
            input -> createCardNumber(input.get("creditCardNetwork"))
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

    Attribute<String> expire = new StandardAttribute<>(
            "expire",
            input -> Strings.padStart(String.valueOf(input.get("expireMonth").getValue()), 2, '0')
                        .concat("/")
                        .concat(String.valueOf(input.get("expireYear").getValue()).substring(2,4))
    );

    public CreditCardBuilder() {
        creditCard.addAttribute(creditCarNetwork);
        creditCard.addAttribute(creditCardNumber);
        creditCard.addAttribute(expireMonth);
        creditCard.addAttribute(expireYear);
        creditCard.addAttribute(cvc);
        creditCard.addAttribute(expire);

        creditCard.addDependency(creditCardNumber, creditCarNetwork);
        creditCard.addDependency(expire, expireMonth);
        creditCard.addDependency(expire, expireYear);
    }

    private String createCardNumber(Attribute<CreditCardNetwork> creditCardNetwork) {
        String cardnumber =
                String.valueOf(creditCardNetwork.getValue().getStartingDigit())
                        .concat(String.valueOf(DataHelper.getRandomNumber(1000000, 9999999)))
                        .concat(String.valueOf(DataHelper.getRandomNumber(1000000, 9999999)));
        return cardnumber.concat(String.valueOf(LuhnAlgorithm.getCheckDigit(cardnumber)));
    }

    public String toString(){
        return creditCard.buildDataForEmptyAttributes().toString();
    }

    public Map<String, Object> toMap(){
        return creditCard.buildDataForEmptyAttributes().toMap();
    }
}
