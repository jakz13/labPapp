package logica;

import DataTypes.DtPasajero;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "cliente_nickname", referencedColumnName = "nickname")
    private Cliente cliente;

    private LocalDate fecha;
    private double costo;

    @Enumerated(EnumType.STRING)
    private TipoAsiento tipoAsiento;

    private int cantidadPasajes;
    private int unidadesEquipajeExtra;

    // Nuevos campos para check-in
    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.PENDIENTE; // Valor por defecto

    private LocalDate fechaCheckin;
    private LocalTime horaInicioEmbarque;

    // Asientos asignados durante el check-in
    @ElementCollection
    @CollectionTable(name = "reserva_asientos", joinColumns = @JoinColumn(name = "reserva_id"))
    @Column(name = "asiento")
    private List<String> asientosAsignados = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "reserva_pasajeros",
            joinColumns = @JoinColumn(name = "reserva_id"),
            inverseJoinColumns = @JoinColumn(name = "pasajero_id")
    )
    private List<Pasajero> pasajeros = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    private Vuelo vuelo;

    public Reserva() {}

    public Reserva(double costo, TipoAsiento tipoAsiento, int cantidadPasajes,
                   int unidadesEquipajeExtra, List<Pasajero> pasajeros, Vuelo vuelo) {
        this.fecha = LocalDate.now();
        this.costo = costo;
        this.tipoAsiento = tipoAsiento;
        this.cantidadPasajes = cantidadPasajes;
        this.unidadesEquipajeExtra = unidadesEquipajeExtra;
        this.pasajeros = pasajeros;
        this.vuelo = vuelo;
        this.estado = EstadoReserva.PENDIENTE; // Estado inicial
    }

    // ===== Métodos para check-in =====

    /**
     * Realiza el check-in de la reserva asignando asientos
     */
    public void realizarCheckin(List<String> asientos, LocalTime horaEmbarque) {
        if (this.estado == EstadoReserva.CHECKIN_REALIZADO) {
            throw new IllegalStateException("El check-in ya fue realizado para esta reserva");
        }

        if (asientos.size() != this.cantidadPasajes) {
            throw new IllegalArgumentException("La cantidad de asientos debe coincidir con la cantidad de pasajes");
        }

        this.asientosAsignados = new ArrayList<>(asientos);
        this.fechaCheckin = LocalDate.now();
        this.horaInicioEmbarque = horaEmbarque;
        this.estado = EstadoReserva.CHECKIN_REALIZADO;
    }

    /**
     * Obtiene la información del check-in realizado
     */
    public String getInfoCheckin() {
        if (this.estado != EstadoReserva.CHECKIN_REALIZADO) {
            return "Check-in pendiente";
        }

        StringBuilder info = new StringBuilder();
        info.append("Check-in realizado el: ").append(fechaCheckin).append("\n");
        info.append("Hora de embarque: ").append(horaInicioEmbarque).append("\n");
        info.append("Asientos asignados: ").append(String.join(", ", asientosAsignados));

        return info.toString();
    }

    // ===== Getters y Setters =====
    public Long getId() { return idReserva; }
    public void setId(Long idReserva) { this.idReserva = idReserva; }
    public LocalDate getFecha() { return fecha; }
    public double getCosto() { return costo; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    public int getCantidadPasajes() { return cantidadPasajes; }
    public int getUnidadesEquipajeExtra() { return unidadesEquipajeExtra; }
    public List<Pasajero> getPasajeros() { return pasajeros; }
    public Vuelo getVuelo() { return vuelo; }
    public void setVuelo(Vuelo vuelo) { this.vuelo = vuelo; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    // Nuevos getters y setters
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public LocalDate getFechaCheckin() { return fechaCheckin; }
    public LocalTime getHoraInicioEmbarque() { return horaInicioEmbarque; }
    public List<String> getAsientosAsignados() { return asientosAsignados; }

    @Override
    public String toString() {
        return "Reserva #" + idReserva +
                " | Vuelo: " + (vuelo != null ? vuelo.getNombre() : "N/A") +
                " | Estado: " + estado;
    }

    public List<DtPasajero> getDtPasajeros() {
        List<DtPasajero> dtPasajeros = new ArrayList<>();
        for (Pasajero p : pasajeros) {
            dtPasajeros.add(new DtPasajero(p.getNombre(), p.getApellido()));
        }
        return dtPasajeros;
    }

    public boolean getCheckinRealizado() {
        return this.estado == EstadoReserva.CHECKIN_REALIZADO;
    }
}