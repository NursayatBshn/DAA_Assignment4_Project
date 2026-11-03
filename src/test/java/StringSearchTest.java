import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import search.KmpStringSearch;
import search.NaiveStringSearch;
import search.StringSearch;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for string search algorithms (Naive & KMP).
 */
public class StringSearchTest {

    private final StringSearch naive = new NaiveStringSearch();
    private final StringSearch kmp = new KmpStringSearch();
    private final StringSearch[] algorithms = new StringSearch[]{naive, kmp};

    @Test
    @DisplayName("Edge Case: Empty pattern throws IllegalArgumentException")
    void testEmptyPatternThrows() {
        for (StringSearch s : algorithms) {
            assertThrows(IllegalArgumentException.class, () -> s.search("hello", ""));
            assertThrows(IllegalArgumentException.class, () -> s.search("hello", null));
        }
    }

    @Test
    @DisplayName("Edge Case: Null text throws IllegalArgumentException")
    void testNullTextThrows() {
        for (StringSearch s : algorithms) {
            assertThrows(IllegalArgumentException.class, () -> s.search(null, "pattern"));
        }
    }

    @Test
    @DisplayName("Edge Case: Pattern longer than text returns empty list")
    void testPatternLongerThanText() {
        for (StringSearch s : algorithms) {
            List<Integer> result = s.search("abc", "abcdef");
            assertEquals(Collections.emptyList(), result);
        }
    }

    @Test
    @DisplayName("Edge Case: Empty text with non-empty pattern returns empty list")
    void testEmptyText() {
        for (StringSearch s : algorithms) {
            List<Integer> result = s.search("", "a");
            assertEquals(Collections.emptyList(), result);
        }
    }

    @Test
    @DisplayName("No match returns empty list")
    void testNoMatch() {
        for (StringSearch s : algorithms) {
            List<Integer> result = s.search("hello world", "xyz");
            assertTrue(result.isEmpty());
        }
    }

    @Test
    @DisplayName("Single character matches and counts")
    void testSingleCharacterMatch() {
        for (StringSearch s : algorithms) {
            List<Integer> result = s.search("banana", "a");
            assertEquals(List.of(1, 3, 5), result);
        }
    }

    @Test
    @DisplayName("Overlapping matches in periodic string")
    void testOverlappingMatches() {
        for (StringSearch s : algorithms) {
            List<Integer> result = s.search("AAAAAA", "AAA");
            assertEquals(List.of(0, 1, 2, 3), result);
        }
    }

    @Test
    @DisplayName("Comparison counter is positive during search")
    void testComparisonCounters() {
        for (StringSearch s : algorithms) {
            s.resetCounter();
            s.search("ababcabab", "ab");
            assertTrue(s.getComparisons() > 0, "Comparisons must be > 0");
        }
    }

    @Test
    @DisplayName("Cross-check: 100 random texts give identical occurrences between Naive and KMP")
    void test100RandomTextsAgreement() {
        Random rng = new Random(42);
        String chars = "abcd";

        for (int t = 0; t < 100; t++) {
            int textLen = rng.nextInt(200) + 1; // 1 to 200
            int patLen = rng.nextInt(6) + 1;    // 1 to 6

            StringBuilder sbText = new StringBuilder();
            for (int i = 0; i < textLen; i++) {
                sbText.append(chars.charAt(rng.nextInt(chars.length())));
            }

            StringBuilder sbPat = new StringBuilder();
            for (int i = 0; i < patLen; i++) {
                sbPat.append(chars.charAt(rng.nextInt(chars.length())));
            }

            String text = sbText.toString();
            String pat = sbPat.toString();

            List<Integer> naiveMatches = naive.search(text, pat);
            List<Integer> kmpMatches = kmp.search(text, pat);

            assertEquals(naiveMatches, kmpMatches,
                    "Mismatch on text='" + text + "', pattern='" + pat + "'");
        }
    }
}
