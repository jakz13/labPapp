package logica;

import DataTypes.DtVuelo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static logica.EstadoRuta.INGRESADA;

/**
 * Representa una ruta de vuelo entre dos ciudades, con su información, costos,
 * estado y la lista de vuelos programados para esa ruta.
 */
@Entity
@Table(name = "rutas_vuelo")
public class RutaVuelo {

    /** Identificador interno de la ruta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRutaVuelo;

    /** Nombre único de la ruta. */
    private String nombre;
    /** Descripción completa de la ruta. */
    private String descripcion;
    /** Descripción corta utilizada en listados. */
    private String descripcionCorta;

    /** URL opcional de la imagen representativa de la ruta. */
    @Column(name = "imagen_url")
    private String imagenUrl;
    /** URL opcional de un video representativo de la ruta. */
    @Column(name = "video_url")
    private String videoUrl;

    /** Contador de vistas de la ruta. */
    @Column(name = "contador_visitas", nullable = false)
    private int contadorVisitas = 0;

    /** Aerolínea propietaria de la ruta. */
    @ManyToOne
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    /** Ciudad de origen de la ruta. */
    private String ciudadOrigen;
    /** Ciudad de destino de la ruta. */
    private String ciudadDestino;
    /** Hora planificada de salida/formato string. */
    private String hora;
    /** Fecha de alta de la ruta. */
    private LocalDate fechaAlta;

    /** Costo por asiento en clase turista. */
    private double costoTurista;
    /** Costo por asiento en clase ejecutivo. */
    private double costoEjecutivo;
    /** Costo por unidad de equipaje extra. */
    private double costoEquipajeExtra;

    /** Estado actual de la ruta (INGRESADA/CONFIRMADA/RECHAZADA). */
    @Enumerated(EnumType.STRING)
    private EstadoRuta estado;

    /** Categorías asociadas a la ruta. */
    @ElementCollection
    @CollectionTable(name = "ruta_categorias", joinColumns = @JoinColumn(name = "ruta_id"))
    @Column(name = "categoria")
    private List<String> categorias = new ArrayList<>();

    /** Lista de vuelos asociados a esta ruta. */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ruta_vuelo_id")
    private List<Vuelo> vuelos = new ArrayList<>();

    public RutaVuelo() {
        this.estado = INGRESADA;
        this.categorias = new ArrayList<>();
        this.vuelos = new ArrayList<>();
        this.imagenUrl = null;
    }

