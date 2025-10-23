package Logica;

import DataTypes.DtReserva;
import DataTypes.DtVuelo;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "ruta_vuelo_id")
    private RutaVuelo rutaVuelo;

    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Reserva> reservasList = new ArrayList<>();

    @Transient
    private Map<Long, Reserva> reservas = new HashMap<>(); // Cambiado a Map<Long, Reserva>

    public Vuelo() {}

    public Vuelo(String nombre, String nombreAereolinea, RutaVuelo rutaVuelo, LocalDate fecha, int duracion, int asientosTurista, int asientosEjecutivo, LocalDate fechaAlta, String imagenUrl) {
        this.nombre = nombre;
        this.NombreAereolinea = nombreAereolinea;
        this.rutaVuelo = rutaVuelo;
        this.fecha = fecha;
        this.duracion = duracion;
        this.asientosTurista = asientosTurista;
        this.asientosEjecutivo = asientosEjecutivo;
        this.fechaAlta = fechaAlta;
        this.imagenUrl = imagenUrl;
    }

    // ===== Getters y Setters =====
    public Long getId() { return id; } // Nuevo getter para el ID del vuelo
    public String getNombre() { return nombre; }
    public String getNombreAerolinea() { return NombreAereolinea; }
    public LocalDate getFecha() { return fecha; }
    public int getDuracion() { return duracion; }
    public int getAsientosTurista() { return asientosTurista; }
    public int getAsientosEjecutivo() { return asientosEjecutivo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }

    public String getImagenUrl() { return imagenUrl; }

    public Map<Long, Reserva> getReservas() {
        reservas.clear();
        for (Reserva r : reservasList) {
            reservas.put(r.getId(), r);
        }
        return reservas;
    }

    // Método actualizado para usar Long
    public void agregarReserva(Long idReserva, Reserva reserva) {
        reservasList.add(reserva);
        reservas.put(idReserva, reserva);
    }

    // Método sobrecargado que no requiere ID (usa el ID de la reserva)
    public void agregarReserva(Reserva reserva) {
        reservasList.add(reserva);
        if (reserva.getId() != null) {
            reservas.put(reserva.getId(), reserva);
        }
    }

    public List<Reserva> getReservasList() { return reservasList; }

    public String getRutaVueloNombre() {
        return rutaVuelo.getNombre();
    }

    @Override
    public String toString() {
        return nombre;
    }

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

    public DtVuelo getDtVuelo() {
        // Obtener DtRutaVuelo desde la entidad RutaVuelo (ya maneja listas de vuelos de forma segura)
        DataTypes.DtRutaVuelo dtRutaSimplificada = null;
        if (rutaVuelo != null) {
            dtRutaSimplificada = rutaVuelo.getDtRutaVuelo();
        }

        return new DtVuelo(
                this.nombre,
                this.NombreAereolinea,
                this.fecha,
                this.duracion,
                this.asientosTurista,
                this.asientosEjecutivo,
                this.fechaAlta,
                dtRutaSimplificada, // Usar la versión simplificada o null
                this.getDtReservas(),
                this.imagenUrl

        );
    }

    // Método alternativo sin información de ruta (para casos donde no se necesita)
    public DtVuelo getDtVueloSinRuta() {
        return new DtVuelo(
                this.nombre,
                this.NombreAereolinea,
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