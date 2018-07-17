package com.github.johan.backstrom.entities.address;

import com.github.johan.backstrom.common.util.DefaultRandomnessImplementation;
import com.github.johan.backstrom.common.util.Randomness;
import com.github.johan.backstrom.entities.Country;

public class AddressBuilder{

    String street;
    String streetNumber;
    String postalCode;
    String postalLocality;
    Country country;

    Randomness randomness = new DefaultRandomnessImplementation();

    public AddressBuilder setAddressBuilder(Randomness randomness){
        this.randomness = randomness;
        return this;
    }
}
