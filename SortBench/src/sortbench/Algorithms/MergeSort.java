package sortbench.Algorithms;

import sortbench.Model.SortMetrics;

public class MergeSort implements SortingAlgorithm {

    @Override
    public String getNome() {
        return "Merge Sort";
    }
    
    @Override
    public void ordenar(int[] dados) {

        if (dados == null || dados.length < 2) {
            return;
        }

        int[] auxiliar = new int[dados.length];

        dividirSemMetricas(dados, auxiliar, 0, dados.length - 1);
    }

    @Override
    public void ordenar(int[] dados, SortMetrics metricas) {
        if (dados == null || dados.length < 2) {
            return;
        }

        int[] auxiliar = new int[dados.length];

        dividir(dados, auxiliar, 0, dados.length - 1, metricas);
    }

    private void dividir(int[] dados, int[] auxiliar, int inicio, int fim, SortMetrics metricas) {

        if (inicio >= fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;

        dividir(dados, auxiliar, inicio, meio, metricas);
        dividir(dados, auxiliar, meio + 1, fim, metricas);

        intercalar(dados, auxiliar, inicio, meio, fim, metricas);
    }

    private void intercalar(int[] dados, int[] auxiliar, int inicio, int meio, int fim, SortMetrics metricas) {

        for (int i = inicio; i <= fim; i++) {
            auxiliar[i] = dados[i];
            metricas.registrarMovimentacao();
        }

        int esquerda = inicio;
        int direita = meio + 1;
        int atual = inicio;

        while (esquerda <= meio && direita <= fim) {
            metricas.registrarComparacao();

            if (auxiliar[esquerda] <= auxiliar[direita]) {
                dados[atual] = auxiliar[esquerda];
                esquerda++;
            } else {
                dados[atual] = auxiliar[direita];
                direita++;
            }

            metricas.registrarMovimentacao();
            atual++;
        }

        while (esquerda <= meio) {
            dados[atual] = auxiliar[esquerda];
            metricas.registrarMovimentacao();

            esquerda++;
            atual++;
        }

        while (direita <= fim) {
            dados[atual] = auxiliar[direita];
            metricas.registrarMovimentacao();

            direita++;
            atual++;
        }
    }
    
    private void dividirSemMetricas(int[] dados, int[] auxiliar, int inicio, int fim) {

        if (inicio >= fim) {
            return;
        }

        int meio = inicio + (fim - inicio) / 2;

        dividirSemMetricas(dados, auxiliar, inicio, meio);
        dividirSemMetricas(dados, auxiliar, meio + 1, fim);
        intercalarSemMetricas(dados, auxiliar, inicio, meio, fim);
    }
    
    
    private void intercalarSemMetricas(int[] dados, int[] auxiliar, int inicio, int meio, int fim) {
        for (int i = inicio; i <= fim; i++) {
            auxiliar[i] = dados[i];
        }

        int esquerda = inicio;
        int direita = meio + 1;
        int atual = inicio;

        while (esquerda <= meio && direita <= fim) {

            if (auxiliar[esquerda] <= auxiliar[direita]) {
                dados[atual] = auxiliar[esquerda];
                esquerda++;
            } else {
                dados[atual] = auxiliar[direita];
                direita++;
            }

            atual++;
        }

        while (esquerda <= meio) {
            dados[atual] = auxiliar[esquerda];
            esquerda++;
            atual++;
        }

        while (direita <= fim) {
            dados[atual] = auxiliar[direita];
            direita++;
            atual++;
        }
    }
}
