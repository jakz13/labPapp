

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import gui.IntentoDiseño;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
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
