package sortbench.Algorithms;

import sortbench.Model.SortMetrics;

public interface SortingAlgorithm {

    String getNome();

    void ordenar(int[] dados);

    void ordenar(int[] dados, SortMetrics metricas);
}
