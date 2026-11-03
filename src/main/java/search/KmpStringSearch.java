package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Knuth-Morris-Pratt (KMP) string search algorithm.
 * Builds LPS array and searches text in linear time without moving backwards in text.
 */
public class KmpStringSearch implements StringSearch {
    private long comparisons = 0;
    private long lpsComparisons = 0;

    /**
     * Builds and returns the LPS (Longest Proper Prefix which is also Suffix) array.
     * Must be public so tests can call it.
     *
     * @param p pattern string
     * @return LPS array
     */
    public int[] lps(String p) {
        if (p == null || p.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        int m = p.length();
        int[] lps = new int[m];
        lps[0] = 0;
        int len = 0;
        int i = 1;

        while (i < m) {
            lpsComparisons++;
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }

        comparisons = 0;
        lpsComparisons = 0;
        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return Collections.emptyList();
        }

        int[] lps = lps(pattern);
        List<Integer> matches = new ArrayList<>();
        int i = 0; // Text index (never moves backwards)
        int j = 0; // Pattern index

        while (i < n) {
            comparisons++; // Character comparison between text and pattern
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    matches.add(i - j); // Match found
                    j = lps[j - 1];      // Look for overlapping occurrences
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1]; // Shift pattern using LPS
                } else {
                    i++;            // Advance text index
                }
            }
        }

        return matches;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    public long getLpsComparisons() {
        return lpsComparisons;
    }

    public long getTotalComparisons() {
        return comparisons + lpsComparisons;
    }

    @Override
    public void resetCounter() {
        comparisons = 0;
        lpsComparisons = 0;
    }
}
