
package Logica;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Vuelo {
    private String nombre;
    private String nombreRuta;
    private LocalDate fecha;
    private int duracion;
    private int asientosTurista;
    private int asientosEjecutivo;
    private LocalDate fechaAlta;
    private Map<String, Reserva> reservas; // Mapa de reservas

    public Vuelo(String nombre, String nombreRuta, String fecha, int duracion, int asientosTurista,
            int asientosEjecutivo) {
        this.nombre = nombre;
        this.nombreRuta = nombreRuta;
        this.fecha = LocalDate.parse(fecha);
        this.duracion = duracion;
        this.asientosTurista = asientosTurista;
        this.asientosEjecutivo = asientosEjecutivo;
        this.fechaAlta = LocalDate.now();
        this.reservas = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreRuta() {
        return nombreRuta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getAsientosTurista() {
        return asientosTurista;
    }

    public int getAsientosEjecutivo() {
        return asientosEjecutivo;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public Map<String, Reserva> getReservas() {
        return reservas;
    }

    public void agregarReserva(String idReserva, Reserva reserva) {
        reservas.put(idReserva, reserva);
    }
}
