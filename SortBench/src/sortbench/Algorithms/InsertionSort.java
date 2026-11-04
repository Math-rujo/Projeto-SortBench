package sortbench.Algorithms;

import sortbench.Model.SortMetrics;

public class InsertionSort implements SortingAlgorithm{
    
    @Override
    public String getNome(){
        return "Insertion Sort";
    }
    
    @Override
    public void ordenar(int[] dados) {

        if (dados == null || dados.length < 2) {
            return;
        }

        for (int i = 1; i < dados.length; i++) {

            int chave = dados[i];
            int j = i - 1;

            while (j >= 0 && dados[j] > chave) {
                dados[j + 1] = dados[j];
                j--;
            }

            dados[j + 1] = chave;
        }
    }

    @Override
    public void ordenar(int[] dados, SortMetrics metricas){
        if (dados == null || dados.length < 2) {
            return;
        }

        for (int i = 1; i < dados.length; i++) {
            int chave = dados[i];
            int j = i - 1;

            while (j >= 0) {
                metricas.registrarComparacao();

                if (dados[j] > chave) {
                    dados[j + 1] = dados[j];
                    metricas.registrarMovimentacao();
                    j--;
                } else {
                    break;
                }
            }
            
            dados[j + 1] = chave;
            metricas.registrarMovimentacao();
        }
    }
}
