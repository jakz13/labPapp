package logica;

/**
 * Fábrica singleton que expone la instancia del sistema (ISistema).
 * Se usa para obtener acceso centralizado al servicio de lógica.
 */
public final class Fabrica {
    private static volatile Fabrica instancia = null;
    private final ISistema sistema;

    private Fabrica() {
        sistema = new Sistema(); // Asumiendo que Logica.Sistema implementa Logica.ISistema
    }

    /** Devuelve la instancia singleton de la fábrica. */
    public static Fabrica getInstance() {
        if (instancia == null) {
            synchronized (Fabrica.class) {
                if (instancia == null) {
                    instancia = new Fabrica();
                }
            }
        }
        return instancia;
    }

    /** Devuelve la interfaz del sistema (ISistema). */
    public ISistema getISistema() {
        return sistema;
    }
}
