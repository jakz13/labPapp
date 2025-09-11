package DataTypes;

import java.time.LocalDate;
import java.util.List;

public class DtRutaVuelo {
    private String nombre;
    private String descripcion;
    private String aerolinea; // solo nickname o nombre, no objeto
    private String ciudadOrigen;
    private String ciudadDestino;
    private String hora;
    private LocalDate fechaAlta;
    private double costoTurista;
    private double costoEjecutivo;
    private double costoEquipajeExtra;
    private List<String> categorias;
    private List<DtVuelo> vuelos; // nombres de vuelos, no objetos

    public DtRutaVuelo(String nombre, String descripcion, String aerolinea,
                       String ciudadOrigen, String ciudadDestino, String hora,
                       LocalDate fechaAlta, double costoTurista, double costoEjecutivo,
                       double costoEquipajeExtra, List<String> categorias, List<DtVuelo> vuelos) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.aerolinea = aerolinea;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEjecutivo = costoEjecutivo;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.categorias = categorias;
        this.vuelos = vuelos;
    }



    // === Getters ===
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getAerolinea() { return aerolinea; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getCiudadDestino() { return ciudadDestino; }
    public String getHora() { return hora; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public double getCostoTurista() { return costoTurista; }
    public double getCostoEjecutivo() { return costoEjecutivo; }
    public double getCostoEquipajeExtra() { return costoEquipajeExtra; }
    public List<String> getCategorias() { return categorias; }
    public List<DtVuelo> getVuelos() { return vuelos; }
}

