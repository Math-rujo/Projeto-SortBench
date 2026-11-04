package sortbench;

import javax.swing.SwingUtilities;
import sortbench.UI.TelaPrincipal;

public class SortBench {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });

    }
}