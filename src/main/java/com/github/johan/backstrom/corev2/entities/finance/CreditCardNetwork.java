package com.github.johan.backstrom.corev2.entities.finance;

public enum CreditCardNetwork {

    VISA("Visa", 4),
    MASTERCARD("Mastercard", 5),
    AMERICAN_EXPRESS("American Express", 3);

    String creditCardNetworkName;
    int startingDigit;

    CreditCardNetwork (String creditCardNetworkName, int startingDigit){
        this.creditCardNetworkName = creditCardNetworkName;
        this.startingDigit = startingDigit;
    }

    public String getCreditCardNetworkName() {
        return creditCardNetworkName;
    }

    public int getStartingDigit() {
        return startingDigit;
    }
}
