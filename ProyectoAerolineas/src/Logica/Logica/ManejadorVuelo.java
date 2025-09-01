
package Logica;
import java.util.HashMap;
import java.util.Map;

public class ManejadorVuelo {
    private Map<String, Vuelo> vuelos;
    public static ManejadorVuelo instancia = null;

    public static ManejadorVuelo getInstance() {
        if (instancia == null) {
            instancia = new ManejadorVuelo();
        }
        return instancia;
    }

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

    public boolean tieneReservaDeCliente(String nicknameCliente, Vuelo vuelo) {
        return vuelo.getReservas().containsKey(nicknameCliente);
    }
}
