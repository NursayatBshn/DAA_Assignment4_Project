package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Naive string search algorithm.
 * Tries every start position and compares character by character.
 */
public class NaiveStringSearch implements StringSearch {
    private long comparisons = 0;

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        comparisons = 0;
        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return Collections.emptyList();
        }

        List<Integer> matches = new ArrayList<>();
        for (int i = 0; i <= n - m; i++) {
            boolean match = true;
            for (int j = 0; j < m; j++) {
                comparisons++; // Character comparison
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if (match) {
                matches.add(i);
            }
        }
        return matches;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public void resetCounter() {
        comparisons = 0;
    }
}
