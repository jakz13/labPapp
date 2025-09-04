

import Logica.Fabrica;
import Logica.ISistema;
import Presentacion.gui.IntentoDiseño;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Fabrica fabrica = Fabrica.getInstance();
        ISistema sistema = fabrica.getISistema();

        try {
            sistema.cargarDesdeBd();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        // Precarga de clientes de ejemplo
//        try {
//            sistema.altaCliente("juan123", "Juan", "Callero", "juan@mail.com",
//                    java.time.LocalDate.of(1990, 5, 10), "Uruguayo", Logica.TipoDoc.CI, "12345678");
//            sistema.altaCliente("ana456", "Aglae", "Locher", "aglae@mail.com",
//                    java.time.LocalDate.of(1985, 8, 22), "Argentina", Logica.TipoDoc.PASAPORTE, "A9876543");
//            sistema.altaCliente("luis789", "Luis", "Martínez", "luis@mail.com",
//                    java.time.LocalDate.of(1992, 12, 3), "Chileno", Logica.TipoDoc.CI, "87654321");
//            sistema.altaAerolinea("latam001", "LATAM Airlines", "Principal aerolínea de Latinoamérica",
//                    "contacto@latam.com", "https://www.latam.com");
//
//            sistema.altaAerolinea("iberia002", "Iberia", "Aerolínea española con vuelos internacionales",
//                    "info@iberia.com", "https://www.iberia.com");
//
//            sistema.altaAerolinea("aa003", "American Airlines", "Aerolínea estadounidense con gran cobertura global",
//                    "support@aa.com", "https://www.aa.com");
//
//            sistema.altaAerolinea("aeromex004", "AeroMéxico", "Aerolínea mexicana líder en vuelos internacionales",
//                    "clientes@aeromexico.com", "https://www.aeromexico.com");
//
//            sistema.altaAerolinea("flybondi005", "FlyBondi", "Low cost argentina con vuelos regionales",
//                    "contacto@flybondi.com", "https://www.flybondi.com");
//        } catch (Exception e) {
//        }


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

