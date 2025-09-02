

import Logica.Fabrica;
import Logica.ISistema;
import gui.IntentoDiseño;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Fabrica fabrica = Fabrica.getInstance();
        ISistema sistema = fabrica.getISistema();


        // Precarga de clientes de ejemplo
        try {
            sistema.altaCliente("juan123", "Juan", "Callero", "juan@mail.com",
                    java.time.LocalDate.of(1990, 5, 10), "Uruguayo", Logica.TipoDoc.CI, "12345678");
            sistema.altaCliente("ana456", "Aglae", "Locher", "aglae@mail.com",
                    java.time.LocalDate.of(1985, 8, 22), "Argentina", Logica.TipoDoc.PASAPORTE, "A9876543");
            sistema.altaCliente("luis789", "Luis", "Martínez", "luis@mail.com",
                    java.time.LocalDate.of(1992, 12, 3), "Chileno", Logica.TipoDoc.CI, "87654321");
        } catch (Exception e) {
            // Ignorar si ya existen
        }


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

