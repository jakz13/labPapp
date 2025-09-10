package DataTypes;

import java.util.List;

public class DtAerolinea {
    private String nickname;
    private String nombre;
    private String email;
    private String descripcion;
    private String sitioWeb;
    private List<DtRutaVuelo> rutas; // solo nombres de rutas, no objetos

    public DtAerolinea(String nickname, String nombre, String email, String descripcion, String sitioWeb, List<DtRutaVuelo> rutas) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.rutas = rutas;
    }

    // --- Getters ---
    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getDescripcion() { return descripcion; }
    public String getSitioWeb() { return sitioWeb; }
    public List<DtRutaVuelo> getRutas() { return rutas; }

}

