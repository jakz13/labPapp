package DataTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class DtVuelo {
    private String nombre;
    private String nombreAerolinea;
    private LocalDate fecha;
    private int duracion;  // en minutos o en horas, según cómo lo manejes
    private int asientosTurista;
    private int asientosEjecutivo;
    private LocalDate fechaAlta;
    private String rutaVuelo;  // nombre de la ruta en lugar de la entidad completa
    private List<DtReserva> reservas; // lista de reservas asociadas

    public DtVuelo(String nombre, String nombreAerolinea, LocalDate fecha, int duracion,
                   int asientosTurista, int asientosEjecutivo, LocalDate fechaAlta,
                   String rutaVuelo, List<DtReserva> reservas) {
        this.nombre = nombre;
        this.nombreAerolinea = nombreAerolinea;
        this.fecha = fecha;
        this.duracion = duracion;
        this.asientosTurista = asientosTurista;
        this.asientosEjecutivo = asientosEjecutivo;
        this.fechaAlta = fechaAlta;
        this.rutaVuelo = rutaVuelo;
        this.reservas = reservas;
    }

    // ===== Getters =====

    public String getNombre() { return nombre; }
    public String getNombreAerolinea() { return nombreAerolinea; }
    public LocalDate getFecha() { return fecha; }
    public int getDuracion() { return duracion; }
    public int getAsientosTurista() { return asientosTurista; }
    public int getAsientosEjecutivo() { return asientosEjecutivo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public String getRutaVuelo() { return rutaVuelo; }
    public List<DtReserva> getReservas() {


        return reservas; }



    @Override
    public String toString() {
        return "Vuelo: " + nombre +
                " | Aerolínea: " + nombreAerolinea +
                " | Fecha: " + fecha +
                " | Ruta: " + rutaVuelo +
                " | Duración: " + duracion + " min" +
                " | Asientos T: " + asientosTurista +
                " | Asientos E: " + asientosEjecutivo;
    }
}
