package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void cargarVuelosDesdeBD(EntityManager em) {
        List<Vuelo> vuelosPersistidos = em.createQuery("SELECT v FROM Vuelo v", Vuelo.class)
                .getResultList();
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

