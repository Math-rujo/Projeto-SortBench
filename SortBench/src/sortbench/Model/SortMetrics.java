
package sortbench.Model;

public class SortMetrics {

    private long comparacoes;
    private long movimentacoes;
    private long trocas;

    public SortMetrics() {
        this.comparacoes = 0;
        this.movimentacoes = 0;
        this.trocas = 0;
    }

    public void registrarComparacao() {
        comparacoes++;
    }

    public void registrarMovimentacao() {
        movimentacoes++;
    }

    public void registrarTroca() {
        trocas++;
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

    public void reiniciar() {
        comparacoes = 0;
        movimentacoes = 0;
        trocas = 0;
    }
}