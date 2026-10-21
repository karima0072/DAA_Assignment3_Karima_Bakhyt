
# DAA Assignment 3 – Graph Algorithms

**Student:** Karima Bakhyt  
**Group:** SE-2518  
**Course:** Design and Analysis of Algorithms  
**Instructor:** Taubakabyl Nurlybek

## Project Description

This project implements graph algorithms for a city network and job scheduling system.

The implemented algorithms are:

- DSU with path compression and union by rank
- Kruskal's minimum spanning tree algorithm
- Prim's minimum spanning tree algorithm
- Kosaraju's strongly connected components algorithm
- Kahn's topological sorting algorithm
- Shortest paths in a directed acyclic graph

All algorithms use a shared Graph class with an adjacency list representation.

## Requirements

- Java 17
- Maven
- JUnit 5
- Python 3
- pandas
- matplotlib
- numpy

## Project Structure

```text
DAA_Assignment3_Karima/
├── src/
│   ├── main/java/
│   │   ├── Graph.java
│   │   ├── Edge.java
│   │   ├── GraphReader.java
│   │   ├── GraphGenerator.java
│   │   ├── GraphMetrics.java
│   │   ├── DSU.java
│   │   ├── MSTResult.java
│   │   ├── Kruskal.java
│   │   ├── Prim.java
│   │   ├── Kosaraju.java
│   │   ├── Kahn.java
│   │   ├── DagShortestPath.java
│   │   ├── ReferenceCheck.java
│   │   └── Benchmark.java
│   └── test/java/
│       ├── GraphTest.java
│       ├── DSUTest.java
│       ├── MSTTest.java
│       ├── KosarajuTest.java
│       ├── KahnTest.java
│       ├── DagShortestPathTest.java
│       └── GraphGeneratorTest.java
├── data/
│   ├── connected.txt
│   ├── disconnected.txt
│   └── cyclic.txt
├── results/
│   ├── results.csv
│   └── plots/
├── plot_results.py
├── analyze_results.py
├── REPORT.md
├── README.md
└── pom.xml
```

## Build and Run Tests

Open the project in IntelliJ IDEA and use:

Maven → Lifecycle → test

Or run the command:

```bash
mvn test
```

The tests cover graph input, DSU, MST, SCC, topological sorting, DAG shortest paths and the graph generator.

## Run the Benchmark

Run the `Benchmark` main class in IntelliJ IDEA.

The benchmark uses the following graph sizes:

- 1,000
- 3,162
- 10,000
- 31,623
- 100,000

Two densities are tested:

- Sparse: E = 4V
- Dense: E = 20V

Each algorithm is executed once for warm-up and five times for measurement. The median time is recorded.

The results are saved in:

`results/results.csv`

The benchmark automatically checks the MST weights, reference values and required edge scan counts.

## Generate Plots

Run:

```bash
python plot_results.py
```

Three PNG charts are generated in `results/plots/`:

- `operations_vs_n.png`
- `scans_per_edge.png`
- `kruskal_edge_share.png`

## Analyze Complexity

Run:

```bash
python analyze_results.py
```

This script calculates counter-to-formula ratios and log-log slopes.

## Input Graph Format

Graph files are stored in the `data/` directory.

Example:

```text
4 3 undirected
0 1 5
1 2 10
2 3 7
```

The first line contains the number of vertices, number of edges and graph type.

The remaining lines contain the source vertex, destination vertex and weight.

## Git Workflow

Development is organized using these branches:

- main
- feature/graph
- feature/dsu
- feature/mst
- feature/scc
- feature/paths
- feature/metrics

The final release is marked with tag `v1.0`.

## AI Use Acknowledgement

AI tools were used during development for explanations of graph algorithms, debugging assistance, test design, code drafting, and documentation support.

The implementation, tests, benchmark results and analysis must be reviewed and understood before submission.

## Report

The full complexity analysis, correctness arguments, benchmark results and algorithm comparisons are available in `REPORT.md`.
