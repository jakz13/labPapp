package Logica;// ProyectoAerolineas/src/Logica/Logica.ManejadorCiudad.java
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ManejadorCiudad {
    private static ManejadorCiudad instancia;
    private Map<String, Ciudad> ciudades;

    private ManejadorCiudad() {
        ciudades = new HashMap<>();
    }

    public static ManejadorCiudad getInstance() {
        if (instancia == null) {
            instancia = new ManejadorCiudad();
        }
        return instancia;
    }

    public void agregarCiudad(Ciudad c) {
        ciudades.put(c.getNombre(), c);
    }

    public Ciudad obtenerCiudad(String nombre) {
        return ciudades.get(nombre);
    }

    public List<Ciudad> getCiudades() {
        return new ArrayList<>(ciudades.values());
    }
}