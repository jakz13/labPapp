package Logica;

import java.time.LocalDate;

public class Reserva {
    private String id;
    private LocalDate fecha;
    private double costo;

    public Reserva(String id, LocalDate fecha, double costo) {
        this.id = id;
        this.fecha = fecha;
        this.costo = costo;
    }
    public String getId() { return id; }
    public LocalDate getFecha() { return fecha; }
    public double getCosto() { return costo; }

}
