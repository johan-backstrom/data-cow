package com.github.johan.backstrom.entities.finance;

public class CreditCard {

    private String creditCardNumber;
    private String expireMonth;
    private String getExpireYear;
    private String cvc;

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setExpireMonth(String expireMonth) {
        this.expireMonth = expireMonth;
    }

    public void setGetExpireYear(String getExpireYear) {
        this.getExpireYear = getExpireYear;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
