# Algorithms I: Percolation Assignment (Princeton University)

This repository contains the solution for the Percolation programming assignment, part of Princeton University's Algorithms, Part I course available on Coursera. The assignment involves modeling a system to determine when it "percolates" and using simulations to estimate the percolation threshold.

## Assignment Goal

The core task is to create a data type (`Percolation.java`) that models an n-by-n grid where sites can be open or blocked. The system percolates if there's a path of open sites from the top row to the bottom row. A second data type (`PercolationStats.java`) uses this model to run Monte Carlo simulations, estimating the fraction of open sites needed for percolation (the percolation threshold, p*).

<img src="images/percolates-no.png" width="30%" alt="Percolation example">
<img src="images/percolates-yes.png" width="30%" alt="Percolation example">

For the full details, problem description, and API requirements, please see the [Official Percolation Assignment Specification](<https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php>).

## Repository Structure

*   `/src`: Contains the Java source code:
    *   `Percolation.java`: Models the percolation system. *(Note: Different implementations addressing the 'backwash' problem exists.)*
    *   `PercolationStats.java`: Runs Monte Carlo simulations.
    *   `PercolationVisualizer.java`: Visualizes the process.
*   `/inputs-visualizer`: Contains sample input text files used for testing and visualization (e.g., `input20.txt`).

## Core Concept

The implementation relies heavily on the **Weighted Quick Union-Find** algorithm (provided in `algs4.jar`) to efficiently track the connectivity of open sites in the grid.

## How to Compile and Run the Visualizer

These commands assume you are running them from the **root directory** of this repository.

1.  **Choose Implementation:** The visualizer uses `Percolation.java`. If you have multiple versions (e.g., `Percolation-noBackwash.java`), **rename the one you want to use to `Percolation.java`** inside the `src` directory. The `-noBackwash` version is recommended for correct visualization of `isFull()`.

2.  **Compile:**
* Download [algs4.jar](<https://algs4.cs.princeton.edu/code/algs4.jar>) to, say ~/algs4/algs4.jar.
    ```bash
    # On Linux/macOS
    javac -cp .:~/algs4/algs4.jar src/Percolation.java src/PercolationVisualizer.java
    ```
* Download [algs4.jar](<https://algs4.cs.princeton.edu/code/algs4.jar>) to, say C:\Users\username\algs4\algs4.jar

    ```bash
    # On Windows
    javac -cp .;C:\Users\username\algs4\algs4.jar src/Percolation.java src/PercolationVisualizer.java
    ```
    *This command tells `javac` where to find the `algs4.jar` library (`dependencies/algs4.jar`) and the source files (`src`), then specifies which source files within the `src` directory to compile.*

3.  **Run Visualizer:**
    ```bash
    # On Linux/macOS
    java -cp .:/dependencies/algs4.jar PercolationVisualizer inputs/<input-file.txt>

    # On Windows
    java -cp .;\dependencies\algs4.jar PercolationVisualizer inputs/<input-file.txt>
    ```
    *Replace `<input-file.txt>` with an actual filename from the `/inputs` directory (e.g., `input20.txt`).*
    *This command tells `java` where to find the compiled classes (`src`) and the library (`dependencies/algs4.jar`), which program to run (`PercolationVisualizer`), and passes the path to an input file as an argument.*

## Assessment Results:
This implementation achieved a maximum score (and exceeded memory expectations) on the course assignment autograder:

* Correctness:  38/38 tests passed
* Memory:       9/8 tests passed
* Timing:       20/20 tests passed
* Aggregate score: 101.25%
