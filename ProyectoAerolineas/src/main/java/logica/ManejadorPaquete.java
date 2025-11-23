package logica;

import DataTypes.DtCliente;
import DataTypes.DtItemPaquete;
import DataTypes.DtPaquete;
import DataTypes.DtRutaVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manejador singleton responsable de la gestión en memoria y persistencia de los paquetes.
 * Provee operaciones para cargar, agregar, modificar y consultar paquetes y sus ítems.
 */
public final class ManejadorPaquete {

    /** Mapa en memoria de paquetes indexado por nombre. */
    private Map<String, Paquete> paquetes;
    private static ManejadorPaquete instancia = null;

    private ManejadorPaquete() {
        paquetes = new HashMap<>();
    }

    /**
     * Devuelve la instancia singleton del manejador de paquetes.
     * @return instancia única de ManejadorPaquete
     */
    public static ManejadorPaquete getInstance() {
        if (instancia == null) {
            instancia = new ManejadorPaquete();
        }
        return instancia;
    }

    // =================== CRUD BD ===================
    /**
     * Carga todos los paquetes persistidos desde la base de datos al mapa en memoria.
     * @param entManager EntityManager usado para la consulta
     */
    public void cargarPaquetesDesdeBD(EntityManager entManager) {
        TypedQuery<Paquete> query = entManager.createQuery("SELECT p FROM Paquete p", Paquete.class);
        List<Paquete> paquetesPersistidos = query.getResultList();
        for (Paquete p : paquetesPersistidos) {
            paquetes.put(p.getNombre(), p);
        }
    }

    /**
     * Agrega un paquete nuevo en memoria y lo persiste en la base de datos.
     * @param paquete objeto Paquete a agregar
     * @param entManager EntityManager de persistencia
     */
    public void agregarPaquete(Paquete paquete, EntityManager entManager) {
        if (!paquetes.containsKey(paquete.getNombre())) {
            paquetes.put(paquete.getNombre(), paquete);
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.persist(paquete);
                entTransaction.commit();
            } catch (PersistenceException e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("El paquete con el nombre " + paquete.getNombre() + " ya existe.");
        }
    }

