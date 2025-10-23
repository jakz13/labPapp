package logica;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Representa una categoría utilizada para clasificar rutas o paquetes.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    /**
     * Nombre único de la categoría. Se usa como identificador principal.
     */
    @Id
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    /** Constructor vacío requerido por JPA. */
    public Categoria() {
        // Requerido por JPA
    }

    /** Crea una categoría con el nombre indicado. */
    public Categoria(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }

    /** Devuelve el nombre de la categoría. */
    public String getNombre() {
        return nombre;
    }

    /** Actualiza el nombre de la categoría validando su contenido. */
    public void setNombre(String nombre) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.nombre = nombre;
    }
}