package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorAerolineaTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field f = ManejadorAerolinea.class.getDeclaredField("instancia");
        f.setAccessible(true);
        f.set(null, null);
    }

    @Test
    void getInstance() {
        ManejadorAerolinea a = ManejadorAerolinea.getInstance();
        ManejadorAerolinea b = ManejadorAerolinea.getInstance();
        assertNotNull(a);
        assertSame(a, b);
    }

    @Test
    void cargarAerolineasDesdeBD() {
        EntityManager em = mock(EntityManager.class);
        @SuppressWarnings("unchecked")
        TypedQuery<Aerolinea> query = mock(TypedQuery.class);
        when(em.createQuery("SELECT a FROM Aerolinea a", Aerolinea.class)).thenReturn(query);

        Aerolinea a1 = mock(Aerolinea.class);
        Aerolinea a2 = mock(Aerolinea.class);
        when(a1.getNickname()).thenReturn("nick-db-1");
        when(a2.getNickname()).thenReturn("nick-db-2");
        when(query.getResultList()).thenReturn(List.of(a1, a2));

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.cargarAerolineasDesdeBD(em);

        assertSame(a1, manejador.obtenerAerolinea("nick-db-1"));
        assertSame(a2, manejador.obtenerAerolinea("nick-db-2"));
    }

    @Test
    void agregarAerolinea() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-add");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        assertSame(aerolinea, manejador.obtenerAerolinea("nick-add"));
        verify(tx).begin();
        verify(em).persist(aerolinea);
        verify(tx).commit();
    }

    @Test
    void agregarRutaVueloAAerolinea() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-ruta");
        RutaVuelo ruta = mock(RutaVuelo.class);

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        reset(em, tx);
        when(em.getTransaction()).thenReturn(tx);

        manejador.agregarRutaVueloAAerolinea("nick-ruta", ruta, em);

        verify(aerolinea).agregarRutaVuelo(ruta);
        verify(tx).begin();
        verify(em).merge(aerolinea);
        verify(tx).commit();
    }

    @Test
    void agregarRutaVueloAAerolinea_noIniciaTransaccionSiNoExiste() {
        EntityManager em = mock(EntityManager.class);

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarRutaVueloAAerolinea("no-existe", mock(RutaVuelo.class), em);

        verify(em, never()).getTransaction();
        verify(em, never()).merge(any());
    }

    @Test
    void obtenerAerolinea() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-one");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        assertSame(aerolinea, manejador.obtenerAerolinea("nick-one"));
        assertNull(manejador.obtenerAerolinea("desconocido"));
    }

    @Test
    void obtenerAerolineaPorEmail() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea a1 = mock(Aerolinea.class);
        when(a1.getNickname()).thenReturn("n1");
        when(a1.getEmail()).thenReturn("a1@mail.com");

        Aerolinea a2 = mock(Aerolinea.class);
        when(a2.getNickname()).thenReturn("n2");
        when(a2.getEmail()).thenReturn("a2@mail.com");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(a1, em);
        manejador.agregarAerolinea(a2, em);

        assertSame(a2, manejador.obtenerAerolineaPorEmail("a2@mail.com"));
        assertNull(manejador.obtenerAerolineaPorEmail("no@mail.com"));
    }

    @Test
    void verificarLogin() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-login");
        when(aerolinea.getEmail()).thenReturn("login@mail.com");
        when(aerolinea.verificarPassword("ok")).thenReturn(true);
        when(aerolinea.verificarPassword("bad")).thenReturn(false);

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        assertTrue(manejador.verificarLogin("login@mail.com", "ok"));
        assertFalse(manejador.verificarLogin("login@mail.com", "bad"));
        assertFalse(manejador.verificarLogin("desconocido@mail.com", "ok"));
    }

    @Test
    void obtenerRutaVueloDeAerolinea() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-routes");
        List<DataTypes.DtRutaVuelo> rutas = List.of(mock(DataTypes.DtRutaVuelo.class));
        when(aerolinea.getDtRutasVuelo()).thenReturn(rutas);

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        List<DataTypes.DtRutaVuelo> resultOk = manejador.obtenerRutaVueloDeAerolinea("nick-routes");
        List<DataTypes.DtRutaVuelo> resultEmpty = manejador.obtenerRutaVueloDeAerolinea("no-existe");

        assertEquals(1, resultOk.size());
        assertTrue(resultEmpty.isEmpty());
    }

    @Test
    void getDtAerolineas() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-dto");
        when(aerolinea.getNombre()).thenReturn("Nombre");
        when(aerolinea.getEmail()).thenReturn("dto@mail.com");
        when(aerolinea.getDescripcion()).thenReturn("Desc");
        when(aerolinea.getSitioWeb()).thenReturn("web");
        when(aerolinea.getDtRutasVuelo()).thenReturn(List.of());

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        List<DataTypes.DtAerolinea> dts = manejador.getDtAerolineas();
        assertFalse(dts.isEmpty());
        assertTrue(dts.stream().anyMatch(dt -> "nick-dto".equals(dt.getNickname()) && "dto@mail.com".equals(dt.getEmail())));
    }

    @Test
    void actualizarPassword() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-pass");
        when(aerolinea.getEmail()).thenReturn("pass@mail.com");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        reset(em, tx);
        when(em.getTransaction()).thenReturn(tx);

        manejador.actualizarPassword("pass@mail.com", "nueva", em);

        verify(aerolinea).setPassword("nueva");
        verify(tx).begin();
        verify(em).merge(aerolinea);
        verify(tx).commit();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> manejador.actualizarPassword("no@mail.com", "x", em));
        assertTrue(ex.getMessage().contains("Aerolínea no encontrada"));
    }

    @Test
    void modificarDatosAerolineaCompleto() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-full");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        reset(em, tx);
        when(em.getTransaction()).thenReturn(tx);

        manejador.modificarDatosAerolineaCompleto("nick-full", " Nombre ", " Desc ", " web ", " pass ", " img ", em);

        verify(aerolinea).setNombre("Nombre");
        verify(aerolinea).setDescripcion("Desc");
        verify(aerolinea).setSitioWeb("web");
        verify(aerolinea).setPassword("pass");
        verify(aerolinea).setImagenUrl("img");
        verify(tx).begin();
        verify(em).merge(aerolinea);
        verify(tx).commit();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                manejador.modificarDatosAerolineaCompleto("no-nick", "n", "d", "w", "p", "i", em));
        assertTrue(ex.getMessage().contains("Aerolínea no encontrada"));
    }


    @Test
    void modificarDatosAerolineaCompleto_noCambiaPasswordSiEsVaciaEImagenNull() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-trim");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        reset(em, tx);
        when(em.getTransaction()).thenReturn(tx);

        manejador.modificarDatosAerolineaCompleto("nick-trim",
                " n ", " d ", " w ", "   ", "   ", em);

        verify(aerolinea, never()).setPassword(anyString());
        verify(aerolinea).setImagenUrl(null);
        verify(em).merge(aerolinea);
    }

    @Test
    void actualizarImagenAerolinea() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);
        when(aerolinea.getNickname()).thenReturn("nick-img");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(aerolinea, em);

        reset(em, tx);
        when(em.getTransaction()).thenReturn(tx);

        manejador.actualizarImagenAerolinea("nick-img", "img.jpg", em);

        verify(aerolinea).setImagenUrl("img.jpg");
        verify(tx).begin();
        verify(em).merge(aerolinea);
        verify(tx).commit();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> manejador.actualizarImagenAerolinea("no-nick", "img.jpg", em));
        assertTrue(ex.getMessage().contains("Aerolínea no encontrada"));
    }

    @Test
    void modificarDatosAerolinea() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea aerolinea = mock(Aerolinea.class);

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.modificarDatosAerolinea(aerolinea, em);

        verify(tx).begin();
        verify(em).merge(aerolinea);
        verify(tx).commit();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> manejador.modificarDatosAerolinea(null, em));
        assertTrue(ex.getMessage().contains("Aerolínea no encontrada"));
    }

    @Test
    void getAerolineas() {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        Aerolinea a1 = mock(Aerolinea.class);
        when(a1.getNickname()).thenReturn("l1");
        Aerolinea a2 = mock(Aerolinea.class);
        when(a2.getNickname()).thenReturn("l2");

        ManejadorAerolinea manejador = ManejadorAerolinea.getInstance();
        manejador.agregarAerolinea(a1, em);
        manejador.agregarAerolinea(a2, em);

        List<Aerolinea> list = manejador.getAerolineas();
        assertEquals(2, list.size());
        assertTrue(list.contains(a1));
        assertTrue(list.contains(a2));
    }
}