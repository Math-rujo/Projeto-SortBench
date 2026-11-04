
package sortbench.Service;

import java.util.List;

import sortbench.Data.DataGenerator;
import sortbench.Model.BenchmarkConfig;
import sortbench.Model.BenchmarkResult;

public class ExperimentService {

    private final DataGenerator gerador;
    private final BenchmarkService benchmarkService;

    public ExperimentService() {
        this.gerador = new DataGenerator();
        this.benchmarkService = new BenchmarkService();
    }

    public List<BenchmarkResult> executar(
            BenchmarkConfig configuracao) {

        int tamanho = configuracao.getTamanho();
        String distribuicao = configuracao.getDistribuicao();

        int[] dados;

        switch (distribuicao) {
            case "aleatorios":
                dados = gerador.gerarAleatorios(tamanho);
                break;

            case "crescentes":
                dados = gerador.gerarCrescentes(tamanho);
                break;

            case "decrescentes":
                dados = gerador.gerarDecrescentes(tamanho);
                break;

            case "duplicados":
                dados = gerador.gerarDuplicados(tamanho);
                break;

            default:
                throw new IllegalArgumentException(
                        "Distribuição desconhecida: " + distribuicao
                );
        }

        return benchmarkService.comparar(dados);
    }

    public List<BenchmarkResult> executar(int[] dados) {

        if (dados == null || dados.length == 0) {
            throw new IllegalArgumentException(
                    "Os dados importados não podem ser nulos ou vazios."
            );
        }

        return benchmarkService.comparar(dados.clone());
    }
}