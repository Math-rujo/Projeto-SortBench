package sortbench.Service;

import java.util.Arrays;

import sortbench.Algorithms.SortingAlgorithm;
import sortbench.Model.BenchmarkResult;
import sortbench.Model.SortMetrics;

public class BenchmarkRunner {

    private static final int QUANTIDADE_AQUECIMENTOS = 3;
    private static final int QUANTIDADE_MEDICOES = 7;

    public BenchmarkResult executar(
            SortingAlgorithm algoritmo,
            int[] dadosOriginais) {

        if (algoritmo == null) {
            throw new IllegalArgumentException("O algoritmo não pode ser nulo.");
        }

        if (dadosOriginais == null) {
            throw new IllegalArgumentException("O vetor de dados não pode ser nulo.");
        }

        SortValidator validador = new SortValidator();


        for (int i = 0; i < QUANTIDADE_AQUECIMENTOS; i++) {
            int[] dadosAquecimento = dadosOriginais.clone();

            algoritmo.ordenar(dadosAquecimento);

            if (!validador.estaOrdenado(dadosAquecimento)) {
                throw new IllegalStateException("O algoritmo " + algoritmo.getNome() + " falhou durante o aquecimento."
                );
            }

            if (!validador.mesmosElementos(dadosOriginais, dadosAquecimento)) {
                throw new IllegalStateException(
                        "O algoritmo " + algoritmo.getNome() + " alterou os elementos durante o aquecimento."
                );
            }
        }

        long[] tempos = new long[QUANTIDADE_MEDICOES];

        for (int i = 0; i < QUANTIDADE_MEDICOES; i++) {

            int[] dadosTempo = dadosOriginais.clone();

            long inicio = System.nanoTime();
            algoritmo.ordenar(dadosTempo);
            long fim = System.nanoTime();

            tempos[i] = fim - inicio;

            if (!validador.estaOrdenado(dadosTempo)) {
                throw new IllegalStateException("O algoritmo " + algoritmo.getNome() + " falhou na execução cronometrada.");
            }

            if (!validador.mesmosElementos(dadosOriginais, dadosTempo)) {
                throw new IllegalStateException("O algoritmo " + algoritmo.getNome() + " alterou os elementos da execução cronometrada.");
            }
        }

        Arrays.sort(tempos);

        long tempoMediano =
                tempos[QUANTIDADE_MEDICOES / 2];

        int[] dadosMetricas = dadosOriginais.clone();

        SortMetrics metricas = new SortMetrics();

        algoritmo.ordenar(dadosMetricas, metricas);

        if (!validador.estaOrdenado(dadosMetricas)) {
            throw new IllegalStateException("O algoritmo " + algoritmo.getNome() + " falhou na execução instrumentada.");
        }

        if (!validador.mesmosElementos(
                dadosOriginais, dadosMetricas)) {
            throw new IllegalStateException("O algoritmo " + algoritmo.getNome() + " alterou os elementos da execução instrumentada.");
        }

        return new BenchmarkResult(
                algoritmo.getNome(),
                dadosOriginais.length,
                tempoMediano,
                metricas.getComparacoes(),
                metricas.getMovimentacoes(),
                metricas.getTrocas()
        );
    }
}