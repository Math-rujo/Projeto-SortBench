package sortbench.Algorithms;

import sortbench.Model.SortMetrics;

public class RadixSort implements SortingAlgorithm {

    @Override
    public String getNome() {
        return "Radix Sort";
    }
    
    @Override
    public void ordenar(int[] dados) {
        if (dados == null || dados.length < 2) {
            return;
        }

        int maior = dados[0];

        for (int valor : dados) {
            if (valor < 0) {
                throw new IllegalArgumentException("O Radix Sort aceita apenas valores não negativos.");
            }

            if (valor > maior) {
                maior = valor;
            }
        }

        int[] auxiliar = new int[dados.length];

        for (long exp = 1; maior / exp > 0; exp *= 10) {
            contarPorDigitoSemMetricas(dados, auxiliar, exp);
        }
    }

    @Override
    public void ordenar(int[] dados, SortMetrics metricas) {
        if (dados == null || dados.length < 2) {
            return;
        }

        int maior = dados[0];

        for (int valor : dados) {
            if (valor < 0) {
                throw new IllegalArgumentException("O Radix Sort aceita apenas valores nao negativos.");
            }

            if (valor > maior) {
                maior = valor;
            }
        }

        int[] auxiliar = new int[dados.length];

        for (long exp = 1; maior / exp > 0; exp *= 10) {
            contarPorDigito(dados, auxiliar, exp, metricas);
        }
    }

    private void contarPorDigito(int[] dados, int[] auxiliar, long exp, SortMetrics metricas) {

        int[] contagem = new int[10];
        
        for (int valor : dados) {
            int digito = (int) ((valor / exp) % 10);
            contagem[digito]++;
        }

        for (int i = 1; i < contagem.length; i++) {
            contagem[i] += contagem[i - 1];
        }

        for (int i = dados.length - 1; i >= 0; i--) {
            int digito = (int) ((dados[i] / exp) % 10);
            int posicao = contagem[digito] - 1;

            auxiliar[posicao] = dados[i];
            metricas.registrarMovimentacao();

            contagem[digito]--;
        }

        for (int i = 0; i < dados.length; i++) {
            dados[i] = auxiliar[i];
            metricas.registrarMovimentacao();
        }
    }
    
    private void contarPorDigitoSemMetricas(int[] dados, int[] auxiliar, long exp) {

        int[] contagem = new int[10];

        for (int valor : dados) {
            int digito = (int) ((valor / exp) % 10);
            contagem[digito]++;
        }

        for (int i = 1; i < contagem.length; i++) {
            contagem[i] += contagem[i - 1];
        }

        for (int i = dados.length - 1; i >= 0; i--) {
            int digito = (int) ((dados[i] / exp) % 10);
            int posicao = contagem[digito] - 1;
            auxiliar[posicao] = dados[i];
            contagem[digito]--;
        }

        for (int i = 0; i < dados.length; i++) {
            dados[i] = auxiliar[i];
        }
    }
}