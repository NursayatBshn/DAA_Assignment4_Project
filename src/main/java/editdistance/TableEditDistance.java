package editdistance;

/**
 * Bottom-up 2D table Dynamic Programming implementation of Edit Distance.
 *
 * DP Design:
 * State: dp[i][j] is the minimum edit distance between prefix a[0..i-1] and prefix b[0..j-1].
 * Transition: if a[i-1] == b[j-1], dp[i][j] = dp[i-1][j-1];
 *             else dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) [delete, insert, replace].
 * Base cases: dp[i][0] = i for all 0 <= i <= n; dp[0][j] = j for all 0 <= j <= m.
 */
public class TableEditDistance implements EditDistance {
    private long operationsCount = 0;

    @Override
    public int distance(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Input strings cannot be null");
        }
        operationsCount = 0;
        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];

        // Base cases
        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        // Fill table bottom-up
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                operationsCount++; // Count each cell computation
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j],
                                   Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }

        return dp[n][m];
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
