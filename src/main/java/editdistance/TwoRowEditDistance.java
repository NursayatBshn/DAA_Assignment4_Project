package editdistance;

/**
 * Memory-optimized two-row Dynamic Programming implementation of Edit Distance.
 *
 * DP Design:
 * State: prev[j] is dp[i-1][j] and curr[j] is dp[i][j] for current prefixes a[0..i-1] and b[0..j-1].
 * Transition: if a[i-1] == b[j-1], curr[j] = prev[j-1];
 *             else curr[j] = 1 + min(prev[j], curr[j-1], prev[j-1]) [delete, insert, replace].
 * Base cases: prev[j] = j for all 0 <= j <= m (row 0); curr[0] = i at each row step.
 */
public class TwoRowEditDistance implements EditDistance {
    private long operationsCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings cannot be null");
        }
        operationsCount = 0;
        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        // Base case: row 0
        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        // Fill row by row
        for (int i = 1; i <= n; i++) {
            curr[0] = i; // Base case for column 0 of row i
            for (int j = 1; j <= m; j++) {
                operationsCount++; // Count each cell computation
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(prev[j],
                                  Math.min(curr[j - 1], prev[j - 1]));
                }
            }
            // Swap rows
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[m];
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
