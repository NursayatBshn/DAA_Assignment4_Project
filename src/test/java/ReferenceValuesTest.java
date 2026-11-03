import editdistance.EditDistance;
import editdistance.RecursiveEditDistance;
import editdistance.TableEditDistance;
import editdistance.TwoRowEditDistance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import search.KmpStringSearch;
import search.NaiveStringSearch;
import search.StringSearch;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the exact reference values specified in Section 7 of Assignment 4.
 */
public class ReferenceValuesTest {

    private final EditDistance[] editDistanceAlgorithms = new EditDistance[]{
            new RecursiveEditDistance(),
            new TableEditDistance(),
            new TwoRowEditDistance()
    };

    private final StringSearch[] searchAlgorithms = new StringSearch[]{
            new NaiveStringSearch(),
            new KmpStringSearch()
    };

    @Test
    @DisplayName("Section 7: distance(\"kitten\", \"sitting\") == 3")
    void testKittenSitting() {
        for (EditDistance ed : editDistanceAlgorithms) {
            assertEquals(3, ed.distance("kitten", "sitting"), ed.getClass().getSimpleName() + " failed");
        }
    }

    @Test
    @DisplayName("Section 7: distance(\"flaw\", \"lawn\") == 2")
    void testFlawLawn() {
        for (EditDistance ed : editDistanceAlgorithms) {
            assertEquals(2, ed.distance("flaw", "lawn"), ed.getClass().getSimpleName() + " failed");
        }
    }

    @Test
    @DisplayName("Section 7: distance(\"intention\", \"execution\") == 5")
    void testIntentionExecution() {
        for (EditDistance ed : editDistanceAlgorithms) {
            assertEquals(5, ed.distance("intention", "execution"), ed.getClass().getSimpleName() + " failed");
        }
    }

    @Test
    @DisplayName("Section 7: distance(\"\", \"abc\") == 3 and distance(\"abc\", \"abc\") == 0")
    void testEmptyAndIdentical() {
        for (EditDistance ed : editDistanceAlgorithms) {
            assertEquals(3, ed.distance("", "abc"), ed.getClass().getSimpleName() + " failed on empty source");
            assertEquals(0, ed.distance("abc", "abc"), ed.getClass().getSimpleName() + " failed on identical strings");
        }
    }

    @Test
    @DisplayName("Section 7: lps(\"AAACAAAA\") == [0, 1, 2, 0, 1, 2, 3, 3]")
    void testLpsReference1() {
        KmpStringSearch kmp = new KmpStringSearch();
        int[] expected = new int[]{0, 1, 2, 0, 1, 2, 3, 3};
        assertArrayEquals(expected, kmp.lps("AAACAAAA"));
    }

    @Test
    @DisplayName("Section 7: lps(\"ABABCABAB\") == [0, 0, 1, 2, 0, 1, 2, 3, 4]")
    void testLpsReference2() {
        KmpStringSearch kmp = new KmpStringSearch();
        int[] expected = new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4};
        assertArrayEquals(expected, kmp.lps("ABABCABAB"));
    }

    @Test
    @DisplayName("Section 7: search(\"ABABDABACDABABCABAB\", \"ABABCABAB\") == [10]")
    void testSearchReference1() {
        for (StringSearch searcher : searchAlgorithms) {
            List<Integer> matches = searcher.search("ABABDABACDABABCABAB", "ABABCABAB");
            assertEquals(List.of(10), matches, searcher.getClass().getSimpleName() + " failed");
        }
    }

    @Test
    @DisplayName("Section 7: search(\"AAAAA\", \"AA\") == [0, 1, 2, 3]")
    void testSearchReference2Overlapping() {
        for (StringSearch searcher : searchAlgorithms) {
            List<Integer> matches = searcher.search("AAAAA", "AA");
            assertEquals(List.of(0, 1, 2, 3), matches, searcher.getClass().getSimpleName() + " failed");
        }
    }

    @Test
    @DisplayName("Section 7: search(\"ABABABA\", \"ABA\") == [0, 2, 4]")
    void testSearchReference3Overlapping() {
        for (StringSearch searcher : searchAlgorithms) {
            List<Integer> matches = searcher.search("ABABABA", "ABA");
            assertEquals(List.of(0, 2, 4), matches, searcher.getClass().getSimpleName() + " failed");
        }
    }
}
