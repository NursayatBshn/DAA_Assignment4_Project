# DAA Assignment 4: Dynamic Programming and String Algorithms

## Overview
This project implements and evaluates fundamental Dynamic Programming and String Search algorithms for a city search and spell-check platform:
* **Edit Distance (Levenshtein Distance):**
  * **Recursive:** Pure unmemoized recursion on suffixes ($O(3^n)$ worst-case time).
  * **Table DP:** Bottom-up 2D dynamic programming table ($O(n \cdot m)$ time, $O(n \cdot m)$ space).
  * **Two-Row DP:** Memory-optimized dynamic programming retaining only two rows ($O(n \cdot m)$ time, $O(m)$ space).
* **String Search:**
  * **Naive Search:** Sliding window character-by-character search ($O(n \cdot m)$ worst-case time).
  * **KMP (Knuth-Morris-Pratt):** Precomputes Longest Proper Prefix which is also Suffix (`lps`) array and searches text in linear time without moving backwards ($O(n + m)$ time, $O(m)$ space).

---

## Build and Run Instructions

Ensure you have **Java 21+**, **Maven 3.8+**, and **Python 3** installed.

### 1. Build the Project
```bash
mvn clean compile
```

### 2. Run Unit Tests (JUnit 5)
Executes all unit tests, including reference values from Section 7, edge cases (empty strings, null arguments, pattern longer than text), and 100 random cross-checks:
```bash
mvn test
```

### 3. Run Benchmarks
Runs empirical benchmarks using a deterministic seed (`42`), measuring operation counters and median wall-clock times over 5 runs. Results are exported to `results/results.csv`:
```bash
java -Xmx4g -cp target/classes benchmark.BenchmarkRunner
```
*(Or run the interactive demonstration: `java -cp target/classes Main`)*

### 4. Generate Plots
Generates the 4 analytical charts saved to `results/plots/`:
```bash
python3 generate_plots.py
```

---

## Repository Structure
```
.
├── pom.xml                                    # Maven configuration with JUnit 5
├── README.md                                  # Build and execution documentation
├── REPORT.md                                  # Complete experimental report and answers
├── generate_plots.py                          # Matplotlib plotting script
├── src
│   ├── main/java
│   │   ├── Main.java                          # Main demonstration and runner launcher
│   │   ├── benchmark
│   │   │   ├── BenchmarkRunner.java           # Automated benchmark suite (exports results.csv)
│   │   │   └── DataGenerator.java             # Deterministic test input generator (seed 42)
│   │   ├── editdistance
│   │   │   ├── EditDistance.java              # Common interface
│   │   │   ├── RecursiveEditDistance.java     # Recursive version with DP comments
│   │   │   ├── TableEditDistance.java         # 2D Table DP version with DP comments
│   │   │   └── TwoRowEditDistance.java        # Two-Row DP version with DP comments
│   │   └── search
│   │       ├── StringSearch.java              # Common interface
│   │       ├── NaiveStringSearch.java         # Naive search
│   │       └── KmpStringSearch.java           # KMP search with public lps()
│   └── test/java
│       ├── EditDistanceTest.java              # Edit Distance tests and 100-run cross-checks
│       ├── KmpLpsTest.java                    # LPS array correctness tests
│       ├── ReferenceValuesTest.java           # Section 7 exact reference values
│       └── StringSearchTest.java              # Search tests, edge cases, and 100-run cross-checks
└── results
    ├── results.csv                            # Empirical benchmark measurements
    └── plots                                  # Generated PNG charts
        ├── plot1_edit_distance_operations.png
        ├── plot2_dp_scalability_time.png
        ├── plot3_search_comparisons_worst_case.png
        └── plot4_search_ratio_comparisons.png
```

