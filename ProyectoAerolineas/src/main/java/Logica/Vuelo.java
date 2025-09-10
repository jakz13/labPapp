package Logica;

import DataTypes.DtReserva;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "vuelos")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String NombreAereolinea;
    private LocalDate fecha;
    private int duracion;
    private int asientosTurista;
    private int asientosEjecutivo;
    private LocalDate fechaAlta;

    @ManyToOne
    @JoinColumn(name = "ruta_vuelo_id")
    private RutaVuelo rutaVuelo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vuelo_id")
    @MapKey(name = "id") // usa el campo 'id' de Reserva como clave del Map
    private Map<String, Reserva> reservas = new HashMap<>();

    public Vuelo() {}

    public Vuelo(String nombre, String nombreAereolinea, RutaVuelo rutaVuelo, LocalDate fecha, int duracion, int asientosTurista, int asientosEjecutivo, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.NombreAereolinea = nombreAereolinea;
        this.rutaVuelo = rutaVuelo;
        this.fecha = fecha;
        this.duracion = duracion;
        this.asientosTurista = asientosTurista;
        this.asientosEjecutivo = asientosEjecutivo;
        this.fechaAlta = fechaAlta;
    }

    // ===== Getters y Setters =====
    public String getNombre() { return nombre; }
    public String getNombreAerolinea() { return NombreAereolinea; }
    public LocalDate getFecha() { return fecha; }
    public int getDuracion() { return duracion; }
    public int getAsientosTurista() { return asientosTurista; }
    public int getAsientosEjecutivo() { return asientosEjecutivo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }
    public Map<String, Reserva> getReservas() { return reservas; }

    public void agregarReserva(String idReserva, Reserva reserva) {
        reservas.put(idReserva, reserva);
    }
    public String getRutaVueloNombre() {
        return rutaVuelo.getNombre();
    }
    @Override
    public String toString() {
        return nombre;
    }

    public List<DtReserva> getDtReservas() {
        return reservas.values().stream()
                .map(reserva -> new DtReserva(
                        reserva.getId(),
                        reserva.getFecha(),
                        reserva.getCosto(),
                        reserva.getTipoAsiento(), // Debes tener este método en Reserva
                        reserva.getCantidadPasajes(), // Debes tener este método en Reserva
                        reserva.getUnidadesEquipajeExtra(), // Debes tener este método en Reserva
                        reserva.getDtPasajeros(), // Debes tener este método en Reserva
                        this.getNombre()
                ))
                .toList();
    }

}
