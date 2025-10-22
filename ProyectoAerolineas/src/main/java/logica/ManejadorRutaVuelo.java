package logica;

import DataTypes.DtVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Administra las rutas de vuelo en memoria y su persistencia.
 * Permite cargar, agregar y actualizar rutas y vuelos asociados.
 */
public final class ManejadorRutaVuelo {

    private Map<String, RutaVuelo> rutasVuelo;
    private static ManejadorRutaVuelo instancia = null;

    private ManejadorRutaVuelo() {
        rutasVuelo = new HashMap<>();
    }

    /** Devuelve la instancia singleton del manejador de rutas. */
    public static ManejadorRutaVuelo getInstance() {
        if (instancia == null) {
            instancia = new ManejadorRutaVuelo();
        }
        return instancia;
    }

    // =================== CRUD BD ===================
    /**
     * Carga las rutas persistidas desde la base de datos al mapa en memoria.
     * @param entManager EntityManager usado para la consulta
     */
    public void cargarRutasDesdeBD(EntityManager entManager) {
        TypedQuery<RutaVuelo> query = entManager.createQuery("SELECT r FROM RutaVuelo r", RutaVuelo.class);
        List<RutaVuelo> rutasPersistidas = query.getResultList();
        for (RutaVuelo r : rutasPersistidas) {
            // Inicializar estado si es nulo
            if (r.getEstado() == null) {
                r.setEstado(RutaVuelo.EstadoRuta.INGRESADA);
            }
            rutasVuelo.put(r.getNombre(), r);
        }
    }

    /**
     * Agrega una nueva ruta en memoria y la persiste en la base de datos.
     * @param ruta RutaVuelo a agregar
     * @param entManager EntityManager para la persistencia
     */
    public void agregarRutaVuelo(RutaVuelo ruta, EntityManager entManager) {
        rutasVuelo.put(ruta.getNombre(), ruta);
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.persist(ruta);
            entTransaction.commit();
        } catch (Exception e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * Agrega un vuelo a una ruta existente y actualiza la persistencia.
     * @param nombreRuta nombre de la ruta destino
     * @param vuelo vuelo a agregar
     * @param entManager EntityManager para la operación
     */
    public void agregarVueloARuta(String nombreRuta, Vuelo vuelo, EntityManager entManager) {
        RutaVuelo ruta = rutasVuelo.get(nombreRuta);
        if (ruta != null) {
            ruta.agregarVuelo(vuelo);
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.merge(ruta);
                entTransaction.commit();
            } catch (Exception e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                e.printStackTrace();
            }
        } else {
            //System.out.println("Ruta de vuelo no encontrada.");
        }
    }

    // =================== Consultas en memoria ===================
    /**
     * Obtiene una ruta por su nombre desde la cache en memoria.
     * @param nombre nombre de la ruta
     * @return RutaVuelo o null si no existe
     */
    public RutaVuelo getRuta(String nombre) {
        return rutasVuelo.get(nombre);
    }

    /**
     * Obtiene un vuelo de una ruta específica.
     * @param nombreRuta nombre de la ruta
     * @param nombreVuelo nombre del vuelo
     * @return Vuelo o null si no existe
     */
    public Vuelo getVueloDeRuta(String nombreRuta, String nombreVuelo) {
        RutaVuelo ruta = rutasVuelo.get(nombreRuta);
        if (ruta != null) return ruta.getVuelo(nombreVuelo);
        return null;
    }

    /**
     * Lista las rutas pertenecientes a una aerolínea.
     * @param nombreAerolinea nickname o nombre de la aerolínea
     * @return lista de rutas pertenecientes a la aerolínea
     */
    public List<RutaVuelo> getRutasPorAerolinea(String nombreAerolinea) {
        List<RutaVuelo> rutas = new ArrayList<>();
        for (RutaVuelo ruta : rutasVuelo.values()) {
            if (ruta.getAerolinea() != null &&
                    ruta.getAerolinea().getNombre().equals(nombreAerolinea)) {
                rutas.add(ruta);
            }
        }
        return rutas;
    }

    /**
     * Filtra rutas por estado y aerolínea.
     */
    public List<RutaVuelo> getRutasPorEstadoYAerolinea(String nombreAerolinea, RutaVuelo.EstadoRuta estado) {
        List<RutaVuelo> rutasFiltradas = new ArrayList<>();
        for (RutaVuelo ruta : rutasVuelo.values()) {
            if (ruta.getAerolinea() != null &&
                    ruta.getAerolinea().getNombre().equals(nombreAerolinea) &&
                    ruta.getEstado() == estado) {
                rutasFiltradas.add(ruta);
            }
        }
        return rutasFiltradas;
    }

    /**
     * Cambia el estado de una ruta y actualiza la persistencia.
     */
    public void cambiarEstadoRuta(String nombreRuta, RutaVuelo.EstadoRuta nuevoEstado, EntityManager entManager) {
        RutaVuelo ruta = rutasVuelo.get(nombreRuta);
        if (ruta != null) {
            ruta.setEstado(nuevoEstado);
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.merge(ruta);
                entTransaction.commit();
            } catch (Exception e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Ruta de vuelo no encontrada: " + nombreRuta);
        }
    }

    /**
     * Devuelve todas las rutas en memoria.
     */
    public List<RutaVuelo> getRutas() {
        return new ArrayList<>(rutasVuelo.values());
    }

    /**
     * Actualiza la URL de la imagen de una ruta y persiste el cambio.
     */
    public void actualizarImagenRuta(String nombreRuta, String imagenUrl, EntityManager entManager) {
        RutaVuelo ruta = rutasVuelo.get(nombreRuta);
        if (ruta != null) {
            ruta.setImagenUrl(imagenUrl);
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.merge(ruta);
                entTransaction.commit();
                //System.out.println("Imagen actualizada para ruta: " + nombreRuta);
            } catch (Exception e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                throw new IllegalStateException("Error actualizando imagen de la ruta: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Ruta no encontrada: " + nombreRuta);
        }
    }

    /**
     * Devuelve los DTs de vuelos asociados a una ruta.
     */
    public List<DtVuelo> obtenerVuelosPorRuta(String nombreRuta) {
        RutaVuelo ruta = rutasVuelo.get(nombreRuta);
        if (ruta != null) {
            List<DtVuelo> dtVuelos = new ArrayList<>();
            for (Vuelo vuelo : ruta.getVuelos()) {
                dtVuelos.add(vuelo.getDtVuelo());
            }
            return dtVuelos;
        }
        return Collections.emptyList();
    }
}