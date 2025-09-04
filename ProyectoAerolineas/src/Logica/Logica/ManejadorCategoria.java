package Logica;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManejadorCategoria {
    private Map<String, Categoria> categorias;
    public static ManejadorCategoria instancia;


    private ManejadorCategoria() {
        categorias = new HashMap<>();
    }


    public static ManejadorCategoria getInstance() {
        if (instancia == null) {
            instancia = new ManejadorCategoria();
        }
        return instancia;
    }

/*
    public void cargarCategoriasDesdeBD(EntityManager em) {
        List<Categoria> categoriasPersistidas = em.createQuery("SELECT c FROM Categoria c", Categoria.class)
                .getResultList();
        for (Categoria c : categoriasPersistidas) {
            categorias.put(c.getNombre(), c);
        }
    }

*/

    public void agregarCategoria(Categoria categoria)  {
        categorias.put(categoria.getNombre(), categoria);
    }
    public Categoria buscarCategorias(String nombre) {
        return categorias.get(nombre);
    }

}
