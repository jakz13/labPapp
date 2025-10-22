package logica;

/**
 * Enum que representa los tipos de documento soportados en el sistema.
 */
public enum TipoDoc {
    CEDULAIDENTIDAD,
    PASAPORTE;

    /**
     * Indica si el valor del enum es considerado vacío. Este método está
     * incluido por conveniencia, aunque una instancia de enum nunca es null.
     * @return siempre false para valores válidos
     */
    public boolean isEmpty() {
        if (this == null) {
            return true;
        } else {
            return false;
        }
    }
}
