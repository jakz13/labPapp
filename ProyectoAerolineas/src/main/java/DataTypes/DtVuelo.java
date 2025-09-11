package DataTypes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import DataTypes.DtReserva;

public class DtVuelo {
    private String nombre;
    private String nombreAerolinea;
    private LocalDate fecha;
    private int duracion;  // en minutos o en horas, según cómo lo manejes
    private int asientosTurista;
    private int asientosEjecutivo;
    private LocalDate fechaAlta;
    private DtRutaVuelo rutaVuelo;  // nombre de la ruta en lugar de la entidad completa
    private List<DtReserva> reservas; // lista de reservas asociadas

    public DtVuelo(String nombre, String nombreAerolinea, LocalDate fecha, int duracion,
                   int asientosTurista, int asientosEjecutivo, LocalDate fechaAlta,
                   DtRutaVuelo rutaVuelo, List<DtReserva> reservas) {
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
    public DtRutaVuelo getRutaVuelo() { return rutaVuelo; }
    public List<DtReserva> getReservas() {

        return reservas;
    }


    @Override
    public String toString() {
        return this.nombre;
    }


}
