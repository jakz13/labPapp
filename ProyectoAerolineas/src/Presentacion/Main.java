

import Logica.Fabrica;
import Logica.ISistema;
import gui.IntentoDiseño;
import javax.swing.*;
import java.time.LocalDate;

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
            sistema.altaAerolinea("latam001", "LATAM Airlines", "Principal aerolínea de Latinoamérica",
                    "contacto@latam.com", "https://www.latam.com");

            sistema.altaAerolinea("iberia002", "Iberia", "Aerolínea española con vuelos internacionales",
                    "info@iberia.com", "https://www.iberia.com");

            sistema.altaAerolinea("aa003", "American Airlines", "Aerolínea estadounidense con gran cobertura global",
                    "support@aa.com", "https://www.aa.com");

            sistema.altaAerolinea("aeromex004", "AeroMéxico", "Aerolínea mexicana líder en vuelos internacionales",
                    "clientes@aeromexico.com", "https://www.aeromexico.com");

            sistema.altaAerolinea("flybondi005", "FlyBondi", "Low cost argentina con vuelos regionales",
                    "contacto@flybondi.com", "https://www.flybondi.com");

            // --- Precarga de rutas de vuelo ---
            try {
                sistema.altaRutaVuelo("SCL-MIA", "Santiago a Miami",
                        sistema.obtenerAerolinea("latam001"),
                        "Santiago", "Miami", "08:00", LocalDate.now(),
                        500.0, 900.0, 50.0, new String[]{"Turista", "Ejecutivo"});

                sistema.altaRutaVuelo("SCL-EZE", "Santiago a Buenos Aires",
                        sistema.obtenerAerolinea("latam001"),
                        "Santiago", "Buenos Aires", "14:00", LocalDate.now(),
                        200.0, 350.0, 30.0, new String[]{"Turista", "Ejecutivo"});

                sistema.altaRutaVuelo("MAD-LHR", "Madrid a Londres",
                        sistema.obtenerAerolinea("iberia002"),
                        "Madrid", "Londres", "10:00", LocalDate.now(),
                        150.0, 300.0, 20.0, new String[]{"Turista", "Ejecutivo"});

                sistema.altaRutaVuelo("JFK-LAX", "Nueva York a Los Ángeles",
                        sistema.obtenerAerolinea("aa003"),
                        "Nueva York", "Los Ángeles", "09:30", LocalDate.now(),
                        400.0, 700.0, 40.0, new String[]{"Turista", "Ejecutivo"});

                sistema.altaRutaVuelo("JFK-MIA", "Nueva York a Miami",
                        sistema.obtenerAerolinea("aa003"),
                        "Nueva York", "Miami", "13:00", LocalDate.now(),
                        300.0, 550.0, 35.0, new String[]{"Turista", "Ejecutivo"});
            } catch (Exception e) {
                e.printStackTrace();
            }

            // --- Precarga de vuelos ---
            sistema.altaVuelo("LATAM001-SCLMIA", "LATAM Airlines", "SCL-MIA",
                    LocalDate.now(), 8, 150, 20, LocalDate.now());

            sistema.altaVuelo("LATAM002-SCLEZE", "LATAM Airlines", "SCL-EZE",
                    LocalDate.now(), 2, 100, 15, LocalDate.now());

            sistema.altaVuelo("IBERIA001-MADLHR", "Iberia", "MAD-LHR",
                    LocalDate.now(), 3, 120, 10, LocalDate.now());

            sistema.altaVuelo("AA001-JFKLAX", "American Airlines", "JFK-LAX",
                    LocalDate.now(), 6, 180, 25, LocalDate.now());

            sistema.altaVuelo("AA002-JFKMIA", "American Airlines", "JFK-MIA",
                    LocalDate.now(), 3, 160, 20, LocalDate.now());

            sistema.altaPaquete(
                    "Verano Caribeño",
                    "Paquete todo incluido con vuelos y hoteles en el Caribe.",
                    1200.0,
                    10,   // descuento 10%
                    30    // válido por 30 días
            );

            // Paquete de aventura
            sistema.altaPaquete(
                    "Aventura Andina",
                    "Incluye excursiones de trekking y escalada en la cordillera de los Andes.",
                    850.0,
                    15,   // descuento 15%
                    45    // válido por 45 días
            );



        } catch (Exception e) {
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

