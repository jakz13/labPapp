import java.util.HashMap;
import java.util.Map;

public class ManejadorRutaVuelo {
    private Map<String, RutaVuelo> rutasVuelo;

    public ManejadorRutaVuelo() {
        rutasVuelo = new HashMap<>();
    }

    public void agregarRutaVuelo(RutaVuelo ruta) {
        rutasVuelo.put(ruta.getNombre(), ruta);
    }

    public RutaVuelo getRutaVuelo(String nombre) {
        return rutasVuelo.get(nombre);
    }

    public Map<String, RutaVuelo> getRutasVuelo() {
        return rutasVuelo;
    }

    public void agregarVueloARuta(String nombreRuta, Vuelo vuelo) {
        RutaVuelo ruta = rutasVuelo.get(nombreRuta);
        if (ruta != null) {
            ruta.agregarVuelo(vuelo);
        } else {
            System.out.println("Ruta de vuelo no encontrada.");
        }
    }

    public Vuelo getVueloDeRuta(String nombreRuta, String nombreVuelo) {
        RutaVuelo ruta = rutasVuelo.get(nombreRuta);
        if (ruta != null) {
            return ruta.getVuelo(nombreVuelo);
        }
        return null;
    }

    public void agregarRuta(RutaVuelo r) {

    }
}
