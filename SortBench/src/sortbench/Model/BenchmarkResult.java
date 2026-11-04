package sortbench.Model;

public class BenchmarkResult {

    private final String nomeAlgoritmo;
    private final int tamanhoDados;
    private final long tempoNanosegundos;
    private final long comparacoes;
    private final long movimentacoes;
    private final long trocas;

    public BenchmarkResult(String nomeAlgoritmo, int tamanhoDados, long tempoNanosegundos, long comparacoes, long movimentacoes, long trocas) {
        this.nomeAlgoritmo = nomeAlgoritmo;
        this.tamanhoDados = tamanhoDados;
        this.tempoNanosegundos = tempoNanosegundos;
        this.comparacoes = comparacoes;
        this.movimentacoes = movimentacoes;
        this.trocas = trocas;
    }

    public String getNomeAlgoritmo() {
        return nomeAlgoritmo;
    }

    public int getTamanhoDados() {
        return tamanhoDados;
    }

    public long getTempoNanosegundos() {
        return tempoNanosegundos;
    }

    public long getComparacoes() {
        return comparacoes;
    }

    public long getMovimentacoes() {
        return movimentacoes;
    }

    public long getTrocas() {
        return trocas;
    }

    public double getTempoMilissegundos() {
        return tempoNanosegundos / 1_000_000.0;
    }
}
