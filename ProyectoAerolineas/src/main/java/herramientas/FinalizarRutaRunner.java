package herramientas;

import logica.Fabrica;
import logica.ISistema;

public class FinalizarRutaRunner {
    public static void main(String[] args) {
        // Ruta fija para pruebas
        String nombreRuta = "JFK-MIA";

        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            try {
                sis.cargarDesdeBd();
            } catch (Exception dbEx) {
                System.err.println("Advertencia: fallo al cargar desde BD: " + dbEx.getMessage());
                // continuamos para probar la lógica en memoria
            }

            int estado = sis.puedeFinalizarRuta(nombreRuta);
            System.out.println("puedeFinalizarRuta(\"" + nombreRuta + "\") = " + estado + " (4 == puede finalizar)");

            if (estado == 4) {
                try {
                    sis.finalizarRutaVuelo(nombreRuta);
                    System.out.println("Ruta finalizada correctamente: " + nombreRuta);
                } catch (Exception e) {
                    System.err.println("Error al finalizar la ruta: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("La ruta NO fue finalizada porque el chequeo devolvió: " + estado);
            }

        } catch (Throwable t) {
            System.err.println("Error ejecutando FinalizarRutaRunner: " + t.getMessage());
            t.printStackTrace();
            System.exit(2);
        }
    }
}
