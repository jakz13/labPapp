package Logica;
import DataTypes.DtAerolinea;
import DataTypes.DtRutaVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // === Cargar todas las aerolíneas desde la BD a memoria ===
    public void cargarAerolineasDesdeBD(EntityManager em) {
        TypedQuery<Aerolinea> query = em.createQuery("SELECT a FROM Aerolinea a", Aerolinea.class);
        List<Aerolinea> aerolineasPersistidas = query.getResultList();
        for (Aerolinea a : aerolineasPersistidas) {
            aerolineas.put(a.getNickname(), a);
        }
    }

    public Aerolinea obtenerAerolinea(String nickname) {
        return aerolineas.get(nickname);
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

    public void agregarRutaVueloAAerolinea(String nicknameAerolinea, RutaVuelo ruta, EntityManager em) {
        Aerolinea aerolinea = obtenerAerolinea(nicknameAerolinea);
        if (aerolinea != null) {
            aerolinea.agregarRutaVuelo(ruta);

            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.merge(aerolinea); // guarda cambios en BD
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        } else {
            System.out.println("Aerolínea no encontrada.");
        }
    }

    public List<DtRutaVuelo> obtenerRutaVueloDeAerolinea(String nicknameAerolinea) {
        Aerolinea aerolinea = obtenerAerolinea(nicknameAerolinea);
        if (aerolinea != null) {
            return aerolinea.getDtRutasVuelo();
        }
        return new ArrayList<>();
    }

    public List<Aerolinea> getAerolineas() {
        return new ArrayList<>(aerolineas.values());
    }

    public List<DtAerolinea> getDtAerolineas() {
        List<DtAerolinea> dtAerolineas = new ArrayList<>();
        for (Aerolinea a : aerolineas.values()) {
            dtAerolineas.add(new DtAerolinea(a.getNickname(), a.getNombre(), a.getEmail(), a.getDescripcion(), a.getSitioWeb(), a.getDtRutasVuelo()));
        }
        return dtAerolineas;
    }

    public void modificarDatosAerolinea(Aerolinea aerolineaTemporal, EntityManager em) {
        Aerolinea aerolineaOriginal = obtenerAerolinea(aerolineaTemporal.getNickname());
        if (aerolineaOriginal == null) {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }

        if (aerolineaTemporal.getNombre() != null && !aerolineaTemporal.getNombre().isEmpty()) {
            aerolineaOriginal.setNombre(aerolineaTemporal.getNombre());
        }
        if (aerolineaTemporal.getDescripcion() != null && !aerolineaTemporal.getDescripcion().isEmpty()) {
            aerolineaOriginal.setDescripcion(aerolineaTemporal.getDescripcion());
        }
        if (aerolineaTemporal.getSitioWeb() != null && !aerolineaTemporal.getSitioWeb().isEmpty()) {
            aerolineaOriginal.setSitioWeb(aerolineaTemporal.getSitioWeb());
        }

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(aerolineaOriginal);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    public List<DtAerolinea> getAerolinea() {
        List<DtAerolinea> dtAerolineas = new ArrayList<>();
        for (Aerolinea a : aerolineas.values()) {
            dtAerolineas.add(new DtAerolinea(a.getNickname(), a.getNombre(), a.getEmail(), a.getDescripcion(), a.getSitioWeb(), a.getDtRutasVuelo()));
        }
        return dtAerolineas;
    }
}
