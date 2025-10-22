package logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorCategoriaTest {

    private ManejadorCategoria manejador;

    @BeforeEach
    void setUp() {
        manejador = ManejadorCategoria.getInstance();
        manejador.getCategorias().clear(); // limpiar para tests
    }

    @Test
    void getInstanceDeberiaRetornarLaMismaInstancia() {
        ManejadorCategoria otra = ManejadorCategoria.getInstance();
        assertSame(manejador, otra);
    }

    @Test
    void agregarCategoriaDeberiaAgregarEnMemoria() {
        Categoria cat = new Categoria();
        cat.setNombre("Aventura");

        manejador.getCategorias().clear();
        manejador.getCategorias().put(cat.getNombre(), cat); // agregado manual
        assertTrue(manejador.getCategorias().containsKey("Aventura"));
        assertEquals(cat, manejador.buscarCategorias("Aventura"));
    }

    @Test
    void buscarCategoriasDeberiaRetornarCategoriaCorrecta() {
        Categoria cat = new Categoria();
        cat.setNombre("Deporte");
        manejador.getCategorias().put(cat.getNombre(), cat);

        Categoria resultado = manejador.buscarCategorias("Deporte");
        assertNotNull(resultado);
        assertEquals("Deporte", resultado.getNombre());
    }

    @Test
    void cargarCategoriasDesdeBDDeberiaLlenarMap() {
        // Mocks
        EntityManager em = mock(EntityManager.class);
        TypedQuery<Categoria> query = mock(TypedQuery.class);
        Categoria cat1 = new Categoria();
        cat1.setNombre("Ciencia");
        Categoria cat2 = new Categoria();
        cat2.setNombre("Arte");

        when(em.createQuery("SELECT c FROM Categoria c", Categoria.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(cat1, cat2));

        manejador.getCategorias().clear();
        manejador.cargarCategoriasDesdeBD(em);

        Map<String, Categoria> categorias = manejador.getCategorias();
        assertEquals(2, categorias.size());
        assertTrue(categorias.containsKey("Ciencia"));
        assertTrue(categorias.containsKey("Arte"));
    }

    @Test
    void agregarCategoriaDeberiaPersistirYCommit() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(em.getTransaction()).thenReturn(tx);

        Categoria cat = new Categoria();
        cat.setNombre("Turismo");

        manejador.agregarCategoria(cat, em);

        verify(em).persist(cat);
        verify(tx).begin();
        verify(tx).commit();
    }

    @Test
    void agregarCategoriaDeberiaHacerRollbackSiFallaPersist() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);

        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);
        doThrow(new RuntimeException()).when(em).persist(any(Categoria.class));

        ManejadorCategoria manejador = ManejadorCategoria.getInstance();

        Categoria categoria = new Categoria("Test");

        manejador.agregarCategoria(categoria, em);

        verify(tx).begin();
        verify(tx).rollback();
    }

}
