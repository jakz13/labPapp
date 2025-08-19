import java.util.HashMap;
import java.util.Map;

public class Aerolinea extends Usuario {
    private String descripcion;
    private String sitioWeb;
    private Map<String, RutaVuelo> rutasVuelo;

    public Aerolinea(String nickname, String nombre, String email, String descripcion) {
        super(nickname, nombre, email);
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.rutasVuelo = new HashMap<>();
    }

    public String getDescripcion() { return descripcion; }
    public String getSitioWeb() { return sitioWeb; }

    public void agregarRutaVuelo(RutaVuelo ruta) {
        rutasVuelo.put(ruta.getNombre(), ruta);
    }

    public RutaVuelo getRutaVuelo(String nombre) {
        return rutasVuelo.get(nombre);
    }

    public Map<String, RutaVuelo> getRutasVuelo() {
        return rutasVuelo;
    }
}
