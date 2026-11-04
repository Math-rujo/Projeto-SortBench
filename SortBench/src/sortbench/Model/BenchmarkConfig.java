
package sortbench.Model;

public class BenchmarkConfig {

    private final int tamanho;
    private final String distribuicao;

    public BenchmarkConfig(int tamanho, String distribuicao) {

        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho deve ser maior que zero.");
        }

        if (distribuicao == null || distribuicao.isBlank()) {
            throw new IllegalArgumentException("A distribuição deve ser informada.");
        }

        String distribuicaoNormalizada = distribuicao.trim().toLowerCase();

        if (!distribuicaoNormalizada.equals("aleatorios")
                && !distribuicaoNormalizada.equals("crescentes")
                && !distribuicaoNormalizada.equals("decrescentes")
                && !distribuicaoNormalizada.equals("duplicados")) {

            throw new IllegalArgumentException("Distribuição inválida: " + distribuicao + ". Use aleatorios, crescentes, decrescentes ou duplicados.");
        }

        this.tamanho = tamanho;
        this.distribuicao = distribuicaoNormalizada;
    }

    public int getTamanho() {
        return tamanho;
    }

    public String getDistribuicao() {
        return distribuicao;
    }
}