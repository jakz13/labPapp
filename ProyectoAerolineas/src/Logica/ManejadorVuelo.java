// ManejadorVuelo.java
import java.util.HashMap;
import java.util.Map;

public class ManejadorVuelo {
    private Map<String, Vuelo> vuelos;

    public ManejadorVuelo() {
        vuelos = new HashMap<>();
    }

    public void agregarVuelo(Vuelo vuelo) {
        vuelos.put(vuelo.getNombre(), vuelo);
    }

    public Vuelo getVuelo(String nombre) {
        return vuelos.get(nombre);
    }

    public Map<String, Vuelo> getVuelos() {
        return vuelos;
    }
}
