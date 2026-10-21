
import os
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

data = pd.read_csv("results/results.csv")
output = "results/plots"
os.makedirs(output, exist_ok=True)

sizes = [1000, 3162, 10000, 31623, 100000]

styles = {
    "Kruskal": ("#2563eb", "o"),
    "Prim": ("#ea580c", "s"),
    "Kosaraju": ("#16a34a", "^"),
    "Kahn": ("#9333ea", "D"),
    "DagShortestPath": ("#0891b2", "v")
}

densities = {
    "sparse": "-",
    "dense": "--"
}


def format_plot(ax, title, ylabel, log_y=False):
    ax.set_xscale("log")

    if log_y:
        ax.set_yscale("log")

    ax.set_xticks(sizes)
    ax.set_xticklabels([
        r"$10^3$",
        "3162",
        r"$10^4$",
        "31623",
        r"$10^5$"
    ])

    ax.set_xlabel("Number of vertices (n)")
    ax.set_ylabel(ylabel)
    ax.set_title(title, fontsize=13, fontweight="bold")
    ax.grid(True, which="major", alpha=0.3)


fig, ax = plt.subplots(figsize=(11, 6))

for algorithm in styles:
    for density in densities:
        part = data[
            (data["algorithm"] == algorithm) &
            (data["graph"] == density)
            ].sort_values("n")

        color, marker = styles[algorithm]

        operations = (
                part["dsu_ops"] +
                part["edge_scans"] +
                part["comparisons"]
        )

        ax.plot(
            part["n"],
            operations,
            marker=marker,
            linestyle=densities[density],
            color=color,
            linewidth=2,
            label=f"{algorithm} {density}"
        )

format_plot(
    ax,
    "Operations vs Graph Size",
    "Counted operations",
    log_y=True
)

ax.legend(
    loc="upper center",
    bbox_to_anchor=(0.5, -0.15),
    fontsize=8,
    ncol=5,
    frameon=False
)
fig.tight_layout()
fig.savefig(f"{output}/operations_vs_n.png", dpi=200)
bbox_inches="tight"
plt.close(fig)


fig, ax = plt.subplots(figsize=(10, 6))

for algorithm in styles:
    for density in densities:
        part = data[
            (data["algorithm"] == algorithm) &
            (data["graph"] == density)
            ].sort_values("n")

        color, marker = styles[algorithm]

        ratio = part["edge_scans"] / part["m"]

        ax.plot(
            part["n"],
            ratio,
            marker=marker,
            linestyle=densities[density],
            color=color,
            linewidth=2,
            label=f"{algorithm} {density}"
        )

format_plot(
    ax,
    "Scans per Edge",
    "edgeScans / m"
)

ax.legend(
    loc="upper center",
    bbox_to_anchor=(0.5, -0.15),
    ncol=2,
    frameon=False
)
fig.tight_layout()
fig.savefig(f"{output}/scans_per_edge.png", dpi=200)
bbox_inches="tight"
plt.close(fig)


fig, ax = plt.subplots(figsize=(9, 5))

for density in densities:
    part = data[
        (data["algorithm"] == "Kruskal") &
        (data["graph"] == density)
        ].sort_values("n")

    ratio = part["edge_scans"] / part["m"]

    ax.plot(
        part["n"],
        ratio,
        marker="o",
        linestyle=densities[density],
        linewidth=2.3,
        label=density.capitalize()
    )

format_plot(
    ax,
    "Share of Edges Examined by Kruskal",
    "edgeScans / m"
)

ax.legend()
fig.tight_layout()
fig.savefig(f"{output}/kruskal_edge_share.png", dpi=200)
bbox_inches="tight"
plt.close(fig)


for algorithm in ["Kosaraju", "Kahn", "DagShortestPath"]:
    part = data[
        (data["algorithm"] == algorithm) &
        (data["graph"] == "sparse")
        ].sort_values("n")

    x = np.log10(part["n"].to_numpy(dtype=float))
    y = np.log10(part["edge_scans"].to_numpy(dtype=float))

    slope = np.polyfit(x, y, 1)[0]

    print(f"{algorithm} edge scan slope: {slope:.3f}")

print("Three benchmark plots created successfully.")
