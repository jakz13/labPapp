package logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Manejador singleton para ciudades. Gestiona la carga, persistencia y consulta
 * de ciudades en memoria.
 */
public final class ManejadorCiudad {
    /** Instancia singleton. */
    private static ManejadorCiudad instancia;
    /** Mapa en memoria de ciudades indexado por nombre normalizado. */
    private Map<String, Ciudad> ciudades;

    private ManejadorCiudad() {
        ciudades = new HashMap<>();
    }

    /**
     * Obtiene la instancia singleton del manejador de ciudades.
     * @return instancia única de ManejadorCiudad
     */
    public static ManejadorCiudad getInstance() {
        if (instancia == null) {
            instancia = new ManejadorCiudad();
        }
        return instancia;
    }

    // === Cargar todas las ciudades desde la BD a memoria ===
    /**
     * Carga las ciudades persistidas desde la base de datos al mapa en memoria.
     * @param entManager EntityManager para la consulta
     */
    public void cargarCiudadesDesdeBD(EntityManager entManager) {
        TypedQuery<Ciudad> query = entManager.createQuery("SELECT c FROM Ciudad c", Ciudad.class);
        List<Ciudad> ciudadesPersistidas = query.getResultList();
        for (Ciudad c : ciudadesPersistidas) {
            ciudades.put(c.getNombre(), c);
        }
    }

    // === Agregar ciudad en memoria y en BD ===
    /**
     * Agrega una ciudad nueva validando duplicados y persiste en BD.
     * @param ciudad objeto Ciudad a agregar
     * @param entManager EntityManager usado para persistencia
     */
    public void agregarCiudad(Ciudad ciudad, EntityManager entManager) {
        String clave = ciudad.getNombre().trim().toLowerCase();

        if (ciudades.containsKey(clave)) {
            throw new IllegalArgumentException("La ciudad '" + ciudad.getNombre() + "' ya existe.");
        }
        ciudades.put(clave, ciudad);
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.persist(ciudad);
            entTransaction.commit();
        } catch (Exception e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            throw e;
        }
    }

    // === Obtener ciudad por nombre (normalizado) ===
    /**
     * Obtiene la ciudad por nombre (normalizado a minúsculas y sin espacios externos).
     * @param nombre nombre de la ciudad
     * @return objeto Ciudad o null si no existe
     */
    public Ciudad obtenerCiudad(String nombre) {
        return ciudades.get(nombre.trim().toLowerCase());
    }
    // === Listar todas las ciudades ===
    /**
     * Devuelve la lista de ciudades en memoria.
     */
    public List<Ciudad> getCiudades() {
        return new ArrayList<>(ciudades.values());
    }

    /**
     * Resetea el manejador para pruebas (vacía la cache en memoria).
     */
    public void resetForTests() {
        ciudades.clear();
    }

}
