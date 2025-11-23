package DataTypes;

import java.util.List;

public class DtAerolinea {
    private String nickname;
    private String nombre;
    private String email;
    private String descripcion;
    private String sitioWeb;
    private String imagenUrl; // ✅ Nuevo campo
    private List<DtRutaVuelo> rutas;

    public DtAerolinea(String nickname, String nombre, String email, String descripcion,
                       String sitioWeb, List<DtRutaVuelo> rutas) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.rutas = rutas;
        this.imagenUrl = null;
    }

    public DtAerolinea(String nickname, String nombre, String email, String descripcion,
                       String sitioWeb, String imagenUrl, List<DtRutaVuelo> rutas) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.imagenUrl = imagenUrl;
        this.rutas = rutas;
    }

    public DtAerolinea(){}

    // --- Getters ---
    public String getNickname() { return nickname; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getDescripcion() { return descripcion; }
    public String getSitioWeb() { return sitioWeb; }
    public String getImagenUrl() { return imagenUrl; }
    public List<DtRutaVuelo> getRutas() { return rutas; }

    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setSitioWeb(String sitioWeb) { this.sitioWeb = sitioWeb; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setRutas(List<DtRutaVuelo> rutas) { this.rutas = rutas; }

    @Override
    public String toString() {
        return nombre + " (" + nickname + ")";
    }
}