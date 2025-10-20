package Logica;

import DataTypes.DtVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.*;

public class ManejadorVuelo {

    private Map<String, Vuelo> vuelos;
    public static ManejadorVuelo instancia = null;

    private ManejadorVuelo() {
        vuelos = new HashMap<>();
    }

    public static ManejadorVuelo getInstance() {
        if (instancia == null) {
            instancia = new ManejadorVuelo();
        }
        return instancia;
    }

    // =================== CRUD BD ===================
    public void cargarVuelosDesdeBD(EntityManager em) {
        TypedQuery<Vuelo> query = em.createQuery("SELECT v FROM Vuelo v LEFT JOIN FETCH v.reservasList", Vuelo.class);
        List<Vuelo> vuelosPersistidos = query.getResultList();
        for (Vuelo v : vuelosPersistidos) {
            // Sincronizar el Map con las reservas cargadas
            v.getReservas(); // Esto sincroniza el Map
            vuelos.put(v.getNombre(), v);
        }
    }

    // Método para uso interno (sin transacción)
    public void agregarVuelo(Vuelo vuelo, EntityManager em) {
        em.persist(vuelo);
        vuelos.put(vuelo.getNombre(), vuelo);
    }

    // Método para uso externo (con transacción)
    public void agregarVueloConTransaccion(Vuelo vuelo, EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vuelo);
            vuelos.put(vuelo.getNombre(), vuelo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al agregar el vuelo: " + e.getMessage(), e);
        }
    }

    // Método para uso interno (sin transacción) - usado por registrarReservaVuelo
    public void actualizarVuelo(Vuelo vuelo, EntityManager em) {
        em.merge(vuelo);
    }

    // Método para uso externo (con transacción)
    public void actualizarVueloConTransaccion(Vuelo vuelo, EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(vuelo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al actualizar el vuelo: " + e.getMessage(), e);
        }
    }

    public boolean tieneReservaDeCliente(String nicknameCliente, Vuelo vuelo) {
        return vuelo.getReservas().containsKey(nicknameCliente);
    }

    public Vuelo getVuelo(String nombre) {
        return vuelos.get(nombre);
    }

    public List<Vuelo> getVuelos() {
        return new ArrayList<>(vuelos.values());
    }

    public List<DtVuelo> getDtVuelos() {
        List<DtVuelo> dtVuelos = new ArrayList<>();
        for (Vuelo vuelo : vuelos.values()) {
            dtVuelos.add(vuelo.getDtVuelo());
        }
        return dtVuelos;
    }

    // Método para obtener vuelos por aerolínea
    public List<Vuelo> getVuelosPorAerolinea(String nombreAerolinea) {
        List<Vuelo> vuelosAerolinea = new ArrayList<>();
        for (Vuelo vuelo : vuelos.values()) {
            if (vuelo.getNombreAerolinea().equals(nombreAerolinea)) {
                vuelosAerolinea.add(vuelo);
            }
        }
        return vuelosAerolinea;
    }

    // Método para obtener vuelos por ruta
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

    // Método para verificar si existe un vuelo
    public boolean existeVuelo(String nombreVuelo) {
        return vuelos.containsKey(nombreVuelo);
    }

    // Método para eliminar vuelo (con transacción)
    public void eliminarVuelo(String nombreVuelo, EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Vuelo vuelo = vuelos.get(nombreVuelo);
            if (vuelo != null) {
                em.remove(vuelo);
                vuelos.remove(nombreVuelo);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al eliminar el vuelo: " + e.getMessage(), e);
        }
    }

    // Método para limpiar cache en memoria (útil para testing)
    public void limpiarCache() {
        vuelos.clear();
    }
}