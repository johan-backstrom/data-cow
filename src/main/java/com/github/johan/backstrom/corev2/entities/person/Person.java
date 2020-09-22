package com.github.johan.backstrom.corev2.entities.person;

import com.github.johan.backstrom.corev2.Attribute;
import com.github.johan.backstrom.corev2.WithGenerators;
import com.github.johan.backstrom.corev2.entities.address.Address;
import com.github.johan.backstrom.corev2.entities.finance.CreditCard;

import java.time.LocalDate;
import java.util.List;

@WithGenerators(PersonGenerators.class)
public class Person {

    @Attribute("sex")
    private Sex sex;

    @Attribute("givenName")
    private String givenName;

    @Attribute("lastName")
    private String lastName;

    @Attribute("phoneNumber")
    private String phoneNumber;

    @Attribute("dateOfBirth")
    private LocalDate dateOfBirth;

    @Attribute("creditCards")
    private List<CreditCard> creditCards;

    @Attribute("address")
    private Address address;

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}