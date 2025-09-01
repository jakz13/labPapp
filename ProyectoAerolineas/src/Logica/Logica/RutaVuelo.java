package Logica;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RutaVuelo {
    private String nombre;
    private String descripcion;
    private Aerolinea aerolinea;
    private String ciudadOrigen;
    private String ciudadDestino;
    private String hora;
    private LocalDate fechaAlta;
    private double costoTurista;
    private double costoEjecutivo;
    private double costoEquipajeExtra;
    private String[] categorias;
    private Map<String, Vuelo> vuelos;

    public RutaVuelo(String nombre, String descripcion, Aerolinea aerolinea, String ciudadOrigen, String ciudadDestino, String hora, LocalDate fechaAlta, double costoTurista, double costoEjecutivo, double costoEquipajeExtra, String[] categorias) {
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
        this.vuelos = new HashMap<>();
    }


    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Aerolinea getAerolinea() { return aerolinea; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getCiudadDestino() { return ciudadDestino; }
    public String getHora() { return hora; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public double getCostoTurista() { return costoTurista; }
    public double getCostoEjecutivo() { return costoEjecutivo; }
    public double getCostoEquipajeExtra() { return costoEquipajeExtra; }
    public String[] getCategorias() { return categorias; }

    public void agregarVuelo(Vuelo vuelo) {
        vuelos.put(vuelo.getNombre(), vuelo);
    }

    public Vuelo getVuelo(String nombre) {
        return vuelos.get(nombre);
    }

    public Map<String, Vuelo> getVuelos() {
        return vuelos;
    }
}
