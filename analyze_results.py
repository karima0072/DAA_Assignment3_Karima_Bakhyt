
import pandas as pd
import numpy as np

data = pd.read_csv("results/results.csv")

print("COUNTERS VS FORMULAS")
print()

for algorithm in ["Kruskal", "Prim", "Kosaraju", "Kahn", "DagShortestPath"]:
    print(algorithm)

    for density in ["sparse", "dense"]:
        part = data[
            (data["algorithm"] == algorithm) &
            (data["graph"] == density)
            ].sort_values("n")

        e = part["m"].to_numpy(dtype=float)
        v = part["n"].to_numpy(dtype=float)
        scans = part["edge_scans"].to_numpy(dtype=float)
        comparisons = part["comparisons"].to_numpy(dtype=float)

        if algorithm == "Kruskal":
            ratio = part["dsu_ops"].to_numpy(dtype=float) / e
            sort_ratio = comparisons / (e * np.log2(e))

            print(density, "dsuOps/E:", np.round(ratio, 3))
            print(density, "comparisons/(E log2 E):",
                  np.round(sort_ratio, 3))

        elif algorithm == "Prim":
            ratio = comparisons / (e * np.log2(e))
            print(density, "comparisons/(E log2 E):",
                  np.round(ratio, 3))

        else:
            ratio = scans / (v + e)
            print(density, "edgeScans/(V+E):",
                  np.round(ratio, 3))

    print()

print("LOG-LOG SLOPES")
print()

for algorithm in ["Kruskal", "Prim", "Kosaraju", "Kahn", "DagShortestPath"]:
    for density in ["sparse", "dense"]:
        part = data[
            (data["algorithm"] == algorithm) &
            (data["graph"] == density)
            ].sort_values("n")

        x = np.log10(part["n"].to_numpy(dtype=float))

        if algorithm in ["Kruskal", "Prim"]:
            y_values = part["comparisons"].to_numpy(dtype=float)
        else:
            y_values = part["edge_scans"].to_numpy(dtype=float)

        y = np.log10(y_values)
        slope = np.polyfit(x, y, 1)[0]

        print(f"{algorithm} {density}: {slope:.3f}")
