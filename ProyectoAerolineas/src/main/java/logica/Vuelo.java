package logica;

import DataTypes.DtReserva;
import DataTypes.DtVuelo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa un vuelo programado en el sistema, con sus asientos, fecha y reservas asociadas.
 */
@Entity
@Table(name = "vuelos")
public class Vuelo {

    /** Identificador interno del vuelo generado por la BD. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVuelo;

    /** Nombre identificador del vuelo (p. ej. código). */
    private String nombre;
    /** Nickname de la aerolínea propietaria del vuelo. */
    private String nombreAereolinea;
    /** Fecha programada del vuelo. */
    private LocalDate fecha;
    /** Duración del vuelo en minutos u otra unidad acordada. */
    private int duracion;
    /** Número de asientos en clase turista disponibles en el vuelo. */
    private int asientosTurista;
    /** Número de asientos en clase ejecutivo disponibles en el vuelo. */
    private int asientosEjecutivo;
    /** Fecha de alta del vuelo en el sistema. */
    private LocalDate fechaAlta;
    private String imagenUrl;

    /** Ruta a la que pertenece este vuelo. */
    @ManyToOne
    @JoinColumn(name = "ruta_vuelo_id")
    private RutaVuelo rutaVuelo;

    /** Lista persistente de reservas asociadas al vuelo. */
    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reserva> reservasList = new ArrayList<>();

    /** Mapa en memoria de reservas indexado por ID (no persistido). */
    @Transient
    private Map<Long, Reserva> reservas = new HashMap<>(); // Cambiado a Map<Long, Reserva>

    public Vuelo() {}

    /**
     * Crea un vuelo con sus parámetros principales.
     */
    public Vuelo(String nombre, String nombreAereolinea, RutaVuelo rutaVuelo, LocalDate fecha, int duracion, int asientosTurista, int asientosEjecutivo, LocalDate fechaAlta, String imagenUrl) {
        this.nombre = nombre;
        this.nombreAereolinea = nombreAereolinea;
        this.rutaVuelo = rutaVuelo;
        this.fecha = fecha;
        this.duracion = duracion;
        this.asientosTurista = asientosTurista;
        this.asientosEjecutivo = asientosEjecutivo;
        this.fechaAlta = fechaAlta;
        this.imagenUrl = imagenUrl;
    }

    // ===== Getters y Setters =====
    /** Devuelve el ID del vuelo. */
    public Long getId() { return idVuelo; } // Nuevo getter para el ID del vuelo
    /** Devuelve el nombre del vuelo. */
    public String getNombre() { return nombre; }
    /** Devuelve el nickname de la aerolínea. */
    public String getNombreAerolinea() { return nombreAereolinea; }
    /** Devuelve la fecha programada del vuelo. */
    public LocalDate getFecha() { return fecha; }
    /** Devuelve la duración del vuelo. */
    public int getDuracion() { return duracion; }
    /** Devuelve la cantidad de asientos en turista. */
    public int getAsientosTurista() { return asientosTurista; }
    /** Devuelve la cantidad de asientos en ejecutivo. */
    public int getAsientosEjecutivo() { return asientosEjecutivo; }
    /** Devuelve la fecha de alta del vuelo. */
    public LocalDate getFechaAlta() { return fechaAlta; }
    /** Devuelve la ruta asociada al vuelo. */
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }

    /**
     * Sincroniza y devuelve el mapa de reservas (ID -> Reserva).
     * @return mapa con reservas indexadas por su ID
     */
    public Map<Long, Reserva> getReservas() {
        reservas.clear();
        for (Reserva r : reservasList) {
            if (r.getId() != null) {
                reservas.put(r.getId(), r);
            }
        }
        return reservas;
    }

    /** Agrega una reserva con ID explícito. */
    public void agregarReserva(Long idReserva, Reserva reserva) {
        reservasList.add(reserva);
        reservas.put(idReserva, reserva);
    }

    /** Agrega una reserva; si la reserva tiene ID, la indexa en el mapa. */
    public void agregarReserva(Reserva reserva) {
        reservasList.add(reserva);
        if (reserva.getId() != null) {
            reservas.put(reserva.getId(), reserva);
        }
    }

    /** Devuelve la lista persistente de reservas. */
    public List<Reserva> getReservasList() { return reservasList; }

    /** Devuelve el nombre de la ruta asociada al vuelo. */
    public String getRutaVueloNombre() {
        return rutaVuelo.getNombre();
    }

    @Override
    public String toString() {
        return nombre;
    }

    /** Convierte las reservas a DTOs para transferencia. */
    public List<DtReserva> getDtReservas() {
        return reservasList.stream()
                .map(reserva -> new DtReserva(
                        reserva.getId(), // Ahora pasa Long directamente
                        reserva.getFecha(),
                        reserva.getCosto(),
                        reserva.getTipoAsiento(),
                        reserva.getCantidadPasajes(),
                        reserva.getUnidadesEquipajeExtra(),
                        reserva.getDtPasajeros(),
                        this.getNombre()
                ))
                .toList();
    }

    /**
     * Genera y devuelve un DTO del vuelo incluyendo datos simplificados de la ruta.
     * @return DtVuelo con información principal del vuelo
     */
    public DtVuelo getDtVuelo() {
        // Crear un DtRutaVuelo simplificado sin vuelos para evitar recursión
        DataTypes.DtRutaVuelo dtRutaSimplificada = new DataTypes.DtRutaVuelo(
                rutaVuelo.getNombre(),
                rutaVuelo.getDescripcion(),
                rutaVuelo.getDescripcionCorta(),
                rutaVuelo.getAerolinea() != null ? rutaVuelo.getAerolinea().getNombre() : null,
                rutaVuelo.getCiudadOrigen(),
                rutaVuelo.getCiudadDestino(),
                rutaVuelo.getHora(),
                rutaVuelo.getFechaAlta(),
                rutaVuelo.getCostoTurista(),
                rutaVuelo.getCostoEjecutivo(),
                rutaVuelo.getCostoEquipajeExtra(),
                rutaVuelo.getEstado() != null ? rutaVuelo.getEstado().toString() : "INGRESADA",
                rutaVuelo.getCategorias() != null ? rutaVuelo.getCategorias() : new ArrayList<>(),
                new ArrayList<>() // Lista vacía de vuelos para evitar recursión
        );

        return new DtVuelo(
                this.nombre,
                this.nombreAereolinea,
                this.fecha,
                this.duracion,
                this.asientosTurista,
                this.asientosEjecutivo,
                this.fechaAlta,
                dtRutaSimplificada, // Usar la versión simplificada
                this.getDtReservas(),
                this.imagenUrl
        );
    }

    /** Devuelve un DTO del vuelo sin información de la ruta (para evitar detalles). */
    public DtVuelo getDtVueloSinRuta() {
        return new DtVuelo(
                this.nombre,
                this.nombreAereolinea,
                this.fecha,
                this.duracion,
                this.asientosTurista,
                this.asientosEjecutivo,
                this.fechaAlta,
                null, // Sin información de ruta
                this.getDtReservas(),
                this.imagenUrl
        );
    }
}