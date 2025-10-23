package DataTypes;

import java.time.LocalDate;
import java.util.List;

public class DtVuelo {
    private String nombre;
    private String nombreAerolinea;
    private LocalDate fecha;
    private int duracion;
    private int asientosTurista;
    private int asientosEjecutivo;
    private LocalDate fechaAlta;
    private DtRutaVuelo rutaVuelo;
    private List<DtReserva> reservas;
    private String imagenUrl;

    public DtVuelo(String nombre, String nombreAerolinea, LocalDate fecha, int duracion,
                   int asientosTurista, int asientosEjecutivo, LocalDate fechaAlta,
                   DtRutaVuelo rutaVuelo, List<DtReserva> reservas, String imagenUrl) {
        this.nombre = nombre;
        this.nombreAerolinea = nombreAerolinea;
        this.fecha = fecha;
        this.duracion = duracion;
        this.asientosTurista = asientosTurista;
        this.asientosEjecutivo = asientosEjecutivo;
        this.fechaAlta = fechaAlta;
        this.rutaVuelo = rutaVuelo;
        this.reservas = reservas;
        this.imagenUrl = imagenUrl;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getNombreAerolinea() { return nombreAerolinea; }
    public LocalDate getFecha() { return fecha; }
    public int getDuracion() { return duracion; }
    public int getAsientosTurista() { return asientosTurista; }
    public int getAsientosEjecutivo() { return asientosEjecutivo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public DtRutaVuelo getRutaVuelo() { return rutaVuelo; }
    public List<DtReserva> getReservas() { return reservas; }
    public String getImagenUrl() { return imagenUrl; }

    @Override
    public String toString() {
        return nombre + " - " + fecha + " (" + nombreAerolinea + ")";
    }
}