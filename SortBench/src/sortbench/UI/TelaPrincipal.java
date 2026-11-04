
package sortbench.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.SpinnerNumberModel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import sortbench.Data.DataFileImporter;
import sortbench.Model.BenchmarkSummary;
import sortbench.Service.RepeatedBenchmarkService;

public class TelaPrincipal extends JFrame {

    private final JSpinner campoTamanho;
    private final JSpinner campoRepeticoes;
    private final JComboBox<String> campoDistribuicao;

    private final JButton botaoExecutar;
    private final JButton botaoImportar;
    private final JButton botaoUsarGerados;
    private final JButton botaoExportar;

    private final JLabel mensagemStatus;
    private final JLabel mensagemFonte;

    private final DefaultTableModel modeloTabela;
    private final JTable tabelaResultados;
    private final JTabbedPane abasResultados;

    private final GraficoBarrasPanel graficoTempo;
    private final GraficoBarrasPanel graficoComparacoes;
    private final GraficoBarrasPanel graficoMovimentacoes;

    private List<BenchmarkSummary> ultimosResultados =
            new ArrayList<>();

    private final DataFileImporter importador;
    private int[] dadosImportados;

    private final RepeatedBenchmarkService servicoBenchmark;

    public TelaPrincipal() {

        setTitle("SortBench - Análise de Algoritmos de Ordenação");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);

        servicoBenchmark = new RepeatedBenchmarkService();

        importador = new DataFileImporter();
        dadosImportados = null;

        botaoImportar = new JButton("Importar CSV/TXT");
        botaoUsarGerados = new JButton("Usar dados gerados");

        botaoExportar = new JButton("Exportar resultados CSV");
        botaoExportar.setEnabled(false);

        mensagemFonte = new JLabel(
                "Fonte de dados: geração automática"
        );

        JPanel painelPrincipal = new JPanel(
                new BorderLayout(15, 15)
        );

        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel titulo = new JLabel(
                "SortBench",
                SwingConstants.LEFT
        );

        titulo.setFont(
                new Font("SansSerif", Font.BOLD, 28)
        );

        JLabel subtitulo = new JLabel(
                "Sistema de análise de performance de algoritmos de ordenação"
        );

        subtitulo.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        JPanel painelCabecalho = new JPanel(
                new BorderLayout(5, 5)
        );

        painelCabecalho.add(titulo, BorderLayout.NORTH);
        painelCabecalho.add(subtitulo, BorderLayout.CENTER);

        JPanel painelConfiguracao = new JPanel(
                new GridBagLayout()
        );

