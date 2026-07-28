package com.cscb07.taamapp.testutil;

/**
 * Class for generating a random number using a deterministic, Java version invariant algorithm.
 * Not intended for practical use, just for getting some random value for key naming in tests.
 */
public final class KnownRandom {
    private KnownRandom() {}
    public static int nextInt(int seed) {
        // xor-shift inspired algorithm.
        if (seed == 0) {
            throw new IllegalArgumentException("do not use 0 as seed.");
        }
        final int byteFilled1 = 255;
        int result = 0;
        result += (seed ^ byteFilled1) << (3 * 8);
        result += ((seed >> (1 * 8))^ byteFilled1) << (2 * 8);
        result += ((seed >> (2 * 8))^ byteFilled1) << (1 * 8);
        result += (seed >> (3 * 8)) ^ byteFilled1;

        result ^= result << 9;
        result ^= -1;
        result ^= result >> 5;
        result = (result * seed) ^ result;

        return result;
    }
}
