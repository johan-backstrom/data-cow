package com.github.johan.backstrom.entities.person;

public enum Gender {
    Male,
    Female;

    public static Gender getRandomGender(){
        return Gender.values()[(int)(Math.random() * Gender.values().length)];
    }
}
