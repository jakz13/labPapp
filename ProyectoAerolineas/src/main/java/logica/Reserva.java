package logica;

import DataTypes.DtPasajero;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una reserva realizada por un cliente para un vuelo, con datos de
 * pasajeros, costo, tipo de asiento y equipaje extra.
 */
@Entity
@Table(name = "reservas")
public class Reserva {

    /** Identificador único de la reserva generado por la BD. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva; //Long para auto-incremento

    /** Fecha en que se realizó la reserva. */
    private LocalDate fecha;
    /** Costo total de la reserva. */
    private double costo;

    /** Tipo de asiento reservado (TURISTA/EJECUTIVO). */
    @Enumerated(EnumType.STRING)
    private TipoAsiento tipoAsiento;

    /** Cantidad de pasajes incluidos en la reserva. */
    private int cantidadPasajes;
    /** Unidades de equipaje extra incluidas en la reserva. */
    private int unidadesEquipajeExtra;

    /** Lista de pasajeros vinculados a la reserva. */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "reserva_pasajeros",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "pasajero_id")
    )
    private List<Pasajero> pasajeros = new ArrayList<>();

    /** Vuelo asociado a la reserva. */
    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;

    public Reserva() {} // Constructor vacío para JPA

    /**
     * Constructor principal para crear una reserva (sin id, que generará la BD).
     */
    public Reserva(double costo, TipoAsiento tipoAsiento, int cantidadPasajes,
                   int unidadesEquipajeExtra, List<Pasajero> pasajeros, Vuelo vuelo) {
        this.fecha = LocalDate.now();
        this.costo = costo;
        this.tipoAsiento = tipoAsiento;
        this.cantidadPasajes = cantidadPasajes;
        this.unidadesEquipajeExtra = unidadesEquipajeExtra;
        this.pasajeros = pasajeros;
        this.vuelo = vuelo;
    }

    // ===== Getters y Setters =====
    /** Devuelve el ID de la reserva. */
    public Long getId() { return idReserva; }
    /** Establece el ID de la reserva. */
    public void setId(Long idReserva) { this.idReserva = idReserva; }
    /** Devuelve la fecha de la reserva. */
    public LocalDate getFecha() { return fecha; }
    /** Devuelve el costo de la reserva. */
    public double getCosto() { return costo; }
    /** Devuelve el tipo de asiento. */
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    /** Devuelve la cantidad de pasajes. */
    public int getCantidadPasajes() { return cantidadPasajes; }
    /** Devuelve las unidades de equipaje extra. */
    public int getUnidadesEquipajeExtra() { return unidadesEquipajeExtra; }
    /** Devuelve la lista de pasajeros. */
    public List<Pasajero> getPasajeros() { return pasajeros; }
    /** Devuelve el vuelo asociado. */
    public Vuelo getVuelo() { return vuelo; }
    /** Establece el vuelo asociado a la reserva. */
    public void setVuelo(Vuelo vuelo) { this.vuelo = vuelo; }

    @Override
    public String toString() {
        return "Reserva #" + idReserva +
                " | Vuelo: " + (vuelo != null ? vuelo.getNombre() : "N/A");
    }

    /** Devuelve la lista de pasajeros en formato DTO. */
    public List<DtPasajero> getDtPasajeros() {
        List<DtPasajero> dtPasajeros = new ArrayList<>();
        for (Pasajero p : pasajeros) {
            dtPasajeros.add(new DtPasajero(p.getNombre(), p.getApellido()));
        }
        return dtPasajeros;
    }
}