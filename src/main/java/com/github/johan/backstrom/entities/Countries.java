package com.github.johan.backstrom.entities;

public enum Countries implements Country {

    SWEDEN("SE", "SWE", "Sweden", "+46");

    private String dialingPrefix;
    private String iso2countryCode;
    private String iso3countryCode;
    private String countryName;

    private Countries(String iso2countryCode, String iso3countryCode, String countryName, String dialingPrefix){

        this.iso2countryCode = iso2countryCode;
        this.iso3countryCode = iso3countryCode;
        this.countryName = countryName;
        this.dialingPrefix = dialingPrefix;
    }

    public static Country getRandomCountry(){
        // TODO: implement proper randomness
        return Countries.values()[(int)(Math.random() * Countries.values().length)];
    }

    @Override
    public String toString(){
        return countryName;
    }

    @Override
    public String getDialingPrefix() {
        return dialingPrefix;
    }

    @Override
    public String getIso2countryCode() {
        return iso2countryCode;
    }

    @Override
    public String getIso3countryCode() {
        return iso3countryCode;
    }

    @Override
    public String getCountryName() {
        return countryName;
    }
}
