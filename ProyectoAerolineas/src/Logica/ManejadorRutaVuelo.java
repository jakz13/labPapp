import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManejadorRutaVuelo {
    private Map<String, RutaVuelo> rutasVuelo;
    public static ManejadorRutaVuelo instancia = null;

    public static ManejadorRutaVuelo getInstance() {
        if (instancia == null) {
            instancia = new ManejadorRutaVuelo();
        }
        return instancia;
    }

    public ManejadorRutaVuelo() {
        rutasVuelo = new HashMap<>();
    }



    public void agregarRutaVuelo(RutaVuelo ruta) {
        rutasVuelo.put(ruta.getNombre(), ruta);
    }

    public RutaVuelo getRuta(String nombre) {
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

    public List<RutaVuelo> getRutasPorAerolinea(String nombreAerolinea) {
        List<RutaVuelo> rutas = new ArrayList<>();
        for (RutaVuelo ruta : rutasVuelo.values()) {
            if (ruta.getAerolinea().equals(nombreAerolinea)) {
                rutas.add(ruta);
            }
        }
        return rutas;
    }
}
