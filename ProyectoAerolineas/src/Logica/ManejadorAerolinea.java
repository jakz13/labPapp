// Manejador de aerolíneas
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManejadorAerolinea {

    private Map<String, Aerolinea> aerolineas;
    public static ManejadorAerolinea instancia = null;

    private ManejadorAerolinea() {
        aerolineas = new HashMap<String, Aerolinea>();
    }

    public static ManejadorAerolinea getInstance() {
        if (instancia == null) {
            instancia = new ManejadorAerolinea();
        }
        return instancia;
    }


    public Aerolinea obtenerAerolinea(String nickname) {
        return ((Aerolinea) aerolineas.get(nickname));
    }



    public void agregarAerolinea(Aerolinea aerolinea) {
        aerolineas.put(aerolinea.getNickname(), aerolinea);
    }

    public Aerolinea buscarAerolinea(String nombre) {
        for (Aerolinea aerolinea : aerolineas.values()) {
            if (aerolinea.getNombre().equalsIgnoreCase(nombre)) {
                return aerolinea;
            }
        }
        return null;
    }

    public void mostrarAerolineas() {
        for (Aerolinea aerolinea : aerolineas.values()) {
            System.out.println(aerolinea);
        }
    }

    public int getCantidadAerolineas() {
        return aerolineas.size();
    }

    public void agregarRutaVueloAAerolinea(String nombreAerolinea, RutaVuelo ruta) {
        Aerolinea aerolinea = buscarAerolinea(nombreAerolinea);
        if (aerolinea != null) {
            aerolinea.agregarRutaVuelo(ruta);
        } else {
            System.out.println("Aerolínea no encontrada.");
        }
    }

    public RutaVuelo obtenerRutaVueloDeAerolinea(String nombreAerolinea, String nombreRuta) {
        Aerolinea aerolinea = buscarAerolinea(nombreAerolinea);
        if (aerolinea != null) {
            return aerolinea.getRutaVuelo(nombreRuta);
        }
        return null;
    }

    public List<Aerolinea> getAerolineas() {
        return new ArrayList<>(aerolineas.values());
    }
}