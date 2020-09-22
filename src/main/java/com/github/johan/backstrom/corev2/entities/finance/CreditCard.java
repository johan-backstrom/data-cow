package com.github.johan.backstrom.corev2.entities.finance;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;

@WithGenerators(CreditCardGenerators.class)
public class CreditCard {

    @Attribute("network")
    private CreditCardNetwork creditCarNetwork;

    @Attribute("creditCardNumber")
    private String creditCardNumber;

    @Attribute("expireMonth")
    private String expireMonth;

    @Attribute("expireYear")
    private String expireYear;

    @Attribute("cvc")
    private String cvc;

    @Attribute("expire")
    private String expire;

    public CreditCardNetwork getCreditCarNetwork() {
        return creditCarNetwork;
    }

    public void setCreditCarNetwork(CreditCardNetwork creditCarNetwork) {
        this.creditCarNetwork = creditCarNetwork;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(String expireYear) {
        this.expireYear = expireYear;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
