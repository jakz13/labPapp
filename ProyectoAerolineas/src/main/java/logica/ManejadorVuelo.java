package logica;

import DataTypes.DtVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona en memoria y en BD los vuelos del sistema.
 * Permite cargar, agregar, actualizar y eliminar vuelos, así como obtener DTs.
 */
public final class ManejadorVuelo {

    /** Mapa en memoria de vuelos indexado por nombre. */
    private Map<String, Vuelo> vuelos;
    /** Instancia singleton del manejador. */
    private static ManejadorVuelo instancia = null;

    private ManejadorVuelo() {
        vuelos = new HashMap<>();
    }

    /** Obtiene la instancia singleton del manejador de vuelos. */
    public static ManejadorVuelo getInstance() {
        if (instancia == null) {
            instancia = new ManejadorVuelo();
        }
        return instancia;
    }

    // =================== CRUD BD ===================
    /** Carga los vuelos desde la base de datos */
    public void cargarVuelosDesdeBD(EntityManager entManager) {
        TypedQuery<Vuelo> query = entManager.createQuery("SELECT v FROM Vuelo v LEFT JOIN FETCH v.reservasList", Vuelo.class);
        List<Vuelo> vuelosPersistidos = query.getResultList();
        for (Vuelo v : vuelosPersistidos) {
            // Sincronizar el Map con las reservas cargadas
            v.getReservas(); // Esto sincroniza el Map
            vuelos.put(v.getNombre(), v);
        }
    }

    /** Método para uso interno (sin transacción) */
    public void agregarVuelo(Vuelo vuelo, EntityManager entManager) {
        entManager.persist(vuelo);
        vuelos.put(vuelo.getNombre(), vuelo);
    }

    /** Método para uso externo (con transacción) */
    public void agregarVueloConTransaccion(Vuelo vuelo, EntityManager entManager) {
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.persist(vuelo);
            vuelos.put(vuelo.getNombre(), vuelo);
            entTransaction.commit();
        } catch (Exception e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            throw new IllegalStateException("Error al agregar el vuelo: " + e.getMessage(), e);
        }
    }

    /** Método para uso interno (sin transacción) - usado por registrarReservaVuelo */
    public void actualizarVuelo(Vuelo vuelo, EntityManager entManager) {
        entManager.merge(vuelo);
    }

    /** Método para uso externo (con transacción) */
    public void actualizarVueloConTransaccion(Vuelo vuelo, EntityManager entManager) {
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.merge(vuelo);
            entTransaction.commit();
        } catch (Exception e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            throw new IllegalStateException("Error al actualizar el vuelo: " + e.getMessage(), e);
        }
    }

    /** Verifica si un vuelo tiene reserva de un cliente */
    public boolean tieneReservaDeCliente(String nicknameCliente, Vuelo vuelo) {
        return vuelo.getReservas().containsKey(nicknameCliente);
    }

    /** Obtiene un vuelo por su nombre */
    public Vuelo getVuelo(String nombre) {
        return vuelos.get(nombre);
    }

    /** Obtiene la lista de vuelos */
    public List<Vuelo> getVuelos() {
        return new ArrayList<>(vuelos.values());
    }

    /** Obtiene la lista de DTs de vuelos */
    public List<DtVuelo> getDtVuelos() {
        List<DtVuelo> dtVuelos = new ArrayList<>();
        for (Vuelo vuelo : vuelos.values()) {
            dtVuelos.add(vuelo.getDtVuelo());
        }
        return dtVuelos;
    }

    /** Método para obtener vuelos por aerolínea */
    public List<Vuelo> getVuelosPorAerolinea(String nombreAerolinea) {
        List<Vuelo> vuelosAerolinea = new ArrayList<>();
        for (Vuelo vuelo : vuelos.values()) {
            if (vuelo.getNombreAerolinea().equals(nombreAerolinea)) {
                vuelosAerolinea.add(vuelo);
            }
        }
        return vuelosAerolinea;
    }

    /** Método para obtener vuelos por ruta */
    public List<Vuelo> getVuelosPorRuta(String nombreRuta) {
        List<Vuelo> vuelosRuta = new ArrayList<>();
        for (Vuelo vuelo : vuelos.values()) {
            if (vuelo.getRutaVuelo() != null &&
                    vuelo.getRutaVuelo().getNombre().equals(nombreRuta)) {
                vuelosRuta.add(vuelo);
            }
        }
        return vuelosRuta;
    }

    /** Método para verificar si existe un vuelo */
    public boolean existeVuelo(String nombreVuelo) {
        return vuelos.containsKey(nombreVuelo);
    }

    /** Método para eliminar vuelo (con transacción) */
    public void eliminarVuelo(String nombreVuelo, EntityManager entManager) {
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            Vuelo vuelo = vuelos.get(nombreVuelo);
            if (vuelo != null) {
                entManager.remove(vuelo);
                vuelos.remove(nombreVuelo);
            }
            entTransaction.commit();
        } catch (Exception e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            throw new IllegalStateException("Error al eliminar el vuelo: " + e.getMessage(), e);
        }
    }

    /** Método para limpiar cache en memoria (útil para testing) */
    public void limpiarCache() {
        vuelos.clear();
    }
}