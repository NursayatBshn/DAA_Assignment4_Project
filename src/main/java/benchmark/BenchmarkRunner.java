package benchmark;

import editdistance.EditDistance;
import editdistance.RecursiveEditDistance;
import editdistance.TableEditDistance;
import editdistance.TwoRowEditDistance;
import search.KmpStringSearch;
import search.NaiveStringSearch;
import search.StringSearch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main benchmark runner for Assignment 4.
 * Measures wall-clock execution time (median of 5 runs) and operation counters.
 * Exports results to results/results.csv.
 */
public class BenchmarkRunner {

    private static final int REPETITIONS = 5;
    private static final String CSV_PATH = "results/results.csv";

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" Starting DAA Assignment 4 Empirical Benchmarks   ");
        System.out.println("==================================================");

        File resultsDir = new File("results");
        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_PATH))) {
            // Write CSV header
            writer.println("algorithm,input,n,m,time_ms,counter,extra_metric");

            // Warm up JVM
            warmUp();

            // 1. Edit Distance - Small sizes (comparing Recursive vs Table vs Two-Row)
            runSmallEditDistanceBenchmarks(writer);

            // 2. Edit Distance - Large sizes (comparing Table vs Two-Row scalability)
            runLargeEditDistanceBenchmarks(writer);

            // 3. String Search (comparing Naive vs KMP across input types)
            runStringSearchBenchmarks(writer);

            System.out.println("\nAll benchmarks completed successfully!");
            System.out.println("Results saved to " + CSV_PATH);
        } catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }

    private static void warmUp() {
        System.out.println("Warming up JVM JIT compiler...");
        DataGenerator gen = new DataGenerator(12345);
        TableEditDistance table = new TableEditDistance();
        KmpStringSearch kmp = new KmpStringSearch();

        for (int i = 0; i < 50; i++) {
            String s1 = gen.randomString(100);
            String s2 = gen.randomString(100);
            table.distance(s1, s2);
            kmp.search(s1, s2.substring(0, 5));
        }
    }

    private static void runSmallEditDistanceBenchmarks(PrintWriter writer) {
        System.out.println("\n--- 1. Edit Distance Small Sizes (Recursive vs Table vs TwoRow) ---");
        int[] smallSizes = {3, 6, 9, 12};
        String[] inputTypes = {"worst_case_disjoint", "random", "best_case_identical"};

        EditDistance[] algorithms = {
                new RecursiveEditDistance(),
                new TableEditDistance(),
                new TwoRowEditDistance()
        };

        DataGenerator gen = new DataGenerator(42);

        for (String inputType : inputTypes) {
            for (int n : smallSizes) {
                String a, b;
                switch (inputType) {
                    case "worst_case_disjoint":
                        a = gen.repeatedString('A', n);
                        b = gen.repeatedString('B', n);
                        break;
                    case "best_case_identical":
                        a = gen.repeatedString('A', n);
                        b = gen.repeatedString('A', n);
                        break;
                    case "random":
                    default:
                        a = gen.randomString(n);
                        b = gen.randomString(n);
                        break;
                }

                for (EditDistance algo : algorithms) {
                    double[] times = new double[REPETITIONS];
                    long counter = 0;
                    int resultDist = 0;

                    for (int r = 0; r < REPETITIONS; r++) {
                        algo.resetCounter();
                        long start = System.nanoTime();
                        resultDist = algo.distance(a, b);
                        long end = System.nanoTime();
                        times[r] = (end - start) / 1_000_000.0;
                        counter = algo.getOperationsCount();
                    }

                    Arrays.sort(times);
                    double medianTime = times[REPETITIONS / 2];

                    String algoName = algo.getClass().getSimpleName();
                    writer.printf("%s,%s,%d,%d,%.4f,%d,%d%n",
                            algoName, inputType, n, n, medianTime, counter, resultDist);

                    System.out.printf("[%s] input=%-20s n=%2d | time=%8.4f ms | counter=%d%n",
                            algoName, inputType, n, medianTime, counter);
                }
            }
        }
    }

    private static void runLargeEditDistanceBenchmarks(PrintWriter writer) {
        System.out.println("\n--- 2. Edit Distance Large Sizes (Table vs TwoRow Scalability) ---");
        int[] largeSizes = {500, 1000, 2000, 4000};
        String[] inputTypes = {"random", "worst_case_disjoint"};

        EditDistance[] algorithms = {
                new TableEditDistance(),
                new TwoRowEditDistance()
        };

        DataGenerator gen = new DataGenerator(42);

        for (String inputType : inputTypes) {
            for (int n : largeSizes) {
                String a, b;
                if ("worst_case_disjoint".equals(inputType)) {
                    a = gen.repeatedString('A', n);
                    b = gen.repeatedString('B', n);
                } else {
                    a = gen.randomString(n);
                    b = gen.randomString(n);
                }

                for (EditDistance algo : algorithms) {
                    double[] times = new double[REPETITIONS];
                    long counter = 0;
                    int resultDist = 0;

                    for (int r = 0; r < REPETITIONS; r++) {
                        algo.resetCounter();
                        long start = System.nanoTime();
                        resultDist = algo.distance(a, b);
                        long end = System.nanoTime();
                        times[r] = (end - start) / 1_000_000.0;
                        counter = algo.getOperationsCount();
                    }

                    Arrays.sort(times);
                    double medianTime = times[REPETITIONS / 2];

                    String algoName = algo.getClass().getSimpleName();
                    writer.printf("%s,%s,%d,%d,%.4f,%d,%d%n",
                            algoName, inputType, n, n, medianTime, counter, resultDist);

                    System.out.printf("[%s] input=%-20s n=%4d | time=%8.3f ms | counter=%d%n",
                            algoName, inputType, n, medianTime, counter);
                }
            }
        }
    }

    private static void runStringSearchBenchmarks(PrintWriter writer) {
        System.out.println("\n--- 3. String Search Benchmarks (Naive vs KMP) ---");
        int[] textSizes = {10000, 25000, 50000, 100000};
        int patternSize = 200;
        String[] inputTypes = {"worst_case_repetitive", "random", "periodic_overlap"};

        StringSearch[] algorithms = {
                new NaiveStringSearch(),
                new KmpStringSearch()
        };

        DataGenerator gen = new DataGenerator(42);

        for (String inputType : inputTypes) {
            for (int n : textSizes) {
                String text, pattern;
                switch (inputType) {
                    case "worst_case_repetitive":
                        text = gen.worstCaseSearchText(n);
                        pattern = gen.worstCaseSearchPattern(patternSize);
                        break;
                    case "periodic_overlap":
                        text = gen.periodicText(n);
                        pattern = gen.periodicPattern(patternSize);
                        break;
                    case "random":
                    default:
                        text = gen.randomString(n);
                        pattern = gen.randomString(patternSize);
                        break;
                }

                for (StringSearch algo : algorithms) {
                    double[] times = new double[REPETITIONS];
                    long comparisons = 0;
                    int matchesCount = 0;

                    for (int r = 0; r < REPETITIONS; r++) {
                        algo.resetCounter();
                        long start = System.nanoTime();
                        List<Integer> matches = algo.search(text, pattern);
                        long end = System.nanoTime();
                        times[r] = (end - start) / 1_000_000.0;
                        comparisons = algo.getComparisons();
                        matchesCount = matches.size();
                    }

                    Arrays.sort(times);
                    double medianTime = times[REPETITIONS / 2];

                    String algoName = algo.getClass().getSimpleName();
                    writer.printf("%s,%s,%d,%d,%.4f,%d,%d%n",
                            algoName, inputType, n, patternSize, medianTime, comparisons, matchesCount);

                    System.out.printf("[%s] input=%-22s n=%6d m=%3d | time=%7.3f ms | comparisons=%8d (matches=%d)%n",
                            algoName, inputType, n, patternSize, medianTime, comparisons, matchesCount);
                }
            }
        }
    }
}