    /**
     * Crea una ruta con sus datos esenciales.
     */
    public RutaVuelo(String nombre, String descripcion, String descripcionCorta, Aerolinea aerolinea,
                     String ciudadOrigen, String ciudadDestino, String hora, LocalDate fechaAlta,
                     double costoTurista, double costoEjecutivo, double costoEquipajeExtra,
                     List<String> categorias, String imagenUrl, String videoUrl) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descripcionCorta = descripcionCorta;
        this.aerolinea = aerolinea;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.hora = hora;
        this.fechaAlta = fechaAlta;
        this.costoTurista = costoTurista;
        this.costoEjecutivo = costoEjecutivo;
        this.costoEquipajeExtra = costoEquipajeExtra;
        this.categorias = (categorias != null) ? categorias : new ArrayList<>();
        this.estado = INGRESADA;
        this.vuelos = new ArrayList<>();
        this.imagenUrl = imagenUrl;
        this.videoUrl = videoUrl;
        this.contadorVisitas = 0; // Inicializar contador
    }

    // ===== Getters y Setters =====
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getDescripcionCorta() {
        return (descripcionCorta != null) ? descripcionCorta : "";
    }
    public String getImagenUrl() { return imagenUrl; }
    public String getVideoUrl() { return videoUrl; }
    public Aerolinea getAerolinea() { return aerolinea; }
    public String getCiudadOrigen() { return ciudadOrigen; }
    public String getCiudadDestino() { return ciudadDestino; }
    public String getHora() { return hora; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public double getCostoTurista() { return costoTurista; }
    public double getCostoEjecutivo() { return costoEjecutivo; }
    public double getCostoEquipajeExtra() { return costoEquipajeExtra; }
    public EstadoRuta getEstado() {
        return (estado != null) ? estado : INGRESADA;
    }
    public List<String> getCategorias() {
        return (categorias != null) ? categorias : new ArrayList<>();
    }
    public List<Vuelo> getVuelos() {
        return (vuelos != null) ? vuelos : new ArrayList<>();
    }
    public int getContadorVisitas() { return contadorVisitas; }

    public void setContadorVisitas(int contadorVisitas) { this.contadorVisitas = contadorVisitas; }
    public void setEstado(EstadoRuta estado) { this.estado = estado; }
    public void setDescripcionCorta(String descripcionCorta) { this.descripcionCorta = descripcionCorta; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setAerolinea(Aerolinea aerolinea) { this.aerolinea = aerolinea; }
    public void setCiudadOrigen(String ciudadOrigen) { this.ciudadOrigen = ciudadOrigen; }
    public void setCiudadDestino(String ciudadDestino) { this.ciudadDestino = ciudadDestino; }
    public void setHora(String hora) { this.hora = hora; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    public void setCostoTurista(double costoTurista) { this.costoTurista = costoTurista; }
    public void setCostoEjecutivo(double costoEjecutivo) { this.costoEjecutivo = costoEjecutivo; }
    public void setCostoEquipajeExtra(double costoEquipajeExtra) { this.costoEquipajeExtra = costoEquipajeExtra; }

    /** Agrega un vuelo a la ruta. */
    public void agregarVuelo(Vuelo vuelo) {
        if (vuelos == null) {
            vuelos = new ArrayList<>();
        }
        vuelos.add(vuelo);
    }

    /** Agrega una vista. */
    public void incrementarVisitas() {
        this.contadorVisitas++;
    }

    /** Obtiene un vuelo de la ruta por su nombre, o null si no existe. */
    public Vuelo getVuelo(String nombreVuelo) {
        if (vuelos == null) return null;

        for (Vuelo v : vuelos) {
            if (v.getNombre().equals(nombreVuelo)) return v;
        }
        return null;
    }

    @Override
    public String toString() { return nombre; }

    /** Devuelve los DTOs de los vuelos asociados a esta ruta. */
    public List<DtVuelo> getDtVuelos() {
        List<DtVuelo> dtVuelos = new ArrayList<>();
        if (vuelos != null) {
            for (Vuelo v : vuelos) {
                dtVuelos.add(v.getDtVueloSinRuta());
            }
        }
        return dtVuelos;
    }

    /** Devuelve un DTO completo de esta ruta (incluye datos resumidos de vuelos). */
    public DataTypes.DtRutaVuelo getDtRutaVuelo() {
        String nombreAerolinea = (aerolinea != null) ? aerolinea.getNombre() : null;
        List<DtVuelo> dtVuelos = getDtVuelos();
        EstadoRuta estado = (this.estado != null) ? this.estado : INGRESADA;
        String descCorta = (descripcionCorta != null) ? descripcionCorta : "";
        String imgUrl = (imagenUrl != null) ? imagenUrl : null;
        String vidUrl = (videoUrl != null) ? videoUrl : null;

        // LLAMADA CORRECTA al constructor
        return new DataTypes.DtRutaVuelo(
                nombre,                    // String nombre
                descripcion,               // String descripcion
                descCorta,                 // String descripcionCorta
                imgUrl,                    // String imagenUrl
                vidUrl,                    // String videoUrl
                nombreAerolinea,           // String aerolinea
                ciudadOrigen,              // String ciudadOrigen
                ciudadDestino,             // String ciudadDestino
                hora,                      // String hora
                fechaAlta,                 // LocalDate fechaAlta
                costoTurista,              // double costoTurista
                costoEjecutivo,            // double costoEjecutivo
                costoEquipajeExtra,        // double costoEquipajeExtra
                estado,                 // String estado
                categorias != null ? categorias : new ArrayList<>(), // List<String> categorias
                dtVuelos,                  // List<DtVuelo> vuelos
                contadorVisitas            // int contadorVisitas
        );
    }
}