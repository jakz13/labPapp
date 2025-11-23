package DataTypes;

import logica.EstadoRuta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DtRutaVuelo {
    private String nombre;
    private String descripcion;
    private String descripcionCorta;
    private String imagenUrl;
    private String videoUrl;
    private String aerolinea;
    private String ciudadOrigen;
    private String ciudadDestino;
    private String hora;
    private LocalDate fechaAlta;
    private double costoTurista;
    private double costoEjecutivo;
    private double costoEquipajeExtra;
    private EstadoRuta estado;
    private List<String> categorias;
    private List<DtVuelo> vuelos;
    private int contadorVisitas;


    public DtRutaVuelo(String nombre, String descripcion, String descripcionCorta,
                       String imagenUrl, String videoUrl, String aerolinea,
                       String ciudadOrigen, String ciudadDestino, String hora,
                       LocalDate fechaAlta, double costoTurista, double costoEjecutivo,
                       double costoEquipajeExtra, EstadoRuta estado, List<String> categorias,
                       List<DtVuelo> vuelos, int contadorVisitas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descripcionCorta = descripcionCorta;
        this.imagenUrl = imagenUrl;
        this.videoUrl = videoUrl;
        this.aerolinea = aerolinea;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEjecutivo = costoEjecutivo;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.estado = estado;
        this.categorias = categorias;
        this.vuelos = vuelos;
        this.contadorVisitas = contadorVisitas;
    }

    public DtRutaVuelo(String nombre, String descripcion, String descripcionCorta,
                       String imagenUrl, String videoUrl, String aerolinea,
                       String ciudadOrigen, String ciudadDestino, String hora,
                       LocalDate fechaAlta, double costoTurista, double costoEjecutivo,
                       double costoEquipajeExtra, EstadoRuta estado, List<String> categorias,
                       List<DtVuelo> vuelos) {
        this(nombre, descripcion, descripcionCorta, imagenUrl, videoUrl, aerolinea,
                ciudadOrigen, ciudadDestino, hora, fechaAlta, costoTurista, costoEjecutivo,
                costoEquipajeExtra, estado, categorias, vuelos, 0); // contadorVisitas = 0 por defecto
    }

        // Constructor vacío
    public DtRutaVuelo(){}

    // === Getters ===
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public String getImagenUrl() { return imagenUrl; }
    public String getVideoUrl() { return videoUrl; }
    public String getAerolinea() { return aerolinea; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getCiudadDestino() { return ciudadDestino; }
    public String getHora() { return hora; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public double getCostoTurista() { return costoTurista; }
    public double getCostoEjecutivo() { return costoEjecutivo; }
    public double getCostoEquipajeExtra() { return costoEquipajeExtra; }
    public EstadoRuta getEstado() { return estado; }
    public List<String> getCategorias() { return categorias; }
    public List<DtVuelo> getVuelos() { return vuelos; }
    public int getContadorVisitas() { return contadorVisitas; }

    @Override
    public String toString() {
        return nombre + " - " + ciudadOrigen + " → " + ciudadDestino + " (" + estado + ")";
    }
}