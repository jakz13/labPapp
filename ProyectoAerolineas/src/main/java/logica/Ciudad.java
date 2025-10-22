package logica;

//import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;


/**
 * Representa una ciudad con información básica (país, aeropuerto, descripción y fecha de alta).
 */
@Entity
@Table(name = "ciudades")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCiudad; // clave primaria autoincremental

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String pais;

    private String aeropuerto;
    private String descripcion;
    private String sitioWeb;

    @Column(name = "fecha_alta")
    private LocalDate fechaAlta;

    public Ciudad() {
    }

    /** Crea una ciudad con todos los datos. */
    public Ciudad(String nombre, String pais, String aeropuerto, String descripcion, String sitioWeb, LocalDate fechaAlta) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser null");
        }
        if (pais == null) {
            throw new IllegalArgumentException("El pais de la ciudad no puede ser null");
        }
        this.nombre = nombre;
        this.pais = pais;
        this.aeropuerto = aeropuerto;
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.fechaAlta = fechaAlta;
    }

    /** Crea una ciudad con nombre y país. */
    public Ciudad(String nombre, String pais) {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser null");
        }
        if (pais == null) {
            throw new IllegalArgumentException("El pais de la ciudad no puede ser null");
        }
        this.nombre = nombre;
        this.pais = pais;
    }

    // --- Getters y Setters ---
    /** Devuelve el id de la ciudad. */
    public Long getId() { return idCiudad; }
    /** Devuelve el nombre de la ciudad. */
    public String getNombre() { return nombre; }
    /** Actualiza el nombre de la ciudad. */
    public void setNombre(String nombre) { this.nombre = nombre; }
    /** Devuelve el país de la ciudad. */
    public String getPais() { return pais; }
    /** Actualiza el país de la ciudad. */
    public void setPais(String pais) { this.pais = pais; }
    /** Devuelve el nombre del aeropuerto, si existe. */
    public String getAeropuerto() { return aeropuerto; }
    /** Actualiza el aeropuerto de la ciudad. */
    public void setAeropuerto(String aeropuerto) { this.aeropuerto = aeropuerto; }
    /** Devuelve la descripción de la ciudad. */
    public String getDescripcion() { return descripcion; }
    /** Actualiza la descripción de la ciudad. */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    /** Devuelve la URL del sitio web de la ciudad. */
    public String getSitioWeb() { return sitioWeb; }
    /** Actualiza la URL del sitio web de la ciudad. */
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }
    /** Devuelve la fecha de alta de la ciudad. */
    public LocalDate getFechaAlta() { return fechaAlta; }
    /** Actualiza la fecha de alta de la ciudad. */
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;           // misma referencia → iguales
        if (obj == null || getClass() != obj.getClass()) return false;
        Ciudad ciudad = (Ciudad) obj;
        return Objects.equals(nombre, ciudad.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

}
