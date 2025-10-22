package logica;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa a una persona pasajera asociada a una reserva.
 * Contiene nombre y apellido y se almacena como entidad.
 */
@Entity
@Table(name = "pasajeros")
public class Pasajero {

    /** Identificador único del pasajero generado por la BD. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPasajero;

    /** Nombre del pasajero. */
    private String nombre;
    /** Apellido del pasajero. */
    private String apellido;

    public Pasajero() {} // Constructor vacío para JPA

    /** Crea un pasajero con nombre y apellido. */
    public Pasajero(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Getters y setters
    /** Devuelve el nombre del pasajero. */
    public String getNombre() { return nombre; }
    /** Actualiza el nombre del pasajero. */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /** Devuelve el apellido del pasajero. */
    public String getApellido() { return apellido; }
    /** Actualiza el apellido del pasajero. */
    public void setApellido(String apellido) { this.apellido = apellido; }
}
