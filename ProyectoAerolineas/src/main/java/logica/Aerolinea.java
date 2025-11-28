package logica;

import DataTypes.DtRutaVuelo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import static logica.EstadoRuta.*;


/**
 * Representa una aerolínea registrada en el sistema.
 * Contiene sus rutas y datos básicos como descripción y sitio web.
 */
@Entity
@Table(name = "aerolineas")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Aerolinea extends Usuario {

    private String descripcion;
    private String sitioWeb;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "nombre")
    @JoinColumn(name = "aerolinea_id")
    private Map<String, RutaVuelo> rutasVuelo = new ConcurrentHashMap<>();

    // Colecciones en memoria con referencias a objetos (no persistidas)
    @jakarta.persistence.Transient
    private Set<Usuario> siguiendo = new HashSet<>();
    @jakarta.persistence.Transient
    private Set<Usuario> seguidores = new HashSet<>();

    public Aerolinea() {
        super();
    }

    /**
     * Crea una aerolínea con datos básicos y contraseña.
     */
    // Constructor actualizado con contraseña
    public Aerolinea(String nickname, String nombre, String email, String password, String descripcion, String sitioWeb, String imagenUrl) {
        super(nickname, nombre, email, password, LocalDate.now(), imagenUrl);
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre de la ciudad no puede ser null");
        }
        if (nickname == null) {
            throw new IllegalArgumentException("El nickname de la ciudad no puede ser null");
        }
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
    }

    // --- Getters y Setters ---
    /** Devuelve la descripción de la aerolínea. */
    public String getDescripcion() { return descripcion; }
    /** Actualiza la descripción de la aerolínea. */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    /** Devuelve la URL del sitio web de la aerolínea. */
    public String getSitioWeb() { return sitioWeb; }
    /** Actualiza la URL del sitio web de la aerolínea. */
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    // --- Métodos de rutas ---
    /**
     * Agrega una ruta de vuelo a la aerolínea.
     */
    public void agregarRutaVuelo(RutaVuelo ruta)
    {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser null");
        }
        rutasVuelo.put(ruta.getNombre(), ruta);
    }

    /**
     * Devuelve una lista con los datos de las rutas de la aerolínea.
     */
    public List<DtRutaVuelo> getRutasVuelo() {
        return rutasVuelo.values().stream()
                .map(ruta -> {
                    String descripcionCorta = (ruta.getDescripcionCorta() != null) ? ruta.getDescripcionCorta() : "";
                    String nombreAerolinea = (ruta.getAerolinea() != null) ? ruta.getAerolinea().getNombre() : null;

                    return new DtRutaVuelo(
                            ruta.getNombre(),
                            ruta.getDescripcion(),
                            descripcionCorta,
                            ruta.getImagenUrl(),
                            ruta.getVideoUrl(),
                            nombreAerolinea,
                            ruta.getCiudadOrigen(),
                            ruta.getCiudadDestino(),
                            ruta.getHora(),
                            ruta.getFechaAlta(),
                            ruta.getCostoTurista(),
                            ruta.getCostoEjecutivo(),
                            ruta.getCostoEquipajeExtra(),
                            ruta.getEstado(),
                            (List) (ruta.getCategorias() != null ? ruta.getCategorias() : new ArrayList()),
                            (List) (ruta.getDtVuelos() != null ? ruta.getDtVuelos() : new ArrayList())
                    );

                })
                .toList();
    }

    /**
     * Devuelve una copia de los datos de las rutas (versión alternativa).
     */
    public List<DtRutaVuelo> getDtRutasVuelo() {
        return new ArrayList<>(
                rutasVuelo.values().stream()
                        .map(ruta -> {
                            String descripcionCorta = (ruta.getDescripcionCorta() != null) ? ruta.getDescripcionCorta() : "";
                            String nombreAerolinea = (ruta.getAerolinea() != null) ? ruta.getAerolinea().getNombre() : null;

                            return new DtRutaVuelo(
                                    ruta.getNombre(),
                                    ruta.getDescripcion(),
                                    descripcionCorta,
                                    ruta.getImagenUrl(),
                                    ruta.getVideoUrl(),
                                    nombreAerolinea,
                                    ruta.getCiudadOrigen(),
                                    ruta.getCiudadDestino(),
                                    ruta.getHora(),
                                    ruta.getFechaAlta(),
                                    ruta.getCostoTurista(),
                                    ruta.getCostoEjecutivo(),
                                    ruta.getCostoEquipajeExtra(),
                                    ruta.getEstado(),
                                    ruta.getCategorias() != null ? ruta.getCategorias() : new ArrayList<>(),
                                    ruta.getDtVuelos() != null ? ruta.getDtVuelos() : new ArrayList<>()
                            );
                        })
                        .toList()
        );
    }

    @Override
    public String toString() {
        return this.getNombre();
    }

    /** Devuelve el mapa interno de rutas. */
    public Map<String, RutaVuelo> getRutasVueloMap() {
        return rutasVuelo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Aerolinea that = (Aerolinea) obj;
        return this.getNickname() != null && this.getNickname().equals(that.getNickname());
    }

    @Override
    public int hashCode() {
        return this.getNickname() != null ? this.getNickname().hashCode() : 0;
    }

}