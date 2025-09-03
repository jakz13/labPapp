package Logica;

import jakarta.persistence.*;
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

    public Aerolinea(String nickname, String nombre, String email, String descripcion, String sitioWeb) {
        super(nickname, nombre, email);
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getSitioWeb() { return sitioWeb; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }

    public void agregarRutaVuelo(RutaVuelo ruta) {
        rutasVuelo.put(ruta.getNombre(), ruta);
    }

    public List<RutaVuelo> getRutasVuelo() {
        return new ArrayList<>(rutasVuelo.values());
    }

    public Map<String, RutaVuelo> getRutasVueloMap() {
        return rutasVuelo;
    }
}

