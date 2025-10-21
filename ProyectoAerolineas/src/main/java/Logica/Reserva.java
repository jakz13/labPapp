package Logica;

import DataTypes.DtPasajero;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Long para auto-incremento

    private LocalDate fecha;
    private double costo;

    @Enumerated(EnumType.STRING)
    private TipoAsiento tipoAsiento;

    private int cantidadPasajes;
    private int unidadesEquipajeExtra;

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

    // Nueva relación ManyToOne hacia Cliente para poder saber el dueño de la reserva
    @ManyToOne
    @JoinColumn(name = "cliente_nickname", referencedColumnName = "nickname")
    private Cliente cliente;

    public Reserva() {} // Constructor vacío para JPA

    // Constructor modificado - sin ID
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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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

    public String toString() {
        return "Reserva #" + id +
                " | Vuelo: " + (vuelo != null ? vuelo.getNombre() : "N/A");
    }

    public List<DtPasajero> getDtPasajeros() {
        List<DtPasajero> dtPasajeros = new ArrayList<>();
        for (Pasajero p : pasajeros) {
            dtPasajeros.add(new DtPasajero(p.getNombre(), p.getApellido()));
        }
        return dtPasajeros;
    }
}