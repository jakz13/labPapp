package Logica;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @Column(length = 50)
    private String id; // se pasa por constructor

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private double costo;

    @Enumerated(EnumType.STRING)
    private TipoAsiento tipoAsiento;

    private int cantidadPasajes;
    private int unidadesEquipajeExtra;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // Esto crea la FK cliente_id en reservas
    private Cliente cliente;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "reserva_id") // FK en la tabla pasajero
    private List<Pasajero> pasajeros;

    public Reserva() {
        this.fecha = LocalDate.now();
    }

    public Reserva(String id, double costo, TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra, List<Pasajero> pasajeros) {
        this.id = id;
        this.fecha = LocalDate.now();
        this.costo = costo;
        this.tipoAsiento = tipoAsiento;
        this.cantidadPasajes = cantidadPasajes;
        this.unidadesEquipajeExtra = unidadesEquipajeExtra;
        this.pasajeros = pasajeros;
    }

    public String getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public double getCosto() { return costo; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    public int getCantidadPasajes() { return cantidadPasajes; }
    public int getUnidadesEquipajeExtra() { return unidadesEquipajeExtra; }
    public List<Pasajero> getPasajeros() { return pasajeros; }

    public void setCosto(double costo) { this.costo = costo; }
    public void setTipoAsiento(TipoAsiento tipoAsiento) { this.tipoAsiento = tipoAsiento; }
    public void setCantidadPasajes(int cantidadPasajes) { this.cantidadPasajes = cantidadPasajes; }
    public void setUnidadesEquipajeExtra(int unidadesEquipajeExtra) { this.unidadesEquipajeExtra = unidadesEquipajeExtra; }
    public void setPasajeros(List<Pasajero> pasajeros) { this.pasajeros = pasajeros; }
    public Cliente getCliente() {
        return cliente;
    }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}

