package Logica;

import DataTypes.DtCliente;
import DataTypes.DtPaquete;
import DataTypes.DtRutaVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.*;

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

    // =================== CRUD BD ===================
    public void cargarPaquetesDesdeBD(EntityManager em) {
        TypedQuery<Paquete> query = em.createQuery("SELECT p FROM Paquete p", Paquete.class);
        List<Paquete> paquetesPersistidos = query.getResultList();
        for (Paquete p : paquetesPersistidos) {
            paquetes.put(p.getNombre(), p);
        }
    }

    public void agregarPaquete(Paquete p, EntityManager em) {
        if (!paquetes.containsKey(p.getNombre())) {
            paquetes.put(p.getNombre(), p);
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
            throw new IllegalArgumentException("El paquete con el nombre " + p.getNombre() + " ya existe.");
        }
    }

    public void agregarRutaPaquete(Paquete p, RutaVuelo ruta, int cantidadAsientos, TipoAsiento tipoAsiento, EntityManager em) {
        ItemPaquete existente = null;
        for (ItemPaquete item : p.getItemPaquetes()) {
            if (item.getRutaVuelo().equals(ruta) && item.getTipoAsiento() == tipoAsiento) {
                existente = item;
                break;
            }
        }
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (existente != null) {
                existente.incrementarCantidad(cantidadAsientos);
                em.merge(existente);
            } else {
                ItemPaquete nuevo = new ItemPaquete(ruta, cantidadAsientos, tipoAsiento);
                p.getItemPaquetes().add(nuevo);
                em.persist(nuevo); // Persistir explícitamente el nuevo ItemPaquete
            }
            em.merge(p);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    public void compraPaquete(Paquete p, DtCliente dtCliente, int validezDias, LocalDate fechaC, double costo, EntityManager em) {
        // Obtener el objeto Cliente real, no DtCliente
        Cliente clienteObj = ManejadorCliente.getInstance().obtenerClienteReal(dtCliente.getNickname());
        if (clienteObj == null) {
            throw new IllegalArgumentException("El cliente con nickname " + dtCliente.getNickname() + " no existe.");
        }

        CompraPaqLogica nuevaCompra = new CompraPaqLogica(clienteObj, p, fechaC, validezDias, costo);
        p.getCompras().add(nuevaCompra);
        clienteObj.getPaquetesComprados().add(p);

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(p);
            em.merge(clienteObj);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    // =================== Consultas en memoria ===================
    public Paquete obtenerPaquete(String nombre) { return paquetes.get(nombre); }


    public List<DtPaquete> getPaquetes() {
        List<DtPaquete> lista = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
            lista.add(new DtPaquete(p)); // Asegúrate de que exista un constructor adecuado en DtPaquete
        }
        return lista;
    }


    public List<DtPaquete> getPaquetesDisp() {
        List<DtPaquete> disponibles = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
            if (!p.estaComprado()) {
                disponibles.add(new DtPaquete(p));
            }
        }
        return disponibles;
    }
}
