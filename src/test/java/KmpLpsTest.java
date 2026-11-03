import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import search.KmpStringSearch;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the LPS array computation in KMP algorithm.
 */
public class KmpLpsTest {

    private final KmpStringSearch kmp = new KmpStringSearch();

    @Test
    @DisplayName("LPS reference string 1: AAACAAAA")
    void testLpsRef1() {
        int[] expected = {0, 1, 2, 0, 1, 2, 3, 3};
        assertArrayEquals(expected, kmp.lps("AAACAAAA"));
    }

    @Test
    @DisplayName("LPS reference string 2: ABABCABAB")
    void testLpsRef2() {
        int[] expected = {0, 0, 1, 2, 0, 1, 2, 3, 4};
        assertArrayEquals(expected, kmp.lps("ABABCABAB"));
    }

    @Test
    @DisplayName("LPS single character pattern")
    void testLpsSingleChar() {
        assertArrayEquals(new int[]{0}, kmp.lps("A"));
    }

    @Test
    @DisplayName("LPS repeating single character")
    void testLpsAllSame() {
        assertArrayEquals(new int[]{0, 1, 2, 3, 4}, kmp.lps("AAAAA"));
    }

    @Test
    @DisplayName("LPS with no proper prefix suffix matches")
    void testLpsNoPrefixSuffix() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, kmp.lps("ABCD"));
    }

    @Test
    @DisplayName("LPS empty pattern or null throws IllegalArgumentException")
    void testLpsEdgeCases() {
        assertThrows(IllegalArgumentException.class, () -> kmp.lps(""));
        assertThrows(IllegalArgumentException.class, () -> kmp.lps(null));
    }
}
