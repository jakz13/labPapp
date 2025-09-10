package Logica;

import java.time.LocalDate;
import java.util.List;

public class Reserva {
    private String id;
    private LocalDate fecha;
    private double costo;
    private TipoAsiento tipoAsiento;
    private int cantidadPasajes;
    private int unidadesEquipajeExtra;
    private List<Pasajero> pasajeros;
    private Vuelo vuelo;

    public Reserva(String id, double costo, TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra, List<Pasajero> pasajeros, Vuelo vuelo) {
        this.id = id;
        this.fecha = LocalDate.now();
        this.costo = costo;
        this.tipoAsiento = tipoAsiento;
        this.cantidadPasajes = cantidadPasajes;
        this.unidadesEquipajeExtra = unidadesEquipajeExtra;
        this.pasajeros = pasajeros;
        this.vuelo = vuelo;
    }

    public String getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public double getCosto() { return costo; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }
    public int getCantidadPasajes() { return cantidadPasajes; }
    public int getUnidadesEquipajeExtra() { return unidadesEquipajeExtra; }
    public List<Pasajero> getPasajeros() { return pasajeros; }
    public Vuelo getVuelo() { return vuelo; };
    public String toString() {
        return "Reserva #" + id +
                " | Vuelo: " + (vuelo != null ? vuelo.getNombre() : "N/A");
    }
}