        painelConfiguracao.setBorder(
                BorderFactory.createTitledBorder(
                        "Configuração do experimento"
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: tamanho dos dados e distribuição.

        gbc.gridx = 0;
        gbc.gridy = 0;

        painelConfiguracao.add(
                new JLabel("Tamanho dos dados:"),
                gbc
        );

        campoTamanho = new JSpinner(
                new SpinnerNumberModel(1000, 1, 1000000, 1000)
        );

        campoTamanho.setPreferredSize(
                new Dimension(150, 28)
        );

        gbc.gridx = 1;

        painelConfiguracao.add(campoTamanho, gbc);

        gbc.gridx = 2;

        painelConfiguracao.add(
                new JLabel("Distribuição:"),
                gbc
        );

        campoDistribuicao = new JComboBox<>(new String[]{
            "Aleatórios",
            "Crescentes",
            "Decrescentes",
            "Duplicados (40%)"
        });

        campoDistribuicao.setPreferredSize(
                new Dimension(180, 28)
        );

        gbc.gridx = 3;

        painelConfiguracao.add(campoDistribuicao, gbc);

        // Linha 1: repetições e execução.

        gbc.gridx = 0;
        gbc.gridy = 1;

        painelConfiguracao.add(
                new JLabel("Repetições:"),
                gbc
        );

        campoRepeticoes = new JSpinner(
                new SpinnerNumberModel(5, 1, 30, 1)
        );

        campoRepeticoes.setPreferredSize(
                new Dimension(150, 28)
        );

        gbc.gridx = 1;

        painelConfiguracao.add(campoRepeticoes, gbc);

        botaoExecutar = new JButton("Executar benchmark");

        botaoExecutar.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        painelConfiguracao.add(botaoExecutar, gbc);

        // Linha 2: importação e seleção da fonte dos dados.

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        painelConfiguracao.add(botaoImportar, gbc);

        gbc.gridx = 1;

        painelConfiguracao.add(botaoUsarGerados, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;

        painelConfiguracao.add(mensagemFonte, gbc);

        // Linha 3: exportação dos resultados.

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        painelConfiguracao.add(botaoExportar, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        // Painel dos resultados.

        JPanel painelResultados = new JPanel(
                new BorderLayout()
        );

        painelResultados.setBorder(
                BorderFactory.createTitledBorder("Resultados")
        );

        String[] colunas = {
            "Algoritmo",
            "Tempo médio (ms)",
            "Comparações",
            "Movimentações",
            "Trocas",
            "Execuções",
            "Status"
        };

        graficoTempo = new GraficoBarrasPanel(
                "Tempo médio por algoritmo",
                "milissegundos (ms)",
                BenchmarkSummary::getTempoMedioMilissegundos
        );

        graficoComparacoes = new GraficoBarrasPanel(
                "Comparações por algoritmo",
                "comparações",
                BenchmarkSummary::getMediaComparacoes
        );

        graficoMovimentacoes = new GraficoBarrasPanel(
                "Movimentações por algoritmo",
                "movimentações",
                BenchmarkSummary::getMediaMovimentacoes
        );

        abasResultados = new JTabbedPane();

        abasResultados.addTab("Tabela", painelResultados);

        abasResultados.addTab(
                "Tempo médio",
                new JScrollPane(graficoTempo)
        );

        abasResultados.addTab(
                "Comparações",
                new JScrollPane(graficoComparacoes)
        );

        abasResultados.addTab(
                "Movimentações",
                new JScrollPane(graficoMovimentacoes)
        );

        modeloTabela = new DefaultTableModel(colunas, 0) {

            @Override
            public boolean isCellEditable(
                    int linha,
                    int coluna
            ) {
                return false;
            }
        };

        tabelaResultados = new JTable(modeloTabela);
        tabelaResultados.setRowHeight(28);

        tabelaResultados.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        tabelaResultados.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        int[] larguras = {
            180, 130, 130, 140, 100, 90, 180
        };

        for (int i = 0; i < larguras.length; i++) {

            tabelaResultados.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(larguras[i]);
        }

        tabelaResultados.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        JScrollPane rolagem = new JScrollPane(
                tabelaResultados
        );

        painelResultados.add(
                rolagem,
                BorderLayout.CENTER
        );

        // Rodapé.

        mensagemStatus = new JLabel(
                "Pronto para executar."
        );

        mensagemStatus.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );

        JPanel painelRodape = new JPanel(
                new BorderLayout()
        );

        painelRodape.add(
                mensagemStatus,
                BorderLayout.CENTER
        );

        // Organização geral da janela.

        JPanel painelCentral = new JPanel(
                new BorderLayout(10, 10)
        );

        painelCentral.add(
                painelConfiguracao,
                BorderLayout.NORTH
        );

        painelCentral.add(
                abasResultados,
                BorderLayout.CENTER
        );

        painelPrincipal.add(
                painelCabecalho,
                BorderLayout.NORTH
        );

        painelPrincipal.add(
                painelCentral,
                BorderLayout.CENTER
        );

        painelPrincipal.add(
                painelRodape,
                BorderLayout.SOUTH
        );

        setContentPane(painelPrincipal);

        // Eventos dos botões.

        botaoExecutar.addActionListener(
                evento -> executarBenchmark()
        );

        botaoImportar.addActionListener(
                evento -> importarArquivo()
        );

        botaoUsarGerados.addActionListener(evento -> {

            dadosImportados = null;

            campoTamanho.setEnabled(true);
            campoDistribuicao.setEnabled(true);

            mensagemFonte.setText(
                    "Fonte de dados: geração automática"
            );

            mensagemStatus.setText(
                    "Pronto para executar com dados gerados."
            );
        });

        botaoExportar.addActionListener(
                evento -> exportarResultados()
        );
    }

    private void importarArquivo() {

        JFileChooser seletor = new JFileChooser();

        seletor.setDialogTitle(
                "Selecionar arquivo de dados"
        );

        FileNameExtensionFilter filtro =
                new FileNameExtensionFilter(
                        "Arquivos CSV e TXT (*.csv, *.txt)",
                        "csv",
                        "txt"
                );

        seletor.setFileFilter(filtro);
        seletor.setAcceptAllFileFilterUsed(false);

        int opcao = seletor.showOpenDialog(this);

        if (opcao != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {

            Path caminho = seletor.getSelectedFile().toPath();

            int[] dados = importador.importar(caminho);

            dadosImportados = dados;

            campoTamanho.setEnabled(false);
            campoDistribuicao.setEnabled(false);

            mensagemFonte.setText(
                    "Arquivo: "
                    + caminho.getFileName()
                    + " | Valores carregados: "
                    + dadosImportados.length
            );

            mensagemStatus.setText(
                    "Arquivo importado com sucesso."
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível importar o arquivo:\n"
                    + ex.getMessage(),
                    "Erro na importação",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void executarBenchmark() {

        int tamanho = dadosImportados != null
                ? dadosImportados.length
                : (Integer) campoTamanho.getValue();

        int repeticoes = (Integer) campoRepeticoes.getValue();

        String distribuicao = dadosImportados == null
                ? obterDistribuicaoSelecionada()
                : null;

        int[] dadosParaBenchmark = dadosImportados == null
                ? null
                : dadosImportados.clone();

        botaoExecutar.setEnabled(false);
        botaoExportar.setEnabled(false);

        mensagemStatus.setText(
                "Executando benchmark. Aguarde..."
        );

        modeloTabela.setRowCount(0);

        ultimosResultados.clear();

        graficoTempo.setResultados(ultimosResultados);
        graficoComparacoes.setResultados(ultimosResultados);
        graficoMovimentacoes.setResultados(ultimosResultados);

        SwingWorker<List<BenchmarkSummary>, Void> trabalhador =
                new SwingWorker<>() {

            @Override
            protected List<BenchmarkSummary> doInBackground()
                    throws Exception {

                if (dadosParaBenchmark != null) {

                    return servicoBenchmark.executar(
                            dadosParaBenchmark,
                            repeticoes
                    );
                }

                return servicoBenchmark.executar(
                        tamanho,
                        distribuicao,
                        repeticoes
                );
            }

            @Override
            protected void done() {

                try {

                    List<BenchmarkSummary> resultados = get();

                    ultimosResultados =
                            new ArrayList<>(resultados);

                    for (BenchmarkSummary resumo : resultados) {
                        adicionarResultado(resumo);
                    }

                    graficoTempo.setResultados(ultimosResultados);
                    graficoComparacoes.setResultados(ultimosResultados);
                    graficoMovimentacoes.setResultados(ultimosResultados);

                    mensagemStatus.setText(
                            "Benchmark concluído. "
                            + resultados.size()
                            + " resultados apresentados."
                    );

                    // A exportação só é habilitada após o sucesso.
                    botaoExportar.setEnabled(
                            !ultimosResultados.isEmpty()
                    );

                } catch (InterruptedException ex) {

                    Thread.currentThread().interrupt();

                    mensagemStatus.setText(
                            "A execução foi interrompida."
                    );

                    JOptionPane.showMessageDialog(
                            TelaPrincipal.this,
                            "A execução do benchmark foi interrompida.",
                            "Execução interrompida",
                            JOptionPane.WARNING_MESSAGE
                    );

                } catch (ExecutionException ex) {

                    Throwable causa = ex.getCause();

                    mensagemStatus.setText(
                            "Erro durante o benchmark."
                    );

                    JOptionPane.showMessageDialog(
                            TelaPrincipal.this,
                            "Não foi possível executar o benchmark:\n"
                            + (causa != null
                                    ? causa.getMessage()
                                    : ex.getMessage()),
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );

                } finally {

                    botaoExecutar.setEnabled(true);
                }
            }
        };

        trabalhador.execute();
    }

    private String obterDistribuicaoSelecionada() {

        String selecionada =
                (String) campoDistribuicao.getSelectedItem();

        return switch (selecionada) {
            case "Aleatórios" -> "aleatorios";
            case "Crescentes" -> "crescentes";
            case "Decrescentes" -> "decrescentes";
            case "Duplicados (40%)" -> "duplicados";
            default -> throw new IllegalArgumentException(
                    "Distribuição não reconhecida: " + selecionada
            );
        };
    }

    private void adicionarResultado(BenchmarkSummary resumo) {

        if (resumo.isExecutado()) {

            modeloTabela.addRow(new Object[]{
                resumo.getNomeAlgoritmo(),
                formatarNumero(
                        resumo.getTempoMedioMilissegundos()
                ),
                formatarNumero(
                        resumo.getMediaComparacoes()
                ),
                formatarNumero(
                        resumo.getMediaMovimentacoes()
                ),
                formatarNumero(
                        resumo.getMediaTrocas()
                ),
                resumo.getQuantidadeExecucoes(),
                "Executado"
            });

        } else {

            modeloTabela.addRow(new Object[]{
                resumo.getNomeAlgoritmo(),
                "—",
                "—",
                "—",
                "—",
                0,
                "Não executado: "
                + resumo.getMotivoNaoExecucao()
            });
        }
    }

    private String formatarNumero(double valor) {

        return String.format(
                Locale.forLanguageTag("pt-BR"),
                "%.2f",
                valor
        );
    }

    private void exportarResultados() {

        if (ultimosResultados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não existem resultados para exportar.",
                    "Exportação",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser seletor = new JFileChooser();

        seletor.setDialogTitle(
                "Salvar resultados do benchmark"
        );

        FileNameExtensionFilter filtro =
                new FileNameExtensionFilter(
                        "Arquivo CSV (*.csv)",
                        "csv"
                );

        seletor.setFileFilter(filtro);
        seletor.setAcceptAllFileFilterUsed(false);

        seletor.setSelectedFile(
                new java.io.File("resultados_sortbench.csv")
        );

        int opcao = seletor.showSaveDialog(this);

        if (opcao != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path caminho = seletor.getSelectedFile().toPath();

        if (!caminho.getFileName().toString()
                .toLowerCase(Locale.ROOT).endsWith(".csv")) {

            caminho = caminho.resolveSibling(
                    caminho.getFileName() + ".csv"
            );
        }

        if (Files.exists(caminho)) {

            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "O arquivo já existe. Deseja substituí-lo?",
                    "Confirmar substituição",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacao != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try (BufferedWriter escritor = Files.newBufferedWriter(
                caminho,
                StandardCharsets.UTF_8)) {

            escritor.write(
                    "Algoritmo;Tamanho dos dados;Tempo médio (ms);"
                    + "Comparações;Movimentações;Trocas;"
                    + "Execuções;Status;Motivo"
            );

            escritor.newLine();

            for (BenchmarkSummary resumo : ultimosResultados) {

                String tempo = "";
                String comparacoes = "";
                String movimentacoes = "";
                String trocas = "";
                String execucoes = "";
                String status;
                String motivo = "";

                if (resumo.isExecutado()) {

                    tempo = formatarNumero(
                            resumo.getTempoMedioMilissegundos()
                    );

                    comparacoes = formatarNumero(
                            resumo.getMediaComparacoes()
                    );

                    movimentacoes = formatarNumero(
                            resumo.getMediaMovimentacoes()
                    );

                    trocas = formatarNumero(
                            resumo.getMediaTrocas()
                    );

                    execucoes = String.valueOf(
                            resumo.getQuantidadeExecucoes()
                    );

                    status = "Executado";

                } else {

                    status = "Não executado";
                    motivo = resumo.getMotivoNaoExecucao();
                }

                String[] campos = {
                    resumo.getNomeAlgoritmo(),
                    String.valueOf(resumo.getTamanhoDados()),
                    tempo,
                    comparacoes,
                    movimentacoes,
                    trocas,
                    execucoes,
                    status,
                    motivo
                };

                for (int i = 0; i < campos.length; i++) {

                    if (i > 0) {
                        escritor.write(";");
                    }

                    escritor.write(
                            escaparCampoCsv(campos[i])
                    );
                }

                escritor.newLine();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Resultados exportados com sucesso para:\n"
                    + caminho.toAbsolutePath(),
                    "Exportação concluída",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível exportar os resultados:\n"
                    + ex.getMessage(),
                    "Erro na exportação",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String escaparCampoCsv(String valor) {

        if (valor == null) {
            return "";
        }

        return "\"" + valor.replace("\"", "\"\"") + "\"";
    }
}