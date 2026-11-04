package sortbench.Model;

public class BenchmarkSummary {

    private final String nomeAlgoritmo;
    private final int tamanhoDados;
    private final int quantidadeExecucoes;
    private final double tempoMedioNanosegundos;
    private final double mediaComparacoes;
    private final double mediaMovimentacoes;
    private final double mediaTrocas;
    private final boolean executado;
    private final String motivoNaoExecucao;

    public BenchmarkSummary(
            String nomeAlgoritmo,
            int tamanhoDados,
            int quantidadeExecucoes,
            double tempoMedioNanosegundos,
            double mediaComparacoes,
            double mediaMovimentacoes,
            double mediaTrocas) {

        this(
                nomeAlgoritmo,
                tamanhoDados,
                quantidadeExecucoes,
                tempoMedioNanosegundos,
                mediaComparacoes,
                mediaMovimentacoes,
                mediaTrocas,
                true,
                null
        );
    }

    private BenchmarkSummary(
            String nomeAlgoritmo,
            int tamanhoDados,
            int quantidadeExecucoes,
            double tempoMedioNanosegundos,
            double mediaComparacoes,
            double mediaMovimentacoes,
            double mediaTrocas,
            boolean executado,
            String motivoNaoExecucao) {

        this.nomeAlgoritmo = nomeAlgoritmo;
        this.tamanhoDados = tamanhoDados;
        this.quantidadeExecucoes = quantidadeExecucoes;
        this.tempoMedioNanosegundos = tempoMedioNanosegundos;
        this.mediaComparacoes = mediaComparacoes;
        this.mediaMovimentacoes = mediaMovimentacoes;
        this.mediaTrocas = mediaTrocas;
        this.executado = executado;
        this.motivoNaoExecucao = motivoNaoExecucao;
    }

    public static BenchmarkSummary naoExecutado(
            String nomeAlgoritmo,
            int tamanhoDados,
            String motivo) {

        return new BenchmarkSummary(
                nomeAlgoritmo,
                tamanhoDados,
                0,
                Double.NaN,
                Double.NaN,
                Double.NaN,
                Double.NaN,
                false,
                motivo
        );
    }

    public String getNomeAlgoritmo() {
        return nomeAlgoritmo;
    }

    public int getTamanhoDados() {
        return tamanhoDados;
    }

    public int getQuantidadeExecucoes() {
        return quantidadeExecucoes;
    }

    public double getTempoMedioNanosegundos() {
        return tempoMedioNanosegundos;
    }

    public double getTempoMedioMilissegundos() {
        return tempoMedioNanosegundos / 1_000_000.0;
    }

    public double getMediaComparacoes() {
        return mediaComparacoes;
    }

    public double getMediaMovimentacoes() {
        return mediaMovimentacoes;
    }

    public double getMediaTrocas() {
        return mediaTrocas;
    }

    public boolean isExecutado() {
        return executado;
    }

    public String getMotivoNaoExecucao() {
        return motivoNaoExecucao;
    }
}