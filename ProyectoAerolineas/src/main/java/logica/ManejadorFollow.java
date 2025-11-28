package logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.PersistenceException;

import java.util.*;

public final class ManejadorFollow {

    private final Map<Long, Follow> followsEnMemoria;

    private final Map<String, Set<String>> seguidoresMap;
    private final Map<String, Set<String>> seguidosMap;

    private static ManejadorFollow instancia = null;

    private ManejadorFollow() {

        followsEnMemoria = new HashMap<>();
        seguidoresMap = new HashMap<>();
        seguidosMap = new HashMap<>();
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

        // LIMPIAR las estructuras de seguidores/seguidos - ESTO ES CRÍTICO
        seguidoresMap.clear();
        seguidosMap.clear();

        for (Follow f : lista) {
            followsEnMemoria.put(f.getId(), f);

            Usuario seguidor = f.getSeguidor();
            Usuario seguido = f.getSeguido();

            // AÑADIR ESTA PARTE: Actualizar las estructuras de seguidores/seguidos
            if (seguidor != null && seguido != null) {
                String seguidorNick = seguidor.getNickname();
                String seguidoNick = seguido.getNickname();

                // Actualizar seguidoresMap (quién sigue a quién)
                seguidoresMap.computeIfAbsent(seguidoNick, k -> new HashSet<>())
                        .add(seguidorNick);

                // Actualizar seguidosMap (a quién sigue cada uno)
                seguidosMap.computeIfAbsent(seguidorNick, k -> new HashSet<>())
                        .add(seguidoNick);
            }
        }

        System.out.println("✅ [MANEJADOR] Cargados " + lista.size() + " follows desde BD");
        System.out.println("✅ [MANEJADOR] " + seguidoresMap.size() + " usuarios con seguidores");
        System.out.println("✅ [MANEJADOR] " + seguidosMap.size() + " usuarios siguiendo a otros");

        // DEBUG: Mostrar algunos follows cargados
        if (!lista.isEmpty()) {
            System.out.println("📊 Ejemplo de follows cargados:");
            for (int i = 0; i < Math.min(3, lista.size()); i++) {
                Follow f = lista.get(i);
                System.out.println("   - " + f.getSeguidor().getNickname() + " → " + f.getSeguido().getNickname());
            }
        }
    }

    // ===========================================================
    // ============ NUEVOS MÉTODOS PARA MEMORIA ==================
    // ===========================================================

    /**
     * Obtiene los seguidores de un usuario desde memoria (rápido)
     */
    public Set<String> getSeguidores(String usuarioNick) {
        return seguidoresMap.getOrDefault(usuarioNick, new HashSet<>());
    }

    /**
     * Obtiene los usuarios que sigue un usuario desde memoria (rápido)
     */
    public Set<String> getSeguidos(String usuarioNick) {
        return seguidosMap.getOrDefault(usuarioNick, new HashSet<>());
    }

    /**
     * Cuenta seguidores desde memoria (rápido)
     */
    public int countSeguidores(String usuarioNick) {
        return getSeguidores(usuarioNick).size();
    }

    /**
     * Cuenta seguidos desde memoria (rápido)
     */
    public int countSeguidos(String usuarioNick) {
        return getSeguidos(usuarioNick).size();
    }

    /**
     * Verifica si un usuario sigue a otro desde memoria (rápido)
     */
    public boolean estaSiguiendo(String seguidorNick, String seguidoNick) {
        return getSeguidos(seguidorNick).contains(seguidoNick);
    }


    // ===========================================================
    // ========================== FOLLOW =========================
    // ===========================================================