    /**
     * Agrega una ruta como ItemPaquete dentro de un paquete y persiste el cambio.
     * Valida que no exista previamente el mismo item con el mismo tipo de asiento.
     */
    public void agregarRutaAPaquete(Paquete paquete, RutaVuelo ruta, int cantidadAsientos, TipoAsiento tipoAsiento, EntityManager entManager) {
        // Verificar que la ruta no esté ya en el paquete con el mismo tipo de asiento
        for (ItemPaquete item : paquete.getItemPaquetes()) {
            if (item.getRutaVuelo().equals(ruta) && item.getTipoAsiento() == tipoAsiento) {
                throw new IllegalArgumentException("La ruta ya fue agregada previamente al paquete con ese tipo de asiento.");
            }
        }

        // Crear y agregar el nuevo item
        ItemPaquete nuevoItem = new ItemPaquete(ruta, cantidadAsientos, tipoAsiento);
        paquete.getItemPaquetes().add(nuevoItem);

        // Recalcular costo
        double costoTotal = 0;
        for (ItemPaquete item : paquete.getItemPaquetes()) {
            double costoRuta = item.getTipoAsiento() == TipoAsiento.TURISTA
                    ? item.getRutaVuelo().getCostoTurista()
                    : item.getRutaVuelo().getCostoEjecutivo();
            costoTotal += costoRuta * item.getCantAsientos();
        }
        double costoConDescuento = costoTotal * (1 - paquete.getDescuentoPorc() / 100.0);
        paquete.setCosto(costoConDescuento);

        // Persistir en BD
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.merge(paquete);
            entTransaction.commit();
        } catch (PersistenceException e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            throw new IllegalArgumentException("Error al guardar la ruta en el paquete: " + e.getMessage());
        }
    }

    /**
     * Registra la compra de un paquete por un cliente: crea la compra, la persiste
     * y actualiza las listas relacionadas.
     */
    public void compraPaquete(Paquete paquete, DtCliente dtCliente, int validezDias, LocalDate fechaC, double costo, EntityManager entManager) {
        if (paquete == null) {
            throw new IllegalArgumentException("El paquete no puede ser null");
        }
        // Obtener el objeto Cliente real, no DtCliente
        Cliente clienteObj = ManejadorCliente.getInstance().obtenerClienteReal(dtCliente.getNickname());
        if (clienteObj == null) {
            throw new IllegalArgumentException("El cliente con nickname " + dtCliente.getNickname() + " no existe.");
        }

        CompraPaqLogica nuevaCompra = new CompraPaqLogica(clienteObj, paquete, fechaC, validezDias, costo);

        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();

            // Persistir la compra
            entManager.persist(nuevaCompra);

            // Agregar a las listas
            paquete.getCompras().add(nuevaCompra);
            clienteObj.getComprasPaquetes().add(nuevaCompra);

            entManager.merge(paquete);
            entManager.merge(clienteObj);
            entTransaction.commit();
        } catch (PersistenceException e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            throw new IllegalStateException("Error al comprar el paquete: " + e.getMessage(), e);
        }
    }

    // =================== Consultas en memoria ===================
    /**
     * Obtiene el paquete por su nombre.
     * @param nombre nombre identificador del paquete
     * @return instancia de Paquete o null si no existe
     */
    public Paquete obtenerPaquete(String nombre) { return paquetes.get(nombre); }

    /**
     * Devuelve la lista de paquetes en forma de DTO.
     */
    public List<DtPaquete> getPaquetes() {
        List<DtPaquete> lista = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
            lista.add(new DtPaquete(p)); // Asegúrate de que exista un constructor adecuado en DtPaquete
        }
        return lista;
    }

    /**
     * Devuelve la lista de paquetes disponibles (no comprados) en forma de DTO.
     */
    public List<DtPaquete> getPaquetesDisp() {
        List<DtPaquete> disponibles = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
            if (!p.estaComprado()) {
                disponibles.add(new DtPaquete(p));
            }
        }
        return disponibles;
    }

    /**
     * Devuelve el DTO de un paquete por nombre.
     */
    public DtPaquete obtenerDtPaquete(String nombrePaquete) {
        Paquete paquete = paquetes.get(nombrePaquete);
        if (paquete != null) {
            return new DtPaquete(paquete);
        }
        return null;
    }

    /**
     * Devuelve los DTs de los items asociados a un paquete.
     */
    public List<DtItemPaquete> obtenerDtItemsPaquete(String nombrePaquete) {
        Paquete paquete = paquetes.get(nombrePaquete);
        if (paquete != null) {
            List<DtItemPaquete> items = new ArrayList<>();
            for (ItemPaquete item : paquete.getItemPaquetes()) {
                // Crear el DtRutaVuelo correspondiente al Item
                RutaVuelo ruta = item.getRutaVuelo();
                DtRutaVuelo dtRuta = new DtRutaVuelo(
                        ruta.getNombre(),
                        ruta.getDescripcion(),
                        ruta.getDescripcionCorta(),
                        ruta.getImagenUrl(),                    // ✅ imagenUrl
                        ruta.getVideoUrl(),                     // ✅ videoUrl
                        ruta.getAerolinea().getNombre(),        // ✅ aerolinea (solo una vez)
                        ruta.getCiudadOrigen(),                 // ✅ ciudadOrigen (solo una vez)
                        ruta.getCiudadDestino(),                // ✅ ciudadDestino (solo una vez)
                        ruta.getHora(),                         // ✅ hora (solo una vez)
                        ruta.getFechaAlta(),
                        ruta.getCostoTurista(),
                        ruta.getCostoEjecutivo(),
                        ruta.getCostoEquipajeExtra(),
                        ruta.getEstado().toString(),
                        ruta.getCategorias(),
                        ruta.getDtVuelos()
                );

                // Crear el DtItemPaquete con la ruta, cantidad y tipo
                DtItemPaquete dtItem = new DtItemPaquete(
                        dtRuta,
                        item.getCantAsientos(),
                        item.getTipoAsiento().toString()
                );

                items.add(dtItem);
            }
            return items;
        }
        return new ArrayList<>(); // devolver lista vacía si no existe
    }

}
