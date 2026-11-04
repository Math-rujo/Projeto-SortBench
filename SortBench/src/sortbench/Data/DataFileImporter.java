package sortbench.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataFileImporter {

    public int[] importar(Path caminho) throws IOException {

        if (caminho == null) {
            throw new IllegalArgumentException("Nenhum arquivo foi selecionado.");
        }

        if (!Files.exists(caminho) || !Files.isRegularFile(caminho)) {
            throw new IOException("O arquivo selecionado não existe ou não é válido.");
        }

        String nomeArquivo = caminho.getFileName().toString().toLowerCase(Locale.ROOT);

        if (!nomeArquivo.endsWith(".csv") && !nomeArquivo.endsWith(".txt")) {
            throw new IOException("Formato inválido. Selecione um arquivo CSV ou TXT.");
        }

        List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);

        List<Integer> valores = new ArrayList<>();

        for (int numeroLinha = 0; numeroLinha < linhas.size(); numeroLinha++) {

            String linha = linhas.get(numeroLinha).trim();
            
            if (linha.isEmpty()) {
                continue;
            }

            String[] partes = linha.split("[,;\\s]+");

            for (String parte : partes) {

                String valorTexto = parte.trim();

                if (valorTexto.isEmpty()) {
                    continue;
                }

                try {
                    valores.add(Integer.parseInt(valorTexto));
                } catch (NumberFormatException ex) {
                    throw new IOException(
                            "Valor inválido na linha " + (numeroLinha + 1) + ": '" + valorTexto 
                                    + "'. Informe apenas números inteiros."
                    );
                }
            }
        }

        if (valores.isEmpty()) {
            throw new IOException("O arquivo não contém números inteiros.");
        }

        int[] dados = new int[valores.size()];

        for (int i = 0; i < valores.size(); i++) {
            dados[i] = valores.get(i);
        }

        return dados;
    }
}