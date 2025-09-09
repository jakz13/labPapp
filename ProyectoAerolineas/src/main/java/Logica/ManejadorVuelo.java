package Logica;

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
        TypedQuery<Vuelo> query = em.createQuery("SELECT v FROM Vuelo v", Vuelo.class);
        List<Vuelo> vuelosPersistidos = query.getResultList();
        for (Vuelo v : vuelosPersistidos) {
            vuelos.put(v.getNombre(), v);
        }
    }

    public void agregarVuelo(Vuelo vuelo, EntityManager em) {
        vuelos.put(vuelo.getNombre(), vuelo);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(vuelo);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
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
}
