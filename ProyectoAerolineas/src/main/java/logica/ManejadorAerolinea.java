package logica;

import DataTypes.DtAerolinea;
import DataTypes.DtRutaVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Gestiona en memoria y en BD las aerolíneas del sistema.
 * Provee operaciones para cargar, agregar y modificar aerolíneas y sus rutas.
 */
public final class ManejadorAerolinea {

    private Map<String, Aerolinea> aerolineas;
    private static ManejadorAerolinea instancia = null;

    private ManejadorAerolinea() {
        aerolineas = new HashMap<>();
    }

    /** Devuelve la instancia singleton del manejador. */
    public static ManejadorAerolinea getInstance() {
        if (instancia == null) {
            instancia = new ManejadorAerolinea();
        }
        return instancia;
    }

    // =================== CRUD BD ===================
    /** Carga todas las aerolíneas desde la base de datos. */
    public void cargarAerolineasDesdeBD(EntityManager entManager) {
        TypedQuery<Aerolinea> query = entManager.createQuery("SELECT a FROM Aerolinea a", Aerolinea.class);
        List<Aerolinea> aerolineasPersistidas = query.getResultList();
        for (Aerolinea a : aerolineasPersistidas) {
            aerolineas.put(a.getNickname(), a);
        }
    }

    /** Agrega una aerolínea en memoria y persiste en BD. */
    public void agregarAerolinea(Aerolinea aerolinea, EntityManager entManager) {
        aerolineas.put(aerolinea.getNickname(), aerolinea);
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.persist(aerolinea);
            entTransaction.commit();
        } catch (PersistenceException e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            e.printStackTrace();
        }
    }

    /** Agrega una ruta a la aerolínea indicada y actualiza en BD. */
    public void agregarRutaVueloAAerolinea(String nicknameAerolinea, RutaVuelo ruta, EntityManager entManager) {
        Aerolinea aerolinea = obtenerAerolinea(nicknameAerolinea);
        if (aerolinea != null) {
            aerolinea.agregarRutaVuelo(ruta);
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.merge(aerolinea);
                entTransaction.commit();
            } catch (PersistenceException  e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                throw new IllegalStateException("Error al agregar la ruta a la aerolínea", e);
            }
        }
    }

    // =================== Consultas en memoria ===================
    /** Devuelve la aerolínea por su nickname (en memoria). */
    public Aerolinea obtenerAerolinea(String nickname) {
        return aerolineas.get(nickname);
    }

    /** Busca una aerolínea por su email. */
    public Aerolinea obtenerAerolineaPorEmail(String email) {
        return aerolineas.values().stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst().orElse(null);
    }

    /** Verifica las credenciales de una aerolínea. */
    public boolean verificarLogin(String email, String password) {
        Aerolinea aerolinea = obtenerAerolineaPorEmail(email);
        if (aerolinea != null) {
            return aerolinea.verificarPassword(password);
        }
        return false;
    }

    /** Devuelve las rutas (DT) de la aerolínea indicada. */
    public List<DtRutaVuelo> obtenerRutaVueloDeAerolinea(String nicknameAerolinea) {
        Aerolinea aerolinea = obtenerAerolinea(nicknameAerolinea);
        if (aerolinea != null) {
            return aerolinea.getDtRutasVuelo();
        }
        return new ArrayList<>();
    }

    /** Devuelve una lista de aerolíneas en forma de DTO. */
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

    /** Devuelve una sola aerolínea en forma de DTO. */
    public DtAerolinea getDtAerolinea(String nickname) {
        Aerolinea a = obtenerAerolinea(nickname);
        if (a == null)
            return null;

        return new DtAerolinea(
                a.getNickname(),
                a.getNombre(),
                a.getEmail(),
                a.getDescripcion(),
                a.getSitioWeb(),
                a.getDtRutasVuelo()
        );
    }

    /** Actualiza la contraseña de una aerolínea y persiste el cambio. */
    public void actualizarPassword(String email, String nuevaPassword, EntityManager entManager) {
        Aerolinea aerolinea = obtenerAerolineaPorEmail(email);
        if (aerolinea != null) {
            aerolinea.setPassword(nuevaPassword);
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.merge(aerolinea);
                entTransaction.commit();
            } catch (PersistenceException  e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                throw new IllegalStateException("Error actualizando contraseña: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Aerolínea no encontrada: " + email);
        }
    }

    /** Modifica datos completos de una aerolínea y persiste el cambio. */
    public void modificarDatosAerolineaCompleto(String nickname, String nombre, String descripcion,
                                                String sitioWeb, String password, String imagenUrl, EntityManager entManager) {
        Aerolinea aerolinea = obtenerAerolinea(nickname);
        if (aerolinea == null) {
            throw new IllegalArgumentException("Aerolínea no encontrada: " + nickname);
        }

        if (nombre != null) aerolinea.setNombre(nombre.trim());
        if (descripcion != null) aerolinea.setDescripcion(descripcion.trim());
        if (sitioWeb != null) aerolinea.setSitioWeb(sitioWeb.trim());
        if (password != null && !password.trim().isEmpty()) aerolinea.setPassword(password.trim());
        if (imagenUrl != null) aerolinea.setImagenUrl(imagenUrl.trim().isEmpty() ? null : imagenUrl.trim());

        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.merge(aerolinea);
            entTransaction.commit();
        } catch (PersistenceException  e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            throw new IllegalStateException("Error modificando datos completos de la aerolínea: " + e.getMessage(), e);
        }
    }

    /** Actualiza la imagen de la aerolínea y persiste el cambio. */
    public void actualizarImagenAerolinea(String nickname, String imagenUrl, EntityManager entManager) {
        Aerolinea aerolinea = aerolineas.get(nickname);
        if (aerolinea != null) {
            aerolinea.setImagenUrl(imagenUrl);
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.merge(aerolinea);
                entTransaction.commit();
            } catch (PersistenceException  e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                throw new IllegalStateException("Error actualizando imagen de la aerolínea: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Aerolínea no encontrada: " + nickname);
        }
    }

    /** Persiste una aerolínea modificada. */
    public void modificarDatosAerolinea(Aerolinea aerolinea, EntityManager entManager) {
        if (aerolinea != null) {
            EntityTransaction entTransaction = entManager.getTransaction();
            try {
                entTransaction.begin();
                entManager.merge(aerolinea);
                entTransaction.commit();
            } catch (PersistenceException  e) {
                if (entTransaction.isActive()) entTransaction.rollback();
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }
    }

    /** Devuelve la lista actual de aerolíneas en memoria. */
    public List<Aerolinea> getAerolineas() {
        return new ArrayList<>(aerolineas.values());
    }
}