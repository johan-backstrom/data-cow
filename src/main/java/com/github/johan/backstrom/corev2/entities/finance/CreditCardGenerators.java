package com.github.johan.backstrom.corev2.entities.finance;

import com.github.johan.backstrom.corev2.Generator;
import com.github.johan.backstrom.corev2.References;
import com.github.johan.backstrom.corev2.entities.util.LuhnAlgorithm;

import static com.github.johan.backstrom.corev2.entities.util.DataHelper.getRandomNumber;
import static com.github.johan.backstrom.corev2.entities.util.DataHelper.getRandomZeroPaddedNumber;

public class CreditCardGenerators {

    @Generator("network")
    public CreditCardNetwork randomNetwork() {
        return CreditCardNetwork.values()[getRandomNumber(CreditCardNetwork.values().length - 1)];
    }

    @Generator("creditCardNumber")
    public String getCardNumber(
            @References("network") CreditCardNetwork creditCardNetwork
    ) {
        String cardnumber = String.valueOf(creditCardNetwork.getStartingDigit())
                .concat(String.valueOf(getRandomNumber(1000000, 9999999)))
                .concat(String.valueOf(getRandomNumber(1000000, 9999999)));
        return cardnumber.concat(String.valueOf(LuhnAlgorithm.getCheckDigit(cardnumber)));
    }

    @Generator("expireMonth")
    public String getExpireMonth() {
        return getRandomZeroPaddedNumber(1, 12, 2);
    }

    @Generator("expireYear")
    public String getExpireYear() {
        return String.valueOf(getRandomNumber(2021, 2030));
    }

    @Generator("cvc")
    public String getCvc() {
        return String.valueOf(getRandomZeroPaddedNumber(0, 999, 3));
    }

    @Generator("expire")
    public String getExpire(
            @References("expireYear") String expireYear,
            @References("expireMonth") String expireMonth
    ) {
        return expireMonth.concat("/").concat(expireYear.substring(2,4));
    }
}
