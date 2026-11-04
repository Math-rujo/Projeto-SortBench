package sortbench.Algorithms;

import sortbench.Model.SortMetrics;

public class HeapSort implements SortingAlgorithm {

    @Override
    public String getNome() {
        return "Heap Sort";
    }
    
    @Override
    public void ordenar(int[] dados) {

        if (dados == null || dados.length < 2) {
            return;
        }

        int tamanho = dados.length;

        for (int i = tamanho / 2 - 1; i >= 0; i--) {
            ajustarHeapSemMetricas(dados, tamanho, i);
        }

        for (int fim = tamanho - 1; fim > 0; fim--) {
            trocarSemMetricas(dados, 0, fim);
            ajustarHeapSemMetricas(dados, fim, 0);
        }
    }

    @Override
    public void ordenar(int[] dados, SortMetrics metricas) {
        if (dados == null || dados.length < 2) {
            return;
        }

        int tamanho = dados.length;

        for (int i = tamanho / 2 - 1; i >= 0; i--) {
            ajustarHeap(dados, tamanho, i, metricas);
        }

        for (int fim = tamanho - 1; fim > 0; fim--) {
            trocar(dados, 0, fim, metricas);
            ajustarHeap(dados, fim, 0, metricas);
        }
    }

    private void ajustarHeap(int[] dados, int tamanho, int raiz, SortMetrics metricas) {

        int maior = raiz;
        int esquerda = 2 * raiz + 1;
        int direita = 2 * raiz + 2;

        if (esquerda < tamanho) {
            metricas.registrarComparacao();

            if (dados[esquerda] > dados[maior]) {
                maior = esquerda;
            }
        }

        if (direita < tamanho) {
            metricas.registrarComparacao();

            if (dados[direita] > dados[maior]) {
                maior = direita;
            }
        }

        if (maior != raiz) {
            trocar(dados, raiz, maior, metricas);
            ajustarHeap(dados, tamanho, maior, metricas);
        }
    }

    private void trocar(int[] dados, int i, int j, SortMetrics metricas) {

        if (i == j) {
            return;
        }

        int temporario = dados[i];
        dados[i] = dados[j];
        dados[j] = temporario;

        metricas.registrarMovimentacao();
        metricas.registrarMovimentacao();
        metricas.registrarMovimentacao();

        metricas.registrarTroca();
    }
    
    private void ajustarHeapSemMetricas(
            int[] dados,
            int tamanho,
            int raiz) {

        int maior = raiz;

        int esquerda = 2 * raiz + 1;
        int direita = 2 * raiz + 2;

        if (esquerda < tamanho && dados[esquerda] > dados[maior]) {
            maior = esquerda;
        }

        if (direita < tamanho && dados[direita] > dados[maior]) {
            maior = direita;
        }

        if (maior != raiz) {
            trocarSemMetricas(dados, raiz, maior);
            ajustarHeapSemMetricas(dados, tamanho, maior);
        }
    }

    private void trocarSemMetricas(int[] dados, int i, int j) {
        int temporario = dados[i];

        dados[i] = dados[j];
        dados[j] = temporario;
    }

}