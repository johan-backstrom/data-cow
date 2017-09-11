package com.github.johan.backstrom.entities;

public enum Country {
    SWEDEN("SE", "Sweden", "+46");

    private String dialingPrefix;
    private String iso2countryCode;
    private String countryName;

    Country(String iso2countryCode, String countryName, String dialingPrefix){
        this.iso2countryCode = iso2countryCode;
        this.countryName = countryName;
        this.dialingPrefix = dialingPrefix;
    }

    public static Country getRandomCountry(){
        // TODO: implement proper randomness
        return Country.values()[(int)(Math.random() * Country.values().length)];
    }

    public String toString(){
        return countryName;
    }

    public String getDialingPrefix() {
        return dialingPrefix;
    }

    public String getIso2countryCode() {
        return iso2countryCode;
    }

    public String getCountryName() {
        return countryName;
    }
}
