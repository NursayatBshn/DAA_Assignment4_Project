package editdistance;

/**
 * Common interface for Edit Distance algorithms.
 */
public interface EditDistance {
    /**
     * Computes the minimum number of insertions, deletions, and replacements
     * to transform string a into string b.
     *
     * @param a source string
     * @param b target string
     * @return minimum edit distance
     */
    int distance(String a, String b);

    /**
     * Returns the number of operations or subproblems computed during the last execution.
     *
     * @return operations count
     */
    long getOperationsCount();

    /**
     * Resets the operation counter to zero.
     */
    void resetCounter();
}
