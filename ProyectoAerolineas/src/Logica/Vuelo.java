

import java.time.LocalDate;

public class Vuelo {
    private String nombre;
    private LocalDate fecha;
    private int duracion;

    public Vuelo(String nombre, LocalDate fecha, int duracion) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.duracion = duracion;
    }
    public String getNombre() { return nombre; }
    public LocalDate getFecha() { return fecha; }
    public int getDuracion() { return duracion; }
}