    public void followUsuario(String followerNick, String targetNick, EntityManager em) {
        System.out.println("🔍 [MANEJADOR] Iniciando con EntityManager: " + (em != null ? "OK" : "NULL"));

        try {
            if (followerNick == null || targetNick == null)
                throw new IllegalArgumentException("Nick vacío");

            if (followerNick.equals(targetNick))
                throw new IllegalArgumentException("No se puede seguir a uno mismo");

            // Verificar EntityManager
            if (em == null) {
                throw new IllegalStateException("EntityManager es null");
            }
            if (!em.isOpen()) {
                throw new IllegalStateException("EntityManager está cerrado");
            }

            System.out.println("🔍 [MANEJADOR] Buscando usuarios...");

            // Buscar usando el método que ya tienes
            Usuario follower = buscarUsuarioConcreto(followerNick, em);
            Usuario target = buscarUsuarioConcreto(targetNick, em);

            if (follower == null)
                throw new IllegalArgumentException("Seguidor no encontrado: " + followerNick);
            if (target == null)
                throw new IllegalArgumentException("Usuario objetivo no encontrado: " + targetNick);

            System.out.println("✅ [MANEJADOR] Usuarios encontrados");

            // Verificar si ya existe la relación
            System.out.println("🔍 [MANEJADOR] Verificando relación existente...");
            Long count = null;
            try {
                count = em.createQuery(
                                "SELECT COUNT(f) FROM Follow f WHERE f.seguidor = :seg AND f.seguido = :tar",
                                Long.class
                        )
                        .setParameter("seg", follower)
                        .setParameter("tar", target)
                        .getSingleResult();
            } catch (Exception e) {
                System.err.println("💥 [MANEJADOR] Error en consulta COUNT: " + e.getMessage());
                count = 0L; // Asumir que no existe
            }

            if (count != null && count > 0) {
                System.out.println("ℹ️ [MANEJADOR] Ya existe relación");
                return; // No es error, simplemente ya existe
            }

            // Crear follow
            System.out.println("🆕 [MANEJADOR] Creando nueva relación...");
            Follow follow = new Follow(follower, target);

            EntityTransaction tx = em.getTransaction();
            boolean startedTransaction = false;

            try {
                // Solo comenzar transacción si no está activa
                if (!tx.isActive()) {
                    tx.begin();
                    startedTransaction = true;
                    System.out.println("💾 [MANEJADOR] Transacción iniciada");
                }

                System.out.println("💾 [MANEJADOR] Persistiendo Follow...");
                em.persist(follow);

                if (startedTransaction) {
                    tx.commit();
                    System.out.println("✅ [MANEJADOR] Transacción commitada");
                }

                followsEnMemoria.put(follow.getId(), follow);
                System.out.println("✅ [MANEJADOR] Follow persistido exitosamente");

                // ⚠️ FALTAN ESTAS LÍNEAS CRÍTICAS ⚠️
                // Actualizar las estructuras de memoria
                seguidoresMap.computeIfAbsent(targetNick, k -> new HashSet<>())
                        .add(followerNick);
                seguidosMap.computeIfAbsent(followerNick, k -> new HashSet<>())
                        .add(targetNick);

                System.out.println("✅ [MANEJADOR] Follow actualizado en BD y MEMORIA");
                System.out.println("   - Seguidores de " + targetNick + " ahora: " + seguidoresMap.get(targetNick).size());

            } catch (Exception e) {
                System.err.println("💥 [MANEJADOR] Error en transacción: " + e.getMessage());
                if (tx.isActive() && startedTransaction) {
                    try {
                        tx.rollback();
                        System.out.println("↩️ [MANEJADOR] Rollback realizado");
                    } catch (Exception rollbackEx) {
                        System.err.println("💥 [MANEJADOR] Error en rollback: " + rollbackEx.getMessage());
                    }
                }
                throw new IllegalStateException("Error al persistir Follow: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            System.err.println("💥 [MANEJADOR] ERROR: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    private Usuario buscarUsuarioConcreto(String nickname, EntityManager em) {
        // Buscar primero como Cliente
        Usuario usuario = em.find(Cliente.class, nickname);
        if (usuario == null) {
            // Si no es Cliente, buscar como Aerolinea
            usuario = em.find(Aerolinea.class, nickname);
        }
        return usuario;
    }


    // ===========================================================
    // ======================== UNFOLLOW =========================
    // ===========================================================

    public void unfollowUsuario(String followerNick, String targetNick, EntityManager em) {
        System.out.println("🔍 [MANEJADOR] Iniciando unfollow: " + followerNick + " → " + targetNick);

        try {
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

                // ✅ CRÍTICO: Actualizar las estructuras de memoria (FALTABA ESTO)
                Set<String> seguidores = seguidoresMap.get(targetNick);
                if (seguidores != null) {
                    seguidores.remove(followerNick);
                    System.out.println("   - Seguidores de " + targetNick + " después: " + seguidores.size());
                }

                Set<String> seguidos = seguidosMap.get(followerNick);
                if (seguidos != null) {
                    seguidos.remove(targetNick);
                    System.out.println("   - Seguidos por " + followerNick + " después: " + seguidos.size());
                }

                tx.commit();

                System.out.println("✅ [MANEJADOR] Unfollow completado en BD y MEMORIA");

            } catch (PersistenceException e) {
                if (tx.isActive()) tx.rollback();
                throw new IllegalStateException("Error al borrar follow: " + e.getMessage(), e);
            }

        } catch (Exception e) {
            System.err.println("💥 [MANEJADOR] ERROR en unfollow: " + e.getMessage());
            e.printStackTrace();
            throw e;
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
