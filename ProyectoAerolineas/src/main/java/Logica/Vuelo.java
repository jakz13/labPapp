package Logica;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "vuelos")
public class Vuelo {

    @Id
    @Column(nullable = false, length = 100)
    private String nombre; // clave primaria

    @ManyToOne
    @JoinColumn(name = "ruta_nombre")
    private RutaVuelo rutaVuelo;

    @Column(nullable = false)
    private LocalDate fecha;

    private int duracion;
    private int asientosTurista;
    private int asientosEjecutivo;
    private LocalDate fechaAlta;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "id") // usar id de Reserva como clave del map
    @JoinColumn(name = "vuelo_nombre")
    private Map<String, Reserva> reservas = new HashMap<>();

    public Vuelo() {}

    public Vuelo(String nombre, RutaVuelo rutaVuelo, LocalDate fecha, int duracion,
                 int asientosTurista, int asientosEjecutivo, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.rutaVuelo = rutaVuelo;
        this.fecha = fecha;
        this.duracion = duracion;
        this.asientosTurista = asientosTurista;
        this.asientosEjecutivo = asientosEjecutivo;
        this.fechaAlta = fechaAlta;
    }

    public String getNombre() { return nombre; }
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }
    public LocalDate getFecha() { return fecha; }
    public int getDuracion() { return duracion; }
    public int getAsientosTurista() { return asientosTurista; }
    public int getAsientosEjecutivo() { return asientosEjecutivo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public Map<String, Reserva> getReservas() { return reservas; }

    public void setRutaVuelo(RutaVuelo rutaVuelo) { this.rutaVuelo = rutaVuelo; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setAsientosTurista(int asientosTurista) { this.asientosTurista = asientosTurista; }
    public void setAsientosEjecutivo(int asientosEjecutivo) { this.asientosEjecutivo = asientosEjecutivo; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }

    public void agregarReserva(String idReserva, Reserva reserva) {
        reservas.put(idReserva, reserva);
    }
}

