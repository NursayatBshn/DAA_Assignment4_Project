import editdistance.EditDistance;
import editdistance.RecursiveEditDistance;
import editdistance.TableEditDistance;
import editdistance.TwoRowEditDistance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the Edit Distance family:
 * - RecursiveEditDistance
 * - TableEditDistance
 * - TwoRowEditDistance
 */
public class EditDistanceTest {

    private final EditDistance recursive = new RecursiveEditDistance();
    private final EditDistance table = new TableEditDistance();
    private final EditDistance twoRow = new TwoRowEditDistance();

    private final EditDistance[] allVersions = new EditDistance[]{recursive, table, twoRow};

    @Test
    @DisplayName("Edge Case: Empty strings")
    void testEmptyStrings() {
        for (EditDistance ed : allVersions) {
            assertEquals(0, ed.distance("", ""), ed.getClass().getSimpleName() + " failed on ('', '')");
            assertEquals(5, ed.distance("hello", ""), ed.getClass().getSimpleName() + " failed on ('hello', '')");
            assertEquals(5, ed.distance("", "world"), ed.getClass().getSimpleName() + " failed on ('', 'world')");
        }
    }

    @Test
    @DisplayName("Edge Case: Null arguments throw IllegalArgumentException")
    void testNullInputs() {
        for (EditDistance ed : allVersions) {
            assertThrows(IllegalArgumentException.class, () -> ed.distance(null, "abc"));
            assertThrows(IllegalArgumentException.class, () -> ed.distance("abc", null));
            assertThrows(IllegalArgumentException.class, () -> ed.distance(null, null));
        }
    }

    @Test
    @DisplayName("Identical strings return distance 0")
    void testIdenticalStrings() {
        for (EditDistance ed : allVersions) {
            assertEquals(0, ed.distance("almaty", "almaty"));
            assertEquals(0, ed.distance("astana", "astana"));
        }
    }

    @Test
    @DisplayName("Completely different strings")
    void testCompletelyDifferentStrings() {
        for (EditDistance ed : allVersions) {
            assertEquals(4, ed.distance("aaaa", "bbbb"));
        }
    }

    @Test
    @DisplayName("Single character operations")
    void testSingleCharacterOperations() {
        for (EditDistance ed : allVersions) {
            assertEquals(1, ed.distance("a", "b")); // replace
            assertEquals(1, ed.distance("a", ""));  // delete
            assertEquals(1, ed.distance("", "a"));  // insert
            assertEquals(0, ed.distance("a", "a")); // match
        }
    }

    @Test
    @DisplayName("Operation counters are positive and reset properly")
    void testOperationCounters() {
        for (EditDistance ed : allVersions) {
            ed.resetCounter();
            int d = ed.distance("kitten", "sitting");
            assertEquals(3, d);
            long count = ed.getOperationsCount();
            org.junit.jupiter.api.Assertions.assertTrue(count > 0, "Counter should be positive");

            ed.resetCounter();
            assertEquals(0, ed.getOperationsCount());
        }
    }

    @Test
    @DisplayName("Cross-check: 100 random short strings give identical results across all 3 versions")
    void test100RandomShortStringsAgreement() {
        Random rng = new Random(42);
        String chars = "abcdef";

        for (int t = 0; t < 100; t++) {
            int lenA = rng.nextInt(9); // lengths 0 to 8 (safe for recursion)
            int lenB = rng.nextInt(9);

            StringBuilder sbA = new StringBuilder();
            for (int i = 0; i < lenA; i++) {
                sbA.append(chars.charAt(rng.nextInt(chars.length())));
            }
            StringBuilder sbB = new StringBuilder();
            for (int i = 0; i < lenB; i++) {
                sbB.append(chars.charAt(rng.nextInt(chars.length())));
            }

            String s1 = sbA.toString();
            String s2 = sbB.toString();

            int distRecursive = recursive.distance(s1, s2);
            int distTable = table.distance(s1, s2);
            int distTwoRow = twoRow.distance(s1, s2);

            assertEquals(distRecursive, distTable, "Mismatch between Recursive and Table on: " + s1 + ", " + s2);
            assertEquals(distRecursive, distTwoRow, "Mismatch between Recursive and TwoRow on: " + s1 + ", " + s2);
        }
    }
}
