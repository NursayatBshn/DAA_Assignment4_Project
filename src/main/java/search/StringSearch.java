package search;

import java.util.List;

/**
 * Common interface for String Search algorithms.
 */
public interface StringSearch {
    /**
     * Finds all start indices of occurrences of pattern in text, in increasing order,
     * including overlapping matches.
     *
     * @param text the text to search in
     * @param pattern the pattern to find
     * @return list of 0-based start indices
     */
    List<Integer> search(String text, String pattern);

    /**
     * Returns the number of character comparisons performed during the last search.
     *
     * @return comparison count
     */
    long getComparisons();

    /**
     * Resets the comparison counter.
     */
    void resetCounter();
}
