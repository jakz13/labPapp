

import Logica.Fabrica;
import Logica.ISistema;
import gui.IntentoDiseño;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Fabrica fabrica = Fabrica.getInstance();
        ISistema sistema = fabrica.getISistema();

        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ventana Principal");
            IntentoDiseño ventanaPrincipal = new IntentoDiseño(frame);
            frame.setContentPane(ventanaPrincipal.Ventana);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}
