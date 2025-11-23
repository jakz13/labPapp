package logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;

import java.util.*;

public final class ManejadorFollow {

    private final Map<Long, Follow> followsEnMemoria;
    private static ManejadorFollow instancia = null;

    private ManejadorFollow() {
        followsEnMemoria = new HashMap<>();
    }

    public static ManejadorFollow getInstance() {
        if (instancia == null) {
            instancia = new ManejadorFollow();
        }
        return instancia;
    }

    // ===========================================================
    // =============== CARGAR FOLLOWS DESDE BD ===================
    // ===========================================================

    public void cargarFollowsDesdeBD(EntityManager em) {
        TypedQuery<Follow> query = em.createQuery("SELECT f FROM Follow f", Follow.class);
        List<Follow> lista = query.getResultList();
        followsEnMemoria.clear();

        for (Follow f : lista) {
            followsEnMemoria.put(f.getId(), f);

            Usuario seguidor = f.getSeguidor();
            Usuario seguido = f.getSeguido();
        }
    }

    // ===========================================================
    // ========================== FOLLOW =========================
    // ===========================================================

    public void followUsuario(String followerNick, String targetNick, EntityManager em) {
        System.out.println(">>> VOY A INSERTAR FOLLOW");

        if (followerNick == null || targetNick == null)
            throw new IllegalArgumentException("Nick vacío");

        if (followerNick.equals(targetNick))
            throw new IllegalArgumentException("No se puede seguir a uno mismo");

        Usuario follower = em.find(Usuario.class, followerNick);
        Usuario target   = em.find(Usuario.class, targetNick);

        if (follower == null)
            throw new IllegalArgumentException("Seguidor no encontrado");
        if (target == null)
            throw new IllegalArgumentException("Usuario objetivo no encontrado");

        // Verificar si ya existe la relación
        long count = em.createQuery(
                        "SELECT COUNT(f) FROM Follow f WHERE f.seguidor = :seg AND f.seguido = :tar",
                        Long.class
                )
                .setParameter("seg", follower)
                .setParameter("tar", target)
                .getSingleResult();

        if (count > 0)
            throw new IllegalArgumentException("Ya lo sigue");

        // Crear follow
        Follow follow = new Follow(follower, target);

        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            System.out.println(">>> VOY A INSERTAR FOLLOW");
            em.persist(follow);
            em.flush(); // fuerza INSERT inmediato
            followsEnMemoria.put(follow.getId(), follow);

            tx.commit();

        } catch (PersistenceException e) {
            if (tx.isActive()) tx.rollback();
            throw new IllegalStateException("Error al persistir Follow: " + e.getMessage(), e);
        }
    }


    // ===========================================================
    // ======================== UNFOLLOW =========================
    // ===========================================================

    public void unfollowUsuario(String followerNick, String targetNick, EntityManager em) {

        if (followerNick == null || targetNick == null)
            throw new IllegalArgumentException("Nick vacío");

        Usuario follower = em.find(Usuario.class, followerNick);
        Usuario target   = em.find(Usuario.class, targetNick);

        if (follower == null || target == null)
            throw new IllegalArgumentException("Usuario no encontrado");

        // Buscar si existe el follow
        Follow follow = obtenerFollowEntidad(follower, target, em);

        if (follow == null)
            throw new IllegalArgumentException("No estaba siguiendo");

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Eliminar de la BD
            em.remove(follow);

            // Eliminar de memoria
            followsEnMemoria.remove(follow.getId());

            tx.commit();

        } catch (PersistenceException e) {
            if (tx.isActive()) tx.rollback();
            throw new IllegalStateException("Error al borrar follow: " + e.getMessage(), e);
        }
    }


    // ===========================================================
    // ========= MÉTODOS AUXILIARES / CONSULTA ==================
    // ===========================================================

    private Follow obtenerFollowEntidad(Usuario seguidor, Usuario seguido, EntityManager em) {
        TypedQuery<Follow> q = em.createQuery(
                "SELECT f FROM Follow f WHERE f.seguidor = :seg AND f.seguido = :target",
                Follow.class
        );
        q.setParameter("seg", seguidor);
        q.setParameter("target", seguido);

        List<Follow> res = q.getResultList();
        return res.isEmpty() ? null : res.get(0);
    }

    public boolean isFollowing(String follower, String target, EntityManager em) {
        Long count = em.createQuery(
                        "SELECT COUNT(f) FROM Follow f " +
                                "WHERE f.seguidor.nickname = :follower " +
                                "AND f.seguido.nickname = :target",
                        Long.class
                )
                .setParameter("follower", follower)
                .setParameter("target", target)
                .getSingleResult();

        return count > 0;
    }


    // Cantidad de usuarios que sigue un usuario
    public long cantidadSeguidos(Usuario usuario, EntityManager em) {
        if (usuario == null) throw new IllegalArgumentException("Usuario nulo");

        String jpql = "SELECT COUNT(f) FROM Follow f WHERE f.seguidor = :usuario";
        return em.createQuery(jpql, Long.class)
                .setParameter("usuario", usuario)
                .getSingleResult();
    }

    // Cantidad de usuarios que siguen a un usuario
    public long cantidadSeguidores(Usuario usuario, EntityManager em) {
        if (usuario == null) throw new IllegalArgumentException("Usuario nulo");

        String jpql = "SELECT COUNT(f) FROM Follow f WHERE f.seguido = :usuario";
        return em.createQuery(jpql, Long.class)
                .setParameter("usuario", usuario)
                .getSingleResult();
    }



}
