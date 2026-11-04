package sortbench.Service;

public class SortValidator {

    public boolean estaOrdenado(int[] dados) {

        if (dados == null) {
            return false;
        }

        for (int i = 1; i < dados.length; i++) {

            if (dados[i - 1] > dados[i]) {
                return false;
            }
        }

        return true;
    }
    
    public boolean mesmosElementos(int[] original, int[] resultado) {
        if (original == null || resultado == null) {
            return false;
        }

        if (original.length != resultado.length) {
            return false;
        }

        int[] copiaOriginal = original.clone();
        int[] copiaResultado = resultado.clone();

        java.util.Arrays.sort(copiaOriginal);
        java.util.Arrays.sort(copiaResultado);

        return java.util.Arrays.equals(copiaOriginal, copiaResultado);
    }
}