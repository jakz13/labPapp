package Logica;

import DataTypes.DtRutaVuelo;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "aerolineas")
@PrimaryKeyJoinColumn(name = "usuario_id") // porque hereda de Usuario
public class Aerolinea extends Usuario {

    private String descripcion;
    private String sitioWeb;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "nombre") // usamos el nombre de la ruta como clave
    @JoinColumn(name = "aerolinea_id") // FK en la tabla de rutas
    private Map<String, RutaVuelo> rutasVuelo = new HashMap<>();

    public Aerolinea() {
        super();
    }

    public Aerolinea(String nickname, String nombre, String email, String descripcion, String sitioWeb) {
        super(nickname, nombre, email);
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
    }

    // --- Getters y Setters ---
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    // --- Métodos de rutas ---
    public void agregarRutaVuelo(RutaVuelo ruta) {
        rutasVuelo.put(ruta.getNombre(), ruta);
    }

    public List<DtRutaVuelo> getRutasVuelo() {
        return rutasVuelo.values().stream()
                .map(ruta -> {
                    // Manejar valores nulos
                    String descripcionCorta = (ruta.getDescripcionCorta() != null) ? ruta.getDescripcionCorta() : "";
                    String estado = (ruta.getEstado() != null) ? ruta.getEstado().toString() : "INGRESADA";
                    String nombreAerolinea = (ruta.getAerolinea() != null) ? ruta.getAerolinea().getNombre() : null;

                    return new DtRutaVuelo(
                            ruta.getNombre(),
                            ruta.getDescripcion(),
                            descripcionCorta, // Campo manejado contra nulos
                            nombreAerolinea,  // Campo manejado contra nulos
                            ruta.getCiudadOrigen(),
                            ruta.getCiudadDestino(),
                            ruta.getHora(),
                            ruta.getFechaAlta(),
                            ruta.getCostoTurista(),
                            ruta.getCostoEjecutivo(),
                            ruta.getCostoEquipajeExtra(),
                            estado, // Campo manejado contra nulos
                            ruta.getCategorias() != null ? ruta.getCategorias() : new ArrayList<>(),
                            ruta.getDtVuelos() != null ? ruta.getDtVuelos() : new ArrayList<>()
                    );
                })
                .toList();
    }

    public List<DtRutaVuelo> getDtRutasVuelo() {
        return new ArrayList<>(
                rutasVuelo.values().stream()
                        .map(ruta -> {
                            // Manejar valores nulos
                            String descripcionCorta = (ruta.getDescripcionCorta() != null) ? ruta.getDescripcionCorta() : "";
                            String estado = (ruta.getEstado() != null) ? ruta.getEstado().toString() : "INGRESADA";
                            String nombreAerolinea = (ruta.getAerolinea() != null) ? ruta.getAerolinea().getNombre() : null;

                            return new DtRutaVuelo(
                                    ruta.getNombre(),
                                    ruta.getDescripcion(),
                                    descripcionCorta, // Campo manejado contra nulos
                                    nombreAerolinea,  // Campo manejado contra nulos
                                    ruta.getCiudadOrigen(),
                                    ruta.getCiudadDestino(),
                                    ruta.getHora(),
                                    ruta.getFechaAlta(),
                                    ruta.getCostoTurista(),
                                    ruta.getCostoEjecutivo(),
                                    ruta.getCostoEquipajeExtra(),
                                    estado, // Campo manejado contra nulos
                                    ruta.getCategorias() != null ? ruta.getCategorias() : new ArrayList<>(),
                                    ruta.getDtVuelos() != null ? ruta.getDtVuelos() : new ArrayList<>()
                            );
                        })
                        .toList()
        );
    }

    public Map<String, RutaVuelo> getRutasVueloMap() {
        return rutasVuelo;
    }
}