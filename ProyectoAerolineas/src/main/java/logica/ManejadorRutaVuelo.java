package logica;

import DataTypes.DtVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


import static logica.EstadoRuta.*;

/**
 * Administra las rutas de vuelo en memoria y su persistencia.
 * Permite cargar, agregar y actualizar rutas y vuelos asociados.
 */
public final class ManejadorRutaVuelo {

    private static final Logger LOGGER = Logger.getLogger(ManejadorRutaVuelo.class.getName());

    private Map<String, RutaVuelo> rutasVuelo;
    private static ManejadorRutaVuelo instancia = null;

    private ManejadorRutaVuelo() {
        rutasVuelo = new ConcurrentHashMap<>();
    }

    /** Devuelve la instancia singleton del manejador de rutas. */
    public static ManejadorRutaVuelo getInstance() {
        if (instancia == null) {
            instancia = new ManejadorRutaVuelo();
        }
        return instancia;
    }

    /**
     * Helper que sincroniza la referencia de la ruta con la Aerolinea en memoria
     * (si existe la aerolínea cargada en ManejadorAerolinea). Esto evita tener
     * dos instancias distintas en memoria para la misma ruta y mantiene las
     * listas/arrays coherentes tras cambios en BD.
     */
    private void syncWithAerolinea(RutaVuelo ruta) {
        if (ruta == null) return;
        Aerolinea aero = null;
        try {
            if (ruta.getAerolinea() != null && ruta.getAerolinea().getNickname() != null) {
                aero = ManejadorAerolinea.getInstance().obtenerAerolinea(ruta.getAerolinea().getNickname());
            }
        } catch (Throwable t) {
            // No crítico: si falla buscar la aerolínea en memoria, seguimos
            LOGGER.log(Level.FINE, "No se pudo sincronizar con Aerolinea en memoria: " + t.getMessage());
        }

        if (aero != null) {
            try {
                aero.getRutasVueloMap().put(ruta.getNombre(), ruta);
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "No se pudo actualizar mapa de la Aerolinea: " + e.getMessage());
            }
        }
    }

    /**
     * Incrementa el contador de visitas de una ruta y actualiza la persistencia.
     * @param nombreRuta nombre de la ruta a incrementar
     * @param entManager EntityManager para la operación
     */
     public void incrementarVisitasRuta(String nombreRuta, EntityManager entManager) {
         RutaVuelo ruta = rutasVuelo.get(nombreRuta);
         if (ruta != null) {
             ruta.incrementarVisitas();
             EntityTransaction entTransaction = entManager.getTransaction();
             try {
                 entTransaction.begin();
                 entManager.merge(ruta);
                 entTransaction.commit();
                 System.out.println("[MANEJADOR] ✅ Visitas incrementadas para ruta: " + nombreRuta + " - Total: " + ruta.getContadorVisitas());
                // Asegurar sincronía con la Aerolinea en memoria
                syncWithAerolinea(ruta);
             } catch (PersistenceException e) {
                 if (entTransaction.isActive()) entTransaction.rollback();
                 throw new IllegalStateException("Error incrementando visitas de la ruta: " + e.getMessage(), e);
             }
         } else {
             throw new IllegalArgumentException("Ruta no encontrada: " + nombreRuta);
         }
     }

    /**
     * Obtiene las rutas más visitadas ordenadas por contador de visitas.
     * @param limite número máximo de rutas a retornar
     * @return lista de rutas ordenadas por visitas (descendente)
     */
     public List<RutaVuelo> getTopRutasMasVisitadas(int limite) {
         List<RutaVuelo> todasRutas = new ArrayList<>(rutasVuelo.values());

         // Ordenar por contador de visitas descendente
         todasRutas.sort((r1, r2) -> Integer.compare(r2.getContadorVisitas(), r1.getContadorVisitas()));

         // Limitar resultados
         return todasRutas.subList(0, Math.min(limite, todasRutas.size()));
     }

    /**
     * Obtiene las rutas más visitadas que estén en estado CONFIRMADA.
     * @param limite número máximo de rutas a retornar
     * @return lista de rutas confirmadas ordenadas por visitas
     */
    public List<RutaVuelo> getTopRutasConfirmadasMasVisitadas(int limite) {
        List<RutaVuelo> rutasConfirmadas = new ArrayList<>();

        // Filtrar solo rutas confirmadas
        for (RutaVuelo ruta : rutasVuelo.values()) {
            if (ruta.getEstado() == CONFIRMADA) {
                rutasConfirmadas.add(ruta);
            }
        }

        // Ordenar por contador de visitas descendente (manualmente)
        for (int i = 0; i < rutasConfirmadas.size() - 1; i++) {
            for (int j = i + 1; j < rutasConfirmadas.size(); j++) {
                RutaVuelo rutaI = rutasConfirmadas.get(i);
                RutaVuelo rutaJ = rutasConfirmadas.get(j);

                if (rutaI.getContadorVisitas() < rutaJ.getContadorVisitas()) {
                    // Intercambiar posiciones
                    rutasConfirmadas.set(i, rutaJ);
                    rutasConfirmadas.set(j, rutaI);
                }
            }
        }

        // Limitar resultados
        if (rutasConfirmadas.size() > limite) {
            return new ArrayList<>(rutasConfirmadas.subList(0, limite));
        }

        return rutasConfirmadas;
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
                 r.setEstado(INGRESADA);
             }
            // Forzar inicialización de colecciones lazy mientras el EntityManager está abierto
            try {
                if (r.getVuelos() != null) r.getVuelos().size();
                if (r.getCategorias() != null) r.getCategorias().size();
                if (r.getAerolinea() != null) {
                    // acceder al nickname para inicializar la relación ManyToOne
                    r.getAerolinea().getNickname();
                }
            } catch (Exception ignore) {
                // No crítico: si no se puede inicializar alguna colección, seguimos
            }
            rutasVuelo.put(r.getNombre(), r);
            // Cuando cargamos desde BD, asegurarnos que la Aerolinea en memoria (si existe)
            // use la misma instancia de RutaVuelo para evitar inconsistencias.
            syncWithAerolinea(r);
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

        } catch (PersistenceException e) {
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
             EntityTransaction entTransaction = entManager.getTransaction();
             try {
                 entTransaction.begin();

                 // Reatachar/obtener la instancia gestionada por este EntityManager
                 RutaVuelo managed = null;
                 Long id = ruta.getIdRutaVuelo();
                 if (id != null) {
                     managed = entManager.find(RutaVuelo.class, id);
                 }

                 if (managed == null) {
                     TypedQuery<RutaVuelo> q = entManager.createQuery("SELECT r FROM RutaVuelo r WHERE r.nombre = :n", RutaVuelo.class);
                     q.setParameter("n", nombreRuta);
                     List<RutaVuelo> res = q.getResultList();
                     if (res.isEmpty()) {
                         throw new IllegalArgumentException("Ruta no encontrada en la base de datos: " + nombreRuta);
                     }
                     managed = res.get(0);
                 }

                 // Asegurar que la Aerolinea asociada esté gestionada por este EntityManager (opcional)
                 if (managed.getAerolinea() != null) {
                     try {
                         String aeroNick = managed.getAerolinea().getNickname();
                         if (aeroNick != null) {
                             Aerolinea managedAero = entManager.find(Aerolinea.class, aeroNick);
                             if (managedAero != null) {
                                 managed.setAerolinea(managedAero);
                             }
                         }
                     } catch (Exception e) {
                         LOGGER.log(Level.FINE, "No se pudo asegurar Aerolinea gestionada: " + e.getMessage());
                     }
                 }

                 // Ahora la entidad está gestionada dentro de la transacción: modificar su colección
                 managed.agregarVuelo(vuelo);

                 // Merge/persist cascada manejará la persistencia del nuevo vuelo (cascade = ALL)
                 entManager.merge(managed);

                 entTransaction.commit();

                 // Actualizar la referencia en memoria para que apunte a la instancia gestionada
                rutasVuelo.put(nombreRuta, managed);
                // Sincronizar con la Aerolinea en memoria
                syncWithAerolinea(managed);

             } catch (PersistenceException | IllegalArgumentException e) {
                 if (entTransaction.isActive()) entTransaction.rollback();
                 e.printStackTrace();
             }
         } else {
             System.out.println("Ruta de vuelo no encontrada.");
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
                    ruta.getAerolinea().getNickname() != null &&
                    ruta.getAerolinea().getNickname().equals(nombreAerolinea)) {
                rutas.add(ruta);
            }
        }
        return rutas;
    }

    /**
     * Filtra rutas por estado y aerolínea.
     */
    public List<RutaVuelo> getRutasPorEstadoYAerolinea(String nombreAerolinea, EstadoRuta estado) {
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
    public void cambiarEstadoRuta(String nombreRuta, EstadoRuta nuevoEstado, EntityManager entManager) {
         RutaVuelo ruta = rutasVuelo.get(nombreRuta);
         if (ruta != null) {
             EntityTransaction entTransaction = entManager.getTransaction();
             try {
                 entTransaction.begin();

                 // Intentar obtener la entidad gestionada desde el EntityManager
                 RutaVuelo managed = null;
                 Long id = ruta.getIdRutaVuelo();
                 if (id != null) {
                     managed = entManager.find(RutaVuelo.class, id);
                 }

                 // Si no se encontró por id, buscar por nombre en la BD
                 try {
                     TypedQuery<RutaVuelo> q = entManager.createQuery("SELECT r FROM RutaVuelo r WHERE r.nombre = :n", RutaVuelo.class);
                     q.setParameter("n", nombreRuta);
                     List<RutaVuelo> res = q.getResultList();
                     if (res.isEmpty()) {
                         throw new IllegalArgumentException("Ruta no encontrada en la base de datos: " + nombreRuta);
                     }
                     managed = res.get(0);

                     // Asegurar que la Aerolinea asociada esté gestionada por este EntityManager
                     if (managed.getAerolinea() != null) {
                         try {
                             String aeroNick = managed.getAerolinea().getNickname();
                             if (aeroNick != null) {
                                 Aerolinea managedAero = entManager.find(Aerolinea.class, aeroNick);
                                 if (managedAero != null) {
                                     managed.setAerolinea(managedAero);
                                 }
                             }
                         } catch (Exception e) {
                             // no crítico, seguimos adelante
                             LOGGER.log(Level.FINE, "No se pudo asegurar Aerolinea gestionada: " + e.getMessage());
                         }
                     }

                     // Actualizar el estado en la entidad gestionada y hacer flush
                     managed.setEstado(nuevoEstado);
                     entManager.merge(managed);
                 } catch (IllegalArgumentException iae) {
                     // No existe en BD -> propagar para que la capa superior lo maneje
                     throw iae;
                 }

                 entTransaction.commit();
                 // Asegurar que el mapa en memoria referencia la instancia gestionada
                //rutasVuelo.put(nombreRuta, managed);
                ruta.setEstado(nuevoEstado);

                 // Sincronizar con la Aerolinea en memoria para que su mapa también vea el nuevo estado
                syncWithAerolinea(managed);
                System.out.println("[MANEJADOR] ✅ Estado cambiado para ruta '" + nombreRuta + "' a: " + nuevoEstado);
             } catch (PersistenceException e) {
                 if (entTransaction.isActive()) entTransaction.rollback();
                 // Log completo con causa raíz para diagnosticar el fallo al commitear
                 LOGGER.log(Level.SEVERE, "Error cambiando estado de la ruta '" + nombreRuta + "' a '" + nuevoEstado + "'", e);
                 Throwable cause = e.getCause();
                 if (cause != null) {
                     LOGGER.log(Level.SEVERE, "Causa raíz:", cause);
                 }
                 throw new IllegalStateException("Error cambiando estado de la ruta: " + e.getMessage(), e);
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
                // Mantener consistencia en la Aerolinea en memoria
                syncWithAerolinea(ruta);
             } catch (PersistenceException e) {
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
