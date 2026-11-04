package benchmark;

import java.util.Random;

/**
 * Deterministic data generator for Edit Distance and String Search benchmarks.
 * Uses a fixed seed to ensure 100% reproducible benchmarks.
 */
public class DataGenerator {
    private final Random rng;

    public DataGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generates a random lowercase ASCII string of the given length.
     */
    public String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + rng.nextInt(26)));
        }
        return sb.toString();
    }

    /**
     * Generates a repeated character string: e.g. "AAAA...A".
     */
    public String repeatedString(char c, int length) {
        return String.valueOf(c).repeat(Math.max(0, length));
    }

    /**
     * Generates worst-case repetitive text for Naive search: "AAAA...A".
     */
    public String worstCaseSearchText(int n) {
        return repeatedString('A', n);
    }

    /**
     * Generates worst-case repetitive pattern for Naive search: "AAAA...B" of length m.
     */
    public String worstCaseSearchPattern(int m) {
        if (m <= 1) return "B";
        return repeatedString('A', m - 1) + "B";
    }

    /**
     * Generates periodic alternating text: "ABABAB...".
     */
    public String periodicText(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append((i % 2 == 0) ? 'A' : 'B');
        }
        return sb.toString();
    }

    /**
     * Generates periodic pattern: e.g. "ABAB".
     */
    public String periodicPattern(int m) {
        return periodicText(m);
    }
}
