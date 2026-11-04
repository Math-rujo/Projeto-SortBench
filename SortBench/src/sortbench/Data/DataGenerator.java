package sortbench.Data;

import java.util.Random;

public class DataGenerator {
    
    private final Random random = new Random();
    
    public int[] gerarAleatorios(int tamanho){
        validarTamanho(tamanho);
        
        int[] dados = new int[tamanho];
        
        for(int i = 0; i < tamanho; i++){
            dados[i] = random.nextInt(1_000_001);
        }
        return dados;
    }
    
    public int[] gerarCrescentes(int tamanho){
        validarTamanho(tamanho);
        
        int[] dados = new int[tamanho];
        
        for(int i = 0; i < tamanho; i++){
            dados[i] = i;
        }
        return dados;
    }
    
    public int[] gerarDecrescentes(int tamanho){
        validarTamanho(tamanho);
        
        int[] dados = new int[tamanho];
        
        for(int i = 0; i < tamanho; i++){
            dados[i] = tamanho - 1 - i;
        }
        return dados;
    }

    public int[] gerarDuplicados(int tamanho) {
        return gerarDuplicados(tamanho, 40);
    }

   
    public int[] gerarDuplicados(int tamanho, int percentualDuplicados) {
        validarTamanho(tamanho);

        if (percentualDuplicados < 0 || percentualDuplicados > 100) {
            throw new IllegalArgumentException("O percentual deve estar entre 0 e 100.");
        }

        int quantidadeExcedente = tamanho * percentualDuplicados / 100;
        int quantidadeDistintos = tamanho - quantidadeExcedente;

        int[] dados = new int[tamanho];

        if (quantidadeDistintos == 0) {
            return dados;
        }

        for (int i = 0; i < quantidadeDistintos; i++) {
            dados[i] = i;
        }

        for (int i = quantidadeDistintos; i < tamanho; i++) {
            dados[i] = random.nextInt(quantidadeDistintos);
        }

        for (int i = dados.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);

            int temporario = dados[i];
            dados[i] = dados[j];
            dados[j] = temporario;
        }

        return dados;
    }

    
    private void validarTamanho(int tamanho){
        if(tamanho <=0){
            throw new IllegalArgumentException("O tamanho deve ser maior que zero.");
        }
    }
}