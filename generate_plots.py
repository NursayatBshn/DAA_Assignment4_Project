import csv
import os
import matplotlib.pyplot as plt

os.makedirs("results/plots", exist_ok=True)

# Read results.csv
data = []
with open("results/results.csv", mode="r") as f:
    reader = csv.DictReader(f)
    for row in reader:
        row["n"] = int(row["n"])
        row["m"] = int(row["m"])
        row["time_ms"] = float(row["time_ms"])
        row["counter"] = int(row["counter"])
        row["extra_metric"] = int(row["extra_metric"])
        data.append(row)

# Set common plotting style
plt.style.use('seaborn-v0_8-whitegrid' if 'seaborn-v0_8-whitegrid' in plt.style.available else 'default')
plt.rcParams['font.sans-serif'] = 'DejaVu Sans'
plt.rcParams['font.size'] = 10
plt.rcParams['axes.titlesize'] = 12
plt.rcParams['axes.labelsize'] = 11

# =====================================================================
# Plot 1: Edit Distance Operations vs n (Log Scale)
# =====================================================================
fig, ax = plt.subplots(figsize=(8, 5.5))

rec_disjoint = sorted([r for r in data if r["algorithm"] == "RecursiveEditDistance" and r["input"] == "worst_case_disjoint"], key=lambda x: x["n"])
rec_random = sorted([r for r in data if r["algorithm"] == "RecursiveEditDistance" and r["input"] == "random"], key=lambda x: x["n"])
rec_ident = sorted([r for r in data if r["algorithm"] == "RecursiveEditDistance" and r["input"] == "best_case_identical"], key=lambda x: x["n"])
table_small = sorted([r for r in data if r["algorithm"] == "TableEditDistance" and r["input"] == "worst_case_disjoint" and r["n"] <= 12], key=lambda x: x["n"])
tworow_small = sorted([r for r in data if r["algorithm"] == "TwoRowEditDistance" and r["input"] == "worst_case_disjoint" and r["n"] <= 12], key=lambda x: x["n"])

ax.plot([r["n"] for r in rec_disjoint], [r["counter"] for r in rec_disjoint], 'ro-', label='Recursive (Worst-Case Disjoint $O(3^n)$)', linewidth=2)
ax.plot([r["n"] for r in rec_random], [r["counter"] for r in rec_random], 'mo--', label='Recursive (Random Strings)', linewidth=1.5)
ax.plot([r["n"] for r in rec_ident], [r["counter"] for r in rec_ident], 'go-.', label='Recursive (Identical Best-Case $O(n)$)', linewidth=1.5)
ax.plot([r["n"] for r in table_small], [r["counter"] for r in table_small], 'bs-', label='Table DP ($O(n^2)$)', linewidth=2)
ax.plot([r["n"] for r in tworow_small], [r["counter"] for r in tworow_small], 'c^:', label='Two-Row DP ($O(n^2)$)', linewidth=2)

ax.set_yscale('log')
ax.set_title("Plot 1: Edit Distance Operations vs Input Size n (Log Scale)")
ax.set_xlabel("String Length n (characters)")
ax.set_ylabel("Operations / Subproblem Calls (log scale)")
ax.set_xticks([3, 6, 9, 12])
ax.grid(True, which="both", ls="--", alpha=0.6)
ax.legend(loc='upper left', frameon=True)
fig.text(0.5, 0.01, "Takeaway: Recursive Edit Distance exhibits exponential O(3^n) growth on disjoint inputs, whereas DP maintains strict O(n^2) scaling.", ha='center', fontsize=9, style='italic')

plt.tight_layout(rect=[0, 0.04, 1, 1])
plot1_path = "results/plots/plot1_edit_distance_operations.png"
plt.savefig(plot1_path, dpi=300)
plt.close()
print("Saved", plot1_path)

# =====================================================================
# Plot 2: Large DP Scalability: Time vs n
# =====================================================================
fig, ax = plt.subplots(figsize=(8, 5.5))

table_large = sorted([r for r in data if r["algorithm"] == "TableEditDistance" and r["input"] == "random" and r["n"] >= 500], key=lambda x: x["n"])
tworow_large = sorted([r for r in data if r["algorithm"] == "TwoRowEditDistance" and r["input"] == "random" and r["n"] >= 500], key=lambda x: x["n"])
table_disjoint = sorted([r for r in data if r["algorithm"] == "TableEditDistance" and r["input"] == "worst_case_disjoint" and r["n"] >= 500], key=lambda x: x["n"])
tworow_disjoint = sorted([r for r in data if r["algorithm"] == "TwoRowEditDistance" and r["input"] == "worst_case_disjoint" and r["n"] >= 500], key=lambda x: x["n"])

ax.plot([r["n"] for r in table_large], [r["time_ms"] for r in table_large], 'bs-', label='Table DP (Random)', linewidth=2)
ax.plot([r["n"] for r in tworow_large], [r["time_ms"] for r in tworow_large], 'c^--', label='Two-Row DP (Random)', linewidth=2)
ax.plot([r["n"] for r in table_disjoint], [r["time_ms"] for r in table_disjoint], 'ms-', label='Table DP (Disjoint)', linewidth=1.5)
ax.plot([r["n"] for r in tworow_disjoint], [r["time_ms"] for r in tworow_disjoint], 'yo--', label='Two-Row DP (Disjoint)', linewidth=1.5)

ax.set_title("Plot 2: DP Scalability: Execution Time vs Input Size n (Large Inputs)")
ax.set_xlabel("String Length n (characters)")
ax.set_ylabel("Wall-clock Time (ms)")
ax.grid(True, ls="--", alpha=0.6)
ax.legend(loc='upper left', frameon=True)
fig.text(0.5, 0.01, "Takeaway: Both DP algorithms display quadratic runtime growth, with Table DP slightly faster in CPU cache and Two-Row saving O(n^2) RAM.", ha='center', fontsize=9, style='italic')

plt.tight_layout(rect=[0, 0.04, 1, 1])
plot2_path = "results/plots/plot2_dp_scalability_time.png"
plt.savefig(plot2_path, dpi=300)
plt.close()
print("Saved", plot2_path)

# =====================================================================
# Plot 3: String Search Comparisons vs n (Worst Case)
# =====================================================================
fig, ax = plt.subplots(figsize=(8, 5.5))

naive_wc = sorted([r for r in data if r["algorithm"] == "NaiveStringSearch" and r["input"] == "worst_case_repetitive"], key=lambda x: x["n"])
kmp_wc = sorted([r for r in data if r["algorithm"] == "KmpStringSearch" and r["input"] == "worst_case_repetitive"], key=lambda x: x["n"])

ax.plot([r["n"] for r in naive_wc], [r["counter"] for r in naive_wc], 'ro-', label='Naive Search ($O(n \\cdot m)$)', linewidth=2)
ax.plot([r["n"] for r in kmp_wc], [r["counter"] for r in kmp_wc], 'bs-', label='KMP Search ($O(n)$)', linewidth=2)

ax.set_title("Plot 3: String Search Comparisons vs Text Length n (Worst-Case Input, m=200)")
ax.set_xlabel("Text Length n (characters)")
ax.set_ylabel("Character Comparisons")
ax.grid(True, ls="--", alpha=0.6)
ax.legend(loc='upper left', frameon=True)
fig.text(0.5, 0.01, "Takeaway: On pathological repetitive input, Naive search requires O(n * m) comparisons, while KMP avoids backtrack and remains strictly O(n).", ha='center', fontsize=9, style='italic')

plt.tight_layout(rect=[0, 0.04, 1, 1])
plot3_path = "results/plots/plot3_search_comparisons_worst_case.png"
plt.savefig(plot3_path, dpi=300)
plt.close()
print("Saved", plot3_path)

# =====================================================================
# Plot 4: Comparisons-to-Length Ratio (comparisons / n)
# =====================================================================
fig, ax = plt.subplots(figsize=(8, 5.5))

naive_wc = sorted([r for r in data if r["algorithm"] == "NaiveStringSearch" and r["input"] == "worst_case_repetitive"], key=lambda x: x["n"])
kmp_wc = sorted([r for r in data if r["algorithm"] == "KmpStringSearch" and r["input"] == "worst_case_repetitive"], key=lambda x: x["n"])
naive_per = sorted([r for r in data if r["algorithm"] == "NaiveStringSearch" and r["input"] == "periodic_overlap"], key=lambda x: x["n"])
kmp_per = sorted([r for r in data if r["algorithm"] == "KmpStringSearch" and r["input"] == "periodic_overlap"], key=lambda x: x["n"])
naive_rnd = sorted([r for r in data if r["algorithm"] == "NaiveStringSearch" and r["input"] == "random"], key=lambda x: x["n"])
kmp_rnd = sorted([r for r in data if r["algorithm"] == "KmpStringSearch" and r["input"] == "random"], key=lambda x: x["n"])

ax.plot([r["n"] for r in naive_wc], [r["counter"] / r["n"] for r in naive_wc], 'ro-', label='Naive: Worst-Case Repetitive', linewidth=2)
ax.plot([r["n"] for r in naive_per], [r["counter"] / r["n"] for r in naive_per], 'mo--', label='Naive: Periodic Overlap', linewidth=1.5)
ax.plot([r["n"] for r in naive_rnd], [r["counter"] / r["n"] for r in naive_rnd], 'yo-.', label='Naive: Random Text', linewidth=1.5)
ax.plot([r["n"] for r in kmp_wc], [r["counter"] / r["n"] for r in kmp_wc], 'bs-', label='KMP: Worst-Case Repetitive', linewidth=2)
ax.plot([r["n"] for r in kmp_per], [r["counter"] / r["n"] for r in kmp_per], 'c^--', label='KMP: Periodic Overlap', linewidth=1.5)
ax.plot([r["n"] for r in kmp_rnd], [r["counter"] / r["n"] for r in kmp_rnd], 'g*-', label='KMP: Random Text', linewidth=1.5)

ax.set_title("Plot 4: String Search Comparison Ratio (Comparisons / n) Across Inputs (m=200)")
ax.set_xlabel("Text Length n (characters)")
ax.set_ylabel("Comparison Ratio (Comparisons / n)")
ax.grid(True, ls="--", alpha=0.6)
ax.legend(loc='center right', frameon=True)
fig.text(0.5, 0.01, "Takeaway: KMP comparison ratio is bounded by <= 2.0 on all inputs, whereas Naive search surges to ~200 comparisons/char on repetitive text.", ha='center', fontsize=9, style='italic')

plt.tight_layout(rect=[0, 0.04, 1, 1])
plot4_path = "results/plots/plot4_search_ratio_comparisons.png"
plt.savefig(plot4_path, dpi=300)
plt.close()
print("Saved", plot4_path)

print("All plots generated successfully!")
