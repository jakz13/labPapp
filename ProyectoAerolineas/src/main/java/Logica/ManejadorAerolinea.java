package Logica;

import DataTypes.DtAerolinea;
import DataTypes.DtRutaVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.*;

public class ManejadorAerolinea {

    private Map<String, Aerolinea> aerolineas;
    private static ManejadorAerolinea instancia = null;

    private ManejadorAerolinea() {
        aerolineas = new HashMap<>();
    }

    public static ManejadorAerolinea getInstance() {
        if (instancia == null) {
            instancia = new ManejadorAerolinea();
        }
        return instancia;
    }

    // =================== CRUD BD ===================
    public void cargarAerolineasDesdeBD(EntityManager em) {
        TypedQuery<Aerolinea> query = em.createQuery("SELECT a FROM Aerolinea a", Aerolinea.class);
        List<Aerolinea> aerolineasPersistidas = query.getResultList();
        for (Aerolinea a : aerolineasPersistidas) {
            aerolineas.put(a.getNickname(), a);
        }
    }

    public void agregarAerolinea(Aerolinea aerolinea, EntityManager em) {
        aerolineas.put(aerolinea.getNickname(), aerolinea);
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(aerolinea);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    public void agregarRutaVueloAAerolinea(String nicknameAerolinea, RutaVuelo ruta, EntityManager em) {
        Aerolinea aerolinea = obtenerAerolinea(nicknameAerolinea);
        if (aerolinea != null) {
            aerolinea.agregarRutaVuelo(ruta);
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.merge(aerolinea);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        } else {
            System.out.println("Aerolínea no encontrada.");
        }
    }

    // =================== Consultas en memoria ===================
    public Aerolinea obtenerAerolinea(String nickname) {
        return aerolineas.get(nickname);
    }

    public List<DtRutaVuelo> obtenerRutaVueloDeAerolinea(String nicknameAerolinea) {
        Aerolinea aerolinea = obtenerAerolinea(nicknameAerolinea);
        if (aerolinea != null) {
            return aerolinea.getDtRutasVuelo();
        }
        return new ArrayList<>();
    }

    public List<DtAerolinea> getDtAerolineas() {
        List<DtAerolinea> dtAerolineas = new ArrayList<>();
        for (Aerolinea a : aerolineas.values()) {
            dtAerolineas.add(new DtAerolinea(
                    a.getNickname(),
                    a.getNombre(),
                    a.getEmail(),
                    a.getDescripcion(),
                    a.getSitioWeb(),
                    a.getDtRutasVuelo()
            ));
        }
        return dtAerolineas;
    }

    public void modificarDatosAerolinea(Aerolinea aerolinea, EntityManager em) {
        if (aerolinea != null) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.merge(aerolinea);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }
    }

    public List<Aerolinea> getAerolineas() {
        return new ArrayList<>(aerolineas.values());
    }
}