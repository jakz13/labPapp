package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
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

    public void cargarCiudadesDesdeBD(EntityManager em) {
        TypedQuery<Ciudad> query = em.createQuery("SELECT c FROM Ciudad c", Ciudad.class);
        List<Ciudad> ciudadesPersistidas = query.getResultList();
        for (Ciudad c : ciudadesPersistidas) {
            ciudades.put(c.getNombre(), c);
        }
    }

    public void agregarCiudad(Ciudad c, EntityManager em) {
        ciudades.put(c.getNombre(), c);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(c);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    public Ciudad obtenerCiudad(String nombre) {
        return ciudades.get(nombre);
    }

    public List<Ciudad> getCiudades() {
        return new ArrayList<>(ciudades.values());
    }
}
