package Presentacion.gui;

import Logica.Fabrica;
import Logica.ISistema;
import Logica.TipoAsiento;
import Logica.TipoDoc;
import Presentacion.gui.IntentoDiseño;
import javax.swing.*;
import java.time.LocalDate;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Fabrica fabrica = Fabrica.getInstance();
        ISistema sistema = fabrica.getISistema();

        System.out.println("🚀 Iniciando aplicación...");

        try {
            // Primero cargar desde BD si existe
            sistema.cargarDesdeBd();

            // Si no hay datos, hacer precarga completa
            if (sistema.listarClientes().isEmpty()) {
                System.out.println("📦 Base de datos vacía - ejecutando precarga...");
                precargarDatosCompletos(sistema);
            } else {
                System.out.println("✅ Datos existentes cargados desde BD");
            }

            System.out.println("🎉 Aplicación iniciada correctamente!");

        } catch (Exception e) {
            System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
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

    public static void precargarDatosCompletos(ISistema sistema) {
        try {
            System.out.println("🚀 Iniciando precarga de datos...");

            // ===== 1. PRECARGAR CATEGORÍAS =====
            sistema.altaCategoria("Turista");
            sistema.altaCategoria("Ejecutivo");
            System.out.println("✅ Categorías creadas");

            // ===== 2. PRECARGAR CIUDADES =====
            sistema.altaCiudad("Santiago", "Chile");
            sistema.altaCiudad("Miami", "Estados Unidos");
            sistema.altaCiudad("Buenos Aires", "Argentina");
            sistema.altaCiudad("Madrid", "España");
            sistema.altaCiudad("Londres", "Reino Unido");
            sistema.altaCiudad("Nueva York", "Estados Unidos");
            sistema.altaCiudad("Los Ángeles", "Estados Unidos");
            System.out.println("✅ Ciudades creadas");

            // ===== 3. PRECARGAR CLIENTES =====
            sistema.altaCliente("juan123", "Juan", "Callero", "juan@mail.com",
                    LocalDate.of(1990, 5, 10), "Uruguayo", TipoDoc.CI,
                    "12345678", "pwdJuan", null);

            sistema.altaCliente("ana456", "Aglae", "Locher", "aglae@mail.com",
                    LocalDate.of(1985, 8, 22), "Argentina", TipoDoc.PASAPORTE,
                    "A9876543", "pwdAna", null);

            sistema.altaCliente("luis789", "Luis", "Martínez", "luis@mail.com",
                    LocalDate.of(1992, 12, 3), "Chileno", TipoDoc.CI,
                    "87654321", "pwdLuis", null);
            System.out.println("✅ Clientes creados");

            // ===== 4. PRECARGAR AEROLÍNEAS =====
            sistema.altaAerolinea("latam001", "LATAM Airlines", "Principal aerolínea de Latinoamérica",
                    "contacto@latam.com", "https://www.latam.com", "pwdLatam", null);

            sistema.altaAerolinea("iberia002", "Iberia", "Aerolínea española con vuelos internacionales",
                    "info@iberia.com", "https://www.iberia.com", "pwdIberia", null);

            sistema.altaAerolinea("aa003", "American Airlines", "Aerolínea estadounidense con gran cobertura global",
                    "support@aa.com", "https://www.aa.com", "pwdAA", null);

            sistema.altaAerolinea("aeromex004", "AeroMéxico", "Aerolínea mexicana líder en vuelos internacionales",
                    "clientes@aeromexico.com", "https://www.aeromexico.com", "pwdAeroMex", null);

            sistema.altaAerolinea("flybondi005", "FlyBondi", "Low cost argentina con vuelos regionales",
                    "contacto@flybondi.com", "https://www.flybondi.com", "pwdFlybondi", null);
            System.out.println("✅ Aerolíneas creadas");

            sistema.altaRutaVuelo("SCL-MIA", "Santiago a Miami", "Directo",
                    sistema.obtenerAerolinea("latam001"),
                    "Santiago", "Miami", "08:00", LocalDate.now(),
                    500.0, 900.0, 50.0, new String[]{"Turista", "Ejecutivo"}, null );

            sistema.altaRutaVuelo("SCL-EZE", "Santiago a Buenos Aires", "Directo",
                    sistema.obtenerAerolinea("latam001"),
                    "Santiago", "Buenos Aires", "14:00", LocalDate.now(),
                    200.0, 350.0, 30.0, new String[]{"Turista", "Ejecutivo"}, null );

            sistema.altaRutaVuelo("MAD-LHR", "Madrid a Londres", "Directo",
                    sistema.obtenerAerolinea("iberia002"),
                    "Madrid", "Londres", "10:00", LocalDate.now(),
                    150.0, 300.0, 20.0, new String[]{"Turista", "Ejecutivo"}, null);

            sistema.altaRutaVuelo("JFK-LAX", "Nueva York a Los Ángeles", "Directo",
                    sistema.obtenerAerolinea("aa003"),
                    "Nueva York", "Los Ángeles", "09:30", LocalDate.now(),
                    400.0, 700.0, 40.0, new String[]{"Turista", "Ejecutivo"}, null);

            sistema.altaRutaVuelo("JFK-MIA", "Nueva York a Miami", "Directo",
                    sistema.obtenerAerolinea("aa003"),
                    "Nueva York", "Miami", "13:00", LocalDate.now(),
                    300.0, 550.0, 35.0, new String[]{"Turista", "Ejecutivo"}, null);
            System.out.println("✅ Rutas de vuelo creadas");

            // ===== 6. CONFIRMAR RUTAS (para que estén activas) =====
            sistema.aceptarRutaVuelo("SCL-MIA");
            sistema.aceptarRutaVuelo("SCL-EZE");
            sistema.aceptarRutaVuelo("MAD-LHR");
            sistema.aceptarRutaVuelo("JFK-LAX");
            sistema.aceptarRutaVuelo("JFK-MIA");
            System.out.println("✅ Rutas confirmadas");

            // ===== 7. PRECARGAR VUELOS =====
            sistema.altaVuelo("LATAM001-SCLMIA", "LATAM Airlines", "SCL-MIA",
                    LocalDate.now().plusDays(7), 8, 150, 20, LocalDate.now(),null);

            sistema.altaVuelo("LATAM002-SCLEZE", "LATAM Airlines", "SCL-EZE",
                    LocalDate.now().plusDays(5), 2, 100, 15, LocalDate.now(),null);

            sistema.altaVuelo("IBERIA001-MADLHR", "Iberia", "MAD-LHR",
                    LocalDate.now().plusDays(10), 3, 120, 10, LocalDate.now(),null);

            sistema.altaVuelo("AA001-JFKLAX", "American Airlines", "JFK-LAX",
                    LocalDate.now().plusDays(14), 6, 180, 25, LocalDate.now(),null);

            sistema.altaVuelo("AA002-JFKMIA", "American Airlines", "JFK-MIA",
                    LocalDate.now().plusDays(3), 3, 160, 20, LocalDate.now(),null);
            System.out.println("✅ Vuelos creados");

            // ===== 8. PRECARGAR PAQUETES =====
            sistema.altaPaquete("Verano Caribeño",
                    "Paquete todo incluido con vuelos y hoteles en el Caribe.",
                    10, 30);

            sistema.altaPaquete("Aventura Andina",
                    "Incluye excursiones de trekking y escalada en la cordillera de los Andes.",
                    15, 45);
            System.out.println("✅ Paquetes creados");

            // ===== 9. AGREGAR RUTAS A PAQUETES =====
            sistema.altaRutaPaquete("Verano Caribeño", "SCL-MIA", 2, TipoAsiento.TURISTA);
            sistema.altaRutaPaquete("Aventura Andina", "SCL-EZE", 1, TipoAsiento.EJECUTIVO);
            System.out.println("✅ Rutas agregadas a paquetes");

            // ===== 10. CREAR RESERVAS DE PRUEBA =====
            System.out.println("📝 Creando reservas de prueba...");

            // Reserva para Ana en el vuelo LATAM001-SCLMIA
            sistema.crearYRegistrarReserva(
                    "ana456",
                    "LATAM001-SCLMIA",
                    LocalDate.now(),
                    sistema.calcularCostoReserva("LATAM001-SCLMIA", TipoAsiento.TURISTA, 1, 0),
                    TipoAsiento.TURISTA,
                    1,
                    0,
                    Arrays.asList(sistema.crearPasajero("Aglae", "Locher"))
            );

            // Reserva para Juan en el vuelo AA001-JFKLAX
            sistema.crearYRegistrarReserva(
                    "juan123",
                    "AA001-JFKLAX",
                    LocalDate.now(),
                    sistema.calcularCostoReserva("AA001-JFKLAX", TipoAsiento.EJECUTIVO, 2, 1),
                    TipoAsiento.EJECUTIVO,
                    2,
                    1,
                    Arrays.asList(sistema.crearPasajero("Juan", "Callero"))
            );

            System.out.println("✅ Reservas de prueba creadas exitosamente!");
            System.out.println("🎉 Precarga de datos completada exitosamente!");

        } catch (Exception e) {
            System.err.println("❌ Error en precarga: " + e.getMessage());
            e.printStackTrace();
        }


    }
}