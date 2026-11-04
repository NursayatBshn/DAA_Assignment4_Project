import benchmark.BenchmarkRunner;
import editdistance.RecursiveEditDistance;
import editdistance.TableEditDistance;
import editdistance.TwoRowEditDistance;
import search.KmpStringSearch;
import search.NaiveStringSearch;

import java.util.Arrays;

/**
 * Main application entry point for DAA Assignment 4.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("DAA Assignment 4: Dynamic Programming & String Algorithms");
        System.out.println("Author: Nursayat Bashan");
        System.out.println("---------------------------------------------------------");

        // Quick Demonstration of Reference Values
        RecursiveEditDistance rec = new RecursiveEditDistance();
        TableEditDistance table = new TableEditDistance();
        TwoRowEditDistance twoRow = new TwoRowEditDistance();
        NaiveStringSearch naive = new NaiveStringSearch();
        KmpStringSearch kmp = new KmpStringSearch();

        System.out.println("1. Edit Distance Check:");
        System.out.println("distance(\"kitten\", \"sitting\") = " + table.distance("kitten", "sitting"));
        System.out.println("distance(\"flaw\", \"lawn\") = " + twoRow.distance("flaw", "lawn"));
        System.out.println("distance(\"intention\", \"execution\") = " + rec.distance("intention", "execution"));

        System.out.println("\n2. LPS Check:");
        System.out.println("lps(\"AAACAAAA\") = " + Arrays.toString(kmp.lps("AAACAAAA")));
        System.out.println("lps(\"ABABCABAB\") = " + Arrays.toString(kmp.lps("ABABCABAB")));

        System.out.println("\n3. String Search Check:");
        System.out.println("search(\"ABABDABACDABABCABAB\", \"ABABCABAB\") = " +
                kmp.search("ABABDABACDABABCABAB", "ABABCABAB"));
        System.out.println("search(\"AAAAA\", \"AA\") [overlapping] = " +
                naive.search("AAAAA", "AA"));

        System.out.println("\nRunning full benchmark suite...");
        BenchmarkRunner.main(args);
    }
}
