package Logica;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "rutas_vuelo")
public class RutaVuelo {

    @Id
    @Column(nullable = false, length = 100)
    private String nombre; // clave primaria

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    private String ciudadOrigen;
    private String ciudadDestino;
    private String hora;

    private LocalDate fechaAlta;

    private double costoTurista;
    private double costoEjecutivo;
    private double costoEquipajeExtra;

    @ElementCollection
    @CollectionTable(name = "ruta_categorias", joinColumns = @JoinColumn(name = "ruta_nombre"))
    @Column(name = "categoria")
    private List<String> categorias = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "nombre")
    @JoinColumn(name = "ruta_nombre")
    private Map<String, Vuelo> vuelos = new HashMap<>();

    public RutaVuelo() {}

    public RutaVuelo(String nombre, String descripcion, Aerolinea aerolinea, String ciudadOrigen,
                     String ciudadDestino, String hora, LocalDate fechaAlta,
                     double costoTurista, double costoEjecutivo, double costoEquipajeExtra, String[] categorias) {
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
        this.categorias = Arrays.asList(categorias);
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
    public List<String> getCategorias() { return categorias; }

    public void agregarVuelo(Vuelo vuelo) {
        vuelos.put(vuelo.getNombre(), vuelo);
    }

    public Vuelo getVuelo(String nombre) {
        return vuelos.get(nombre);
    }

    public Map<String, Vuelo> getVuelos() {
        return vuelos;
    }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setAerolinea(Aerolinea aerolinea) { this.aerolinea = aerolinea; }
    public void setCiudadOrigen(String ciudadOrigen) { this.ciudadOrigen = ciudadOrigen; }
    public void setCiudadDestino(String ciudadDestino) { this.ciudadDestino = ciudadDestino; }
    public void setHora(String hora) { this.hora = hora; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    public void setCostoTurista(double costoTurista) { this.costoTurista = costoTurista; }
    public void setCostoEjecutivo(double costoEjecutivo) { this.costoEjecutivo = costoEjecutivo; }
    public void setCostoEquipajeExtra(double costoEquipajeExtra) { this.costoEquipajeExtra = costoEquipajeExtra; }
    public void setCategorias(List<String> categorias) { this.categorias = categorias; }
}

