
package sortbench.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.ToDoubleFunction;

import javax.swing.JPanel;

import sortbench.Model.BenchmarkSummary;

public class GraficoBarrasPanel extends JPanel {

    private final String titulo;
    private final String unidade;
    private final ToDoubleFunction<BenchmarkSummary> obterValor;

    private List<BenchmarkSummary> resultados = new ArrayList<>();

    public GraficoBarrasPanel(
            String titulo,
            String unidade,
            ToDoubleFunction<BenchmarkSummary> obterValor) {

        this.titulo = titulo;
        this.unidade = unidade;
        this.obterValor = obterValor;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 400));
        setMinimumSize(new Dimension(400, 300));
    }

    public void setResultados(List<BenchmarkSummary> resultados) {
        this.resultados = new ArrayList<>(resultados);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            desenharGrafico(g2);
        } finally {
            g2.dispose();
        }
    }

    private void desenharGrafico(Graphics2D g2) {

        int largura = getWidth();
        int altura = getHeight();

        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString(titulo, 25, 30);

        List<BenchmarkSummary> validos = new ArrayList<>();

        for (BenchmarkSummary resultado : resultados) {
            if (resultado.isExecutado()) {
                double valor = obterValor.applyAsDouble(resultado);

                if (Double.isFinite(valor) && valor >= 0) {
                    validos.add(resultado);
                }
            }
        }

        if (validos.isEmpty()) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g2.drawString(
                    "Execute um benchmark para visualizar o gráfico.",
                    25,
                    70
            );
            return;
        }

        int margemEsquerda = 75;
        int margemDireita = 30;
        int margemSuperior = 65;
        int margemInferior = 100;

        int areaLargura = largura - margemEsquerda - margemDireita;
        int areaAltura = altura - margemSuperior - margemInferior;

        if (areaLargura <= 0 || areaAltura <= 0) {
            return;
        }

        double maiorValor = 0;

        for (BenchmarkSummary resultado : validos) {
            maiorValor = Math.max(
                    maiorValor,
                    obterValor.applyAsDouble(resultado)
            );
        }

        // Evita divisão por zero quando todos os valores são zero.
        if (maiorValor == 0) {
            maiorValor = 1;
        }

        double escala = maiorValor * 1.10;

        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();

        // Linhas horizontais e valores da escala.
        for (int i = 0; i <= 5; i++) {

            int y = margemSuperior
                    + areaAltura
                    - (i * areaAltura / 5);

            double valorEscala = escala * i / 5.0;

            g2.setColor(new Color(225, 225, 225));
            g2.drawLine(
                    margemEsquerda,
                    y,
                    largura - margemDireita,
                    y
            );

            g2.setColor(Color.DARK_GRAY);

            String textoEscala = formatarNumero(valorEscala);
            int textoLargura = fm.stringWidth(textoEscala);

            g2.drawString(
                    textoEscala,
                    margemEsquerda - textoLargura - 10,
                    y + 4
            );
        }

        // Eixos.
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1.2f));

        g2.drawLine(
                margemEsquerda,
                margemSuperior,
                margemEsquerda,
                margemSuperior + areaAltura
        );

        g2.drawLine(
                margemEsquerda,
                margemSuperior + areaAltura,
                largura - margemDireita,
                margemSuperior + areaAltura
        );

        int quantidade = validos.size();
        double larguraGrupo = (double) areaLargura / quantidade;
        double larguraBarra = Math.min(75, larguraGrupo * 0.60);

        Color[] cores = {
            new Color(65, 105, 180),
            new Color(46, 139, 87),
            new Color(218, 165, 32),
            new Color(148, 103, 189),
            new Color(220, 100, 80)
        };

        for (int i = 0; i < quantidade; i++) {

            BenchmarkSummary resultado = validos.get(i);

            double valor = obterValor.applyAsDouble(resultado);

            int barraAltura = (int) Math.round(
                    (valor / escala) * areaAltura
            );

            int centroX = (int) (
                    margemEsquerda + larguraGrupo * (i + 0.5)
            );

            int barraX = (int) (centroX - larguraBarra / 2);
            int barraY = margemSuperior + areaAltura - barraAltura;

            g2.setColor(cores[i % cores.length]);

            g2.fill(new Rectangle2D.Double(
                    barraX,
                    barraY,
                    larguraBarra,
                    Math.max(0, barraAltura)
            ));

            // Valor acima da barra.
            String textoValor = formatarNumero(valor);

            g2.setColor(Color.DARK_GRAY);

            int valorLargura = fm.stringWidth(textoValor);

            g2.drawString(
                    textoValor,
                    centroX - valorLargura / 2,
                    Math.max(margemSuperior + 12, barraY - 7)
            );

            // Nome do algoritmo abaixo da barra.
            String nome = resultado.getNomeAlgoritmo();

            int nomeLargura = fm.stringWidth(nome);

            g2.drawString(
                    nome,
                    centroX - nomeLargura / 2,
                    margemSuperior + areaAltura + 22
            );
        }

        // Unidade do eixo vertical.
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));

        g2.drawString(
                "Unidade: " + unidade,
                margemEsquerda,
                altura - 20
        );
    }

    private String formatarNumero(double valor) {

        NumberFormat formato =
                NumberFormat.getNumberInstance(
                        Locale.forLanguageTag("pt-BR")
                );

        formato.setMaximumFractionDigits(2);
        formato.setMinimumFractionDigits(0);

        return formato.format(valor);
    }
}