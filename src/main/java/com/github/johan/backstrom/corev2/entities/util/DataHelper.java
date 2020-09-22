package com.github.johan.backstrom.corev2.entities.util;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class DataHelper {

    private static Random random;
    private static long seed = 0;

    private static Random getRandom(){
        if(random == null){
            random = new Random(seed);
        }
        return random;
    }

    public static void setRandomSeed(long randomSeed){
        seed = randomSeed;
    }

    public static <T> T sometimesNull(T input, int percentageNull) {
        if (getRandomNumber(100) <= percentageNull) {
            return null;
        } else {
            return input;
        }

    }

    public static boolean sometimesTrue(int percentage) {
        return getRandomNumber(100) <= percentage;
    }

    public static String getRandomZeroPaddedNumber(int min, int max, int length) {

        String number = String.valueOf(getRandomNumber(min, max));
        int zeroesToAdd = length - number.length();

        return String.format("%s%s",
                Collections.nCopies(zeroesToAdd, "0").stream().collect(Collectors.joining("")),
                number
        );
    }

    public static Integer getRandomNumber(int max) {
        return getRandomNumber(0, max);
    }

    public static Integer getRandomNumber(int min, int max) {
        return (int) (getRandom().nextDouble() * (1 + max - min) + min);
    }

    public static int getRandomIndex(int length) {
        return getRandomNumber(length - 1);
    }
}
