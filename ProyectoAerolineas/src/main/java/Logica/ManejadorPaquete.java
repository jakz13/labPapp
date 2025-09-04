package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ManejadorPaquete {
    private Map<String, Paquete> paquetes;
    private static ManejadorPaquete instancia = null;

    private ManejadorPaquete() {
        paquetes = new HashMap<>();
    }

    public static ManejadorPaquete getInstance() {
        if (instancia == null) {
            instancia = new ManejadorPaquete();
        }
        return instancia;
    }

    public void cargarPaquetesDesdeBD(EntityManager em) {
        TypedQuery<Paquete> query = em.createQuery("SELECT p FROM Paquete p", Paquete.class);
        List<Paquete> paquetesPersistidos = query.getResultList();
        for (Paquete p : paquetesPersistidos) {
            paquetes.put(p.getNombre(), p);
        }
    }

    public void crearPaquete(Paquete p, EntityManager em) {
        String nombre = p.getNombre();
        if (this.obtenerPaquete(nombre) == null) {
            paquetes.put(nombre, p);
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(p);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("El paquete con el nombre " + nombre + " ya existe.");
        }
    }

    public Paquete obtenerPaquete(String nombre) {
        return paquetes.get(nombre);
    }

    public void agregarRutaPaquete(Paquete p, RutaVuelo ruta, int cantidadAsientos, TipoAsiento tipoAsiento) {
        ItemPaquete existente = null;
        for (ItemPaquete item : p.getItemPaquetes()) {
            if (item.getRutaVuelo().equals(ruta) && item.getTipoAsiento() == tipoAsiento) {
                existente = item;
                break;
            }
        }
        if (existente != null) {
            existente.incrementarCantidad(cantidadAsientos);
        } else {
            ItemPaquete nuevo = new ItemPaquete(p,ruta, cantidadAsientos, tipoAsiento);
            p.getItemPaquetes().add(nuevo);
        }
    }

    public void compraPaquete(Paquete p, Cliente c, int validezDias, LocalDate fechaC, double costo) {
        CompraPaq nuevaCompra = new CompraPaq(c, p, fechaC, validezDias, costo);
        p.getCompras().add(nuevaCompra);
        c.getPaquetesComprados().add(p);
    }

    public List<Paquete> getPaquetes() {
        return new ArrayList<>(paquetes.values());
    }

    public List<Paquete> getPaquetesDisp() {
        List<Paquete> disponibles = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
            if (!p.estaComprado()) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }

    public Paquete buscarPaquete(String nombrePaquete) {
        return paquetes.get(nombrePaquete);
    }
}


