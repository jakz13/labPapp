package Logica;

import DataTypes.DtRutaVuelo;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "aerolineas")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Aerolinea extends Usuario {

    private String descripcion;
    private String sitioWeb;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name = "nombre")
    @JoinColumn(name = "aerolinea_id")
    private Map<String, RutaVuelo> rutasVuelo = new HashMap<>();

    public Aerolinea() {
        super();
    }

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
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    // --- Métodos de rutas ---
    public void agregarRutaVuelo(RutaVuelo ruta)
    {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser null");
        }
        rutasVuelo.put(ruta.getNombre(), ruta);
    }

    public List<DtRutaVuelo> getRutasVuelo() {
        return rutasVuelo.values().stream()
                .map(ruta -> {
                    String descripcionCorta = (ruta.getDescripcionCorta() != null) ? ruta.getDescripcionCorta() : "";
                    String estado = (ruta.getEstado() != null) ? ruta.getEstado().toString() : "INGRESADA";
                    String nombreAerolinea = (ruta.getAerolinea() != null) ? ruta.getAerolinea().getNombre() : null;

                    return new DtRutaVuelo(
                            ruta.getNombre(),
                            ruta.getDescripcion(),
                            descripcionCorta,
                            nombreAerolinea,
                            ruta.getCiudadOrigen(),
                            ruta.getCiudadDestino(),
                            ruta.getHora(),
                            ruta.getFechaAlta(),
                            ruta.getCostoTurista(),
                            ruta.getCostoEjecutivo(),
                            ruta.getCostoEquipajeExtra(),
                            estado,
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
                            String descripcionCorta = (ruta.getDescripcionCorta() != null) ? ruta.getDescripcionCorta() : "";
                            String estado = (ruta.getEstado() != null) ? ruta.getEstado().toString() : "INGRESADA";
                            String nombreAerolinea = (ruta.getAerolinea() != null) ? ruta.getAerolinea().getNombre() : null;

                            return new DtRutaVuelo(
                                    ruta.getNombre(),
                                    ruta.getDescripcion(),
                                    descripcionCorta,
                                    nombreAerolinea,
                                    ruta.getCiudadOrigen(),
                                    ruta.getCiudadDestino(),
                                    ruta.getHora(),
                                    ruta.getFechaAlta(),
                                    ruta.getCostoTurista(),
                                    ruta.getCostoEjecutivo(),
                                    ruta.getCostoEquipajeExtra(),
                                    estado,
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aerolinea that = (Aerolinea) o;
        return this.getNickname() != null && this.getNickname().equals(that.getNickname());
    }

    @Override
    public int hashCode() {
        return this.getNickname() != null ? this.getNickname().hashCode() : 0;
    }

}