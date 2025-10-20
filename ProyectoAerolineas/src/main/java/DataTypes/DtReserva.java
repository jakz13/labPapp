package DataTypes;

import Logica.TipoAsiento;

import java.time.LocalDate;
import java.util.List;

public class DtReserva {
    private Long id;  // Cambiado de String a Long
    private LocalDate fecha;
    private double costo;
    private TipoAsiento tipoAsiento;
    private int cantidadPasajes;
    private int unidadesEquipajeExtra;
    private List<DtPasajero> pasajeros;
    private String vuelo; // nombre del vuelo, para no exponer la entidad completa

    // Constructor con Long id
    public DtReserva(Long id, LocalDate fecha, double costo, TipoAsiento tipoAsiento,
                     int cantidadPasajes, int unidadesEquipajeExtra,
                     List<DtPasajero> pasajeros, String vuelo) {
        this.id = id;
        this.fecha = fecha;
        this.costo = costo;
        this.tipoAsiento = tipoAsiento;
        this.cantidadPasajes = cantidadPasajes;
        this.unidadesEquipajeExtra = unidadesEquipajeExtra;
        this.pasajeros = pasajeros;
        this.vuelo = vuelo;
    }

    // Constructor alternativo que acepta la entidad Reserva
    public DtReserva(Logica.Reserva reserva) {
        this.id = reserva.getId();
        this.fecha = reserva.getFecha();
        this.costo = reserva.getCosto();
        this.tipoAsiento = reserva.getTipoAsiento();
        this.cantidadPasajes = reserva.getCantidadPasajes();
        this.unidadesEquipajeExtra = reserva.getUnidadesEquipajeExtra();
        this.pasajeros = reserva.getDtPasajeros();
        this.vuelo = reserva.getVuelo() != null ? reserva.getVuelo().getNombre() : "N/A";
    }

    // ===== Getters =====
    public Long getId() { return id; }  // Cambiado a Long
    public LocalDate getFecha() { return fecha; }
    public double getCosto() { return costo; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    public int getCantidadPasajes() { return cantidadPasajes; }
    public int getUnidadesEquipajeExtra() { return unidadesEquipajeExtra; }
    public List<DtPasajero> getPasajeros() { return pasajeros; }
    public String getVuelo() { return vuelo; }

    @Override
    public String toString() {
        return "Reserva #" + id +  // Ahora muestra el ID numérico
                " | Vuelo: " + vuelo;
    }
}