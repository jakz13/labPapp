package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorCiudadTest {

    private ManejadorCiudad manejador;
    private EntityManager em;
    private EntityTransaction tx;

    @BeforeEach
    void setUp() {
        manejador = ManejadorCiudad.getInstance();
        manejador.resetForTests(); // limpia el map interno
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);
    }

    @Test
    void agregarCiudadDeberiaAgregarCiudadEnMemoriaYEnBD() {
        Ciudad ciudad = new Ciudad("Montevideo", "Uruguay");

        manejador.agregarCiudad(ciudad, em);

        // Verifica que la ciudad esté en memoria
        assertEquals(ciudad, manejador.obtenerCiudad("Montevideo"));

        // Verifica que persist haya sido llamado
        verify(tx).begin();
        verify(em).persist(ciudad);
        verify(tx).commit();
    }

    @Test
    void agregarCiudadDuplicadaDeberiaLanzarExcepcion() {
        // Usamos un nombre raro para no chocar con otras ciudades ya agregadas
        Ciudad ciudad = new Ciudad("MontevideoTestDuplicada", "Uruguay");
        manejador.agregarCiudad(ciudad, em);

        Ciudad ciudadDuplicada = new Ciudad("MontevideoTestDuplicada", "Uruguay");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                manejador.agregarCiudad(ciudadDuplicada, em)
        );
        assertTrue(e.getMessage().contains("ya existe"));
    }


    @Test
    void agregarCiudadDeberiaHacerRollbackSiFallaPersist() {
        Ciudad ciudad = new Ciudad("Salto", "Uruguay");

        // Simula que persist lanza excepción
        doThrow(new RuntimeException()).when(em).persist(any(Ciudad.class));

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                manejador.agregarCiudad(ciudad, em)
        );

        verify(tx).begin();
        verify(tx).rollback();
    }
/*
    @Test
    void cargarCiudadesDesdeBDDeberiaCargarTodas() {
        Ciudad c1 = new Ciudad("Maldonado", "Uruguay");
        Ciudad c2 = new Ciudad("Canelones", "Uruguay");

        TypedQuery<Ciudad> query = mock(TypedQuery.class);
        when(em.createQuery("SELECT c FROM Ciudad c", Ciudad.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(c1, c2));

        manejador.cargarCiudadesDesdeBD(em);

        assertEquals(c1, manejador.obtenerCiudad("Maldonado"));
        assertEquals(c2, manejador.obtenerCiudad("Canelones"));
    }
*/
    @Test
    void getCiudadesDeberiaDevolverLista() {
        Ciudad c1 = new Ciudad("Artigas", "Uruguay");
        Ciudad c2 = new Ciudad("Rocha", "Uruguay");

        manejador.agregarCiudad(c1, em);
        manejador.agregarCiudad(c2, em);

        List<Ciudad> lista = manejador.getCiudades();
        assertTrue(lista.contains(c1));
        assertTrue(lista.contains(c2));
        assertEquals(2, lista.size());
    }
}
