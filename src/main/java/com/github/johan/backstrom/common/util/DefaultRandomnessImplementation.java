package com.github.johan.backstrom.common.util;

import java.util.Random;

public class DefaultRandomnessImplementation implements Randomness {

    private static Random random;

    // Use the seed to generate consistent data when fetching by Id (i.e. use the Id as seed)
    public DefaultRandomnessImplementation(long seed) {
        if (random == null) {
            random = new Random(seed);
        }
        random.setSeed(seed);
    }

    public DefaultRandomnessImplementation() {
        this(0);
    }

    @Override
    public int getRandomInteger(int lowerBound, int upperBound) {
        return (int) (random.nextDouble() * (1 + upperBound - lowerBound) + lowerBound);
    }
}
