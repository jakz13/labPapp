package logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona las categorías disponibles en el sistema.
 * Permite cargar, agregar y consultar categorías en memoria y en BD.
 */
public final class ManejadorCategoria {

    private Map<String, Categoria> categorias;
    private static ManejadorCategoria instancia;

    private ManejadorCategoria() {
        categorias = new HashMap<>();
    }

    public static ManejadorCategoria getInstance() {
        if (instancia == null) {
            instancia = new ManejadorCategoria();
        }
        return instancia;
    }

    // === Cargar todas las categorías desde la BD a memoria ===
    public void cargarCategoriasDesdeBD(EntityManager entManager) {
        TypedQuery<Categoria> query = entManager.createQuery("SELECT c FROM Categoria c", Categoria.class);
        List<Categoria> categoriasPersistidas = query.getResultList();
        for (Categoria c : categoriasPersistidas) {
            categorias.put(c.getNombre(), c);
        }
    }

    // === Agregar categoría en memoria y en BD ===
    public void agregarCategoria(Categoria categoria, EntityManager entManager) {
        categorias.put(categoria.getNombre(), categoria);

        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();
            entManager.persist(categoria);
            entTransaction.commit();
        } catch (Exception e) {
            if (entTransaction.isActive()) entTransaction.rollback();
            e.printStackTrace();
        }
    }

    // === Buscar categoría en memoria ===
    public Categoria buscarCategorias(String nombre) {
        return categorias.get(nombre);
    }

    // === Obtener todas las categorías ===
    public Map<String, Categoria> getCategorias() {
        return categorias;
    }

}
