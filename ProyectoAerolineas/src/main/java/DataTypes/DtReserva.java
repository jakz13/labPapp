package DataTypes;

import logica.TipoAsiento;
import logica.EstadoReserva;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DtReserva {
    private Long id;
    private LocalDate fecha;
    private double costo;
    private TipoAsiento tipoAsiento;
    private int cantidadPasajes;
    private int unidadesEquipajeExtra;
    private List<DtPasajero> pasajeros;
    private String vuelo;

    private EstadoReserva estado = EstadoReserva.PENDIENTE;
    private LocalDate fechaCheckin;
    private LocalTime horaInicioEmbarque;
    private List<String> asientosAsignados = new ArrayList<>();

    // Constructor completo
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

    // Constructor desde entidad Reserva
    public DtReserva(logica.Reserva reserva) {
        this.id = reserva.getId();
        this.fecha = reserva.getFecha();
        this.costo = reserva.getCosto();
        this.tipoAsiento = reserva.getTipoAsiento();
        this.cantidadPasajes = reserva.getCantidadPasajes();
        this.unidadesEquipajeExtra = reserva.getUnidadesEquipajeExtra();
        this.pasajeros = reserva.getDtPasajeros();
        this.vuelo = reserva.getVuelo() != null ? reserva.getVuelo().getNombre() : "N/A";
        this.estado = reserva.getEstado();
        this.fechaCheckin = reserva.getFechaCheckin();
        this.horaInicioEmbarque = reserva.getHoraInicioEmbarque();
        this.asientosAsignados = reserva.getAsientosAsignados();
    }

    // ===== Getters =====
    public Long getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public double getCosto() { return costo; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    public int getCantidadPasajes() { return cantidadPasajes; }
    public int getUnidadesEquipajeExtra() { return unidadesEquipajeExtra; }
    public List<DtPasajero> getPasajeros() { return pasajeros; }
    public String getVuelo() { return vuelo; }
    public EstadoReserva getEstado() { return estado; }
    public LocalDate getFechaCheckin() { return fechaCheckin; }
    public LocalTime getHoraInicioEmbarque() { return horaInicioEmbarque; }
    public List<String> getAsientosAsignados() { return asientosAsignados; }

    @Override
    public String toString() {
        return "Reserva #" + id +
                " | Vuelo: " + vuelo +
                " | Estado: " + estado;
    }
}