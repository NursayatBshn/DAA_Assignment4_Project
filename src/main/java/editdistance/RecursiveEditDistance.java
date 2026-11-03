package editdistance;

/**
 * Recursive implementation of Edit Distance without memoization.
 *
 * DP Design:
 * State: dp(i, j) is the minimum edit distance between prefix a[0..i-1] and prefix b[0..j-1].
 * Transition: if a[i-1] == b[j-1], dp(i, j) = dp(i-1, j-1);
 *             else dp(i, j) = 1 + min(dp(i-1, j), dp(i, j-1), dp(i-1, j-1)) [delete, insert, replace].
 * Base case: dp(i, 0) = i (delete i chars), dp(0, j) = j (insert j chars).
 */
public class RecursiveEditDistance implements EditDistance {
    private long operationsCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings cannot be null");
        }
        operationsCount = 0;
        return compute(a, b, a.length(), b.length());
    }

    private int compute(String a, String b, int i, int j) {
        operationsCount++; // Count every recursive call

        // Base cases
        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }

        // Transition
        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return compute(a, b, i - 1, j - 1);
        }

        int deleteCost = compute(a, b, i - 1, j);
        int insertCost = compute(a, b, i, j - 1);
        int replaceCost = compute(a, b, i - 1, j - 1);

        return 1 + Math.min(deleteCost, Math.min(insertCost, replaceCost));
    }

    @Override
    public long getOperationsCount() {
        return operationsCount;
    }

    @Override
    public void resetCounter() {
        operationsCount = 0;
    }
}
