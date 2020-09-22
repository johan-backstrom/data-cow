package com.github.johan.backstrom.corev2.entities.address;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;

@WithGenerators(AddressGenerators.class)
public class Address {

    @Attribute("street")
    private String street;

    @Attribute("streetNumber")
    private String streetNumber;

    @Attribute("postalCode")
    private String postalCode;

    @Attribute("postalLocality")
    private String postalLocality;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalLocality() {
        return postalLocality;
    }

    public void setPostalLocality(String postalLocality) {
        this.postalLocality = postalLocality;
    }

}
