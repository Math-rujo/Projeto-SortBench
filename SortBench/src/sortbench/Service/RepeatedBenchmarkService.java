
package sortbench.Service;

import java.util.ArrayList;
import java.util.List;

import sortbench.Model.BenchmarkConfig;
import sortbench.Model.BenchmarkResult;
import sortbench.Model.BenchmarkSummary;

public class RepeatedBenchmarkService {

    private final ExperimentService experimentService;

    public RepeatedBenchmarkService() {
        this.experimentService = new ExperimentService();
    }

    public List<BenchmarkSummary> executar(int tamanho, String distribuicao, int repeticoes) {

        validarRepeticoes(repeticoes);

        List<List<BenchmarkResult>> execucoes = new ArrayList<>();

        for (int i = 0; i < repeticoes; i++) {
            BenchmarkConfig configuracao =
                    new BenchmarkConfig(tamanho, distribuicao);

            execucoes.add(experimentService.executar(configuracao));
        }

        return calcularResumos(execucoes, tamanho, repeticoes, false);
    }

    public List<BenchmarkSummary> executar(int[] dados, int repeticoes) {

        validarRepeticoes(repeticoes);

        if (dados == null || dados.length == 0) {
            throw new IllegalArgumentException("Os dados importados não podem ser nulos ou vazios.");
        }

        List<List<BenchmarkResult>> execucoes = new ArrayList<>();

        for (int i = 0; i < repeticoes; i++) {
            execucoes.add(experimentService.executar(dados));
        }
        
        return calcularResumos(execucoes, dados.length, repeticoes, contemNegativos(dados));
    }

    private void validarRepeticoes(int repeticoes) {
        if (repeticoes <= 0) {
            throw new IllegalArgumentException(
                    "O número de repetições deve ser positivo."
            );
        }
    }

    private List<BenchmarkSummary> calcularResumos(List<List<BenchmarkResult>> execucoes, int tamanho, int repeticoes, boolean dadosContemNegativos) {

        if (execucoes.isEmpty() || execucoes.get(0).isEmpty()) {
            throw new IllegalStateException(
                    "Nenhum algoritmo foi executado."
            );
        }

        List<BenchmarkResult> primeiraExecucao = execucoes.get(0);
        int quantidadeAlgoritmos = primeiraExecucao.size();

        double[] tempos = new double[quantidadeAlgoritmos];
        double[] comparacoes = new double[quantidadeAlgoritmos];
        double[] movimentacoes = new double[quantidadeAlgoritmos];
        double[] trocas = new double[quantidadeAlgoritmos];

        String[] nomes = new String[quantidadeAlgoritmos];

        for (int i = 0; i < quantidadeAlgoritmos; i++) {
            nomes[i] = primeiraExecucao.get(i).getNomeAlgoritmo();
        }

        for (List<BenchmarkResult> resultados : execucoes) {

            if (resultados.size() != quantidadeAlgoritmos) {
                throw new IllegalStateException(
                        "A quantidade de resultados mudou entre as repetições."
                );
            }

            for (int i = 0; i < quantidadeAlgoritmos; i++) {

                BenchmarkResult resultado = resultados.get(i);

                if (!nomes[i].equals(resultado.getNomeAlgoritmo())) {
                    throw new IllegalStateException(
                            "A ordem dos algoritmos mudou entre as repetições."
                    );
                }

                tempos[i] += resultado.getTempoNanosegundos();
                comparacoes[i] += resultado.getComparacoes();
                movimentacoes[i] += resultado.getMovimentacoes();
                trocas[i] += resultado.getTrocas();
            }
        }

        List<BenchmarkSummary> resumos = new ArrayList<>();

        for (int i = 0; i < quantidadeAlgoritmos; i++) {
            resumos.add(new BenchmarkSummary(
                    nomes[i],
                    tamanho,
                    repeticoes,
                    tempos[i] / repeticoes,
                    comparacoes[i] / repeticoes,
                    movimentacoes[i] / repeticoes,
                    trocas[i] / repeticoes
            ));
        }

        adicionarAlgoritmosNaoExecutados(resumos, tamanho, dadosContemNegativos);

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

    private void adicionarAlgoritmosNaoExecutados(
            List<BenchmarkSummary> resumos,
            int tamanho,
            boolean dadosContemNegativos) {

        if (tamanho > BenchmarkService.LIMITE_INSERTION_SORT) {
            resumos.add(0, BenchmarkSummary.naoExecutado(
                    "Insertion Sort",
                    tamanho,
                    "O limite definido para o Insertion Sort é "
                    + BenchmarkService.LIMITE_INSERTION_SORT
                    + " elementos."
            ));
        }

        if (dadosContemNegativos) {
            resumos.add(BenchmarkSummary.naoExecutado(
                    "Radix Sort",
                    tamanho,
                    "O Radix Sort não foi executado porque "
                    + "a implementação atual aceita apenas "
                    + "números não negativos."
            ));
        }
    }
}