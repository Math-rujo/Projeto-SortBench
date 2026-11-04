
package sortbench.Service;

import java.util.ArrayList;
import java.util.List;

import sortbench.Algorithms.SortingAlgorithm;
import sortbench.Algorithms.InsertionSort;
import sortbench.Algorithms.MergeSort;
import sortbench.Algorithms.HeapSort;
import sortbench.Algorithms.RadixSort;
import sortbench.Algorithms.QuickSortDualPivot;

import sortbench.Model.BenchmarkResult;
import sortbench.Model.BenchmarkSummary;

public class BenchmarkService {

    public static final int LIMITE_INSERTION_SORT = 100_000;

    private final BenchmarkRunner runner;

    public BenchmarkService() {
        this.runner = new BenchmarkRunner();
    }

    public List<BenchmarkResult> comparar(int[] dadosOriginais) {

        if (dadosOriginais == null || dadosOriginais.length == 0) {
            throw new IllegalArgumentException(
                    "O vetor de dados não pode ser nulo ou vazio."
            );
        }

        List<SortingAlgorithm> algoritmos = new ArrayList<>();

        algoritmos.add(new InsertionSort());
        algoritmos.add(new MergeSort());
        algoritmos.add(new HeapSort());
        algoritmos.add(new RadixSort());
        algoritmos.add(new QuickSortDualPivot());

        List<BenchmarkResult> resultados = new ArrayList<>();

        for (SortingAlgorithm algoritmo : algoritmos) {

            if (algoritmo instanceof InsertionSort
                    && dadosOriginais.length > LIMITE_INSERTION_SORT) {
                continue;
            }

            if (algoritmo instanceof RadixSort
                    && contemNegativos(dadosOriginais)) {
                continue;
            }

            BenchmarkResult resultado =
                    runner.executar(algoritmo, dadosOriginais);

            resultados.add(resultado);
        }

        return resultados;
    }

    public List<BenchmarkSummary> compararComStatus(int[] dadosOriginais) {

        List<BenchmarkSummary> resumos = new ArrayList<>();
        List<BenchmarkResult> resultados = comparar(dadosOriginais);

        for (BenchmarkResult resultado : resultados) {
            resumos.add(new BenchmarkSummary(
                    resultado.getNomeAlgoritmo(),
                    resultado.getTamanhoDados(),
                    1,
                    resultado.getTempoNanosegundos(),
                    resultado.getComparacoes(),
                    resultado.getMovimentacoes(),
                    resultado.getTrocas()
            ));
        }

        if (dadosOriginais.length > LIMITE_INSERTION_SORT) {
            resumos.add(0, BenchmarkSummary.naoExecutado(
                    "Insertion Sort",
                    dadosOriginais.length,
                    "O limite definido para o Insertion Sort é "
                    + LIMITE_INSERTION_SORT + " elementos."
            ));
        }

        if (contemNegativos(dadosOriginais)) {
            int posicaoRadix = 0;

            for (int i = 0; i < resumos.size(); i++) {
                if ("Radix Sort".equals(resumos.get(i).getNomeAlgoritmo())) {
                    posicaoRadix = i;
                    break;
                }
            }
            resumos.add(posicaoRadix, BenchmarkSummary.naoExecutado("Radix Sort", dadosOriginais.length, "Esta implementação do Radix Sort aceita apenas números não negativos."));
        }

        return resumos;
    }

    private boolean contemNegativos(int[] dados) {
        for (int valor : dados) {
            if (valor < 0) {
                return true;
            }
        }

        return false;
    }
}