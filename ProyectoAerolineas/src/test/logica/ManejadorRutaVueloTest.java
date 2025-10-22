package logica;

import DataTypes.DtVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorRutaVueloTest {

    private EntityManager em;
    private EntityTransaction tx;

    @BeforeEach
    void setUp() throws Exception {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        doNothing().when(tx).begin();
        doNothing().when(tx).commit();
        doNothing().when(tx).rollback();
        when(tx.isActive()).thenReturn(true);

        // reset singleton
        setManagerInstance(ManejadorRutaVuelo.class, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        setManagerInstance(ManejadorRutaVuelo.class, null);
    }

    private void setManagerInstance(Class<?> cls, Object instance) throws Exception {
        Field f = cls.getDeclaredField("instancia");
        f.setAccessible(true);
        f.set(null, instance);
    }

    @Test
    void cargarRutasDesdeBD_ShouldLoadAndInitEstado() {
        ManejadorRutaVuelo mr = ManejadorRutaVuelo.getInstance();
        Aerolinea aero = new Aerolinea("a1","Aero","a@mail","pwd","d","w",null);
        RutaVuelo ruta = new RutaVuelo("RBD","d","dc",aero,"O","D","10:00", LocalDate.now(),100,200,10,List.of(), null);
        // forzar estado null para probar inicialización
        ruta.setEstado(null);

        @SuppressWarnings("unchecked")
        TypedQuery<RutaVuelo> query = mock(TypedQuery.class);
        when(em.createQuery("SELECT r FROM RutaVuelo r", RutaVuelo.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(ruta));

        mr.cargarRutasDesdeBD(em);

        RutaVuelo loaded = mr.getRuta("RBD");
        assertNotNull(loaded);
        assertEquals(RutaVuelo.EstadoRuta.INGRESADA, loaded.getEstado());
    }

    @Test
    void agregarRutaVuelo_PersistAndRollbackOnException() {
        ManejadorRutaVuelo mr = ManejadorRutaVuelo.getInstance();
        Aerolinea aero = new Aerolinea("a2","Aero2","a2@mail","pwd","d","w",null);
        RutaVuelo ruta = new RutaVuelo("R1","d","dc",aero,"O","D","09:00", LocalDate.now(),50,80,5,List.of(), null);

        mr.agregarRutaVuelo(ruta, em);
        verify(em).persist(ruta);
        assertEquals(ruta, mr.getRuta("R1"));

        // simular excepción en persist para ruta2
        RutaVuelo ruta2 = new RutaVuelo("R2","d2","dc2",aero,"O2","D2","11:00", LocalDate.now(),60,90,5,List.of(), null);
        doThrow(new PersistenceException()).when(em).persist(any(RutaVuelo.class));
        mr.agregarRutaVuelo(ruta2, em);
        // se intentó rollback
        verify(tx, atLeastOnce()).rollback();
    }

    @Test
    void agregarVueloARuta_SuccessAndRouteNotFound() {
        ManejadorRutaVuelo mr = ManejadorRutaVuelo.getInstance();
        Aerolinea aero = new Aerolinea("a3","Aero3","a3@mail","pwd","d","w",null);
        RutaVuelo ruta = new RutaVuelo("RR","d","dc",aero,"O","D","08:00", LocalDate.now(),70,120,8,List.of(), null);
        mr.agregarRutaVuelo(ruta, em);

        Vuelo vuelo = new Vuelo("V1", aero.getNombre(), ruta, LocalDate.now(), 120, 100, 20, LocalDate.now());
        mr.agregarVueloARuta("RR", vuelo, em);
        // merge called and vuelo agregado a la ruta
        verify(em, atLeastOnce()).merge(ruta);
        assertNotNull(ruta.getVuelo("V1"));

        // route not found path (no exception expected)
        mr.agregarVueloARuta("NO_EXISTE", vuelo, em);
    }

    @Test
    void getRutasPorAerolineaAndEstadoAndChangeState() {
        ManejadorRutaVuelo mr = ManejadorRutaVuelo.getInstance();
        Aerolinea aeroA = new Aerolinea("a4","Aero4","a4@mail","pwd","d","w",null);
        RutaVuelo r1 = new RutaVuelo("RA","d","dc",aeroA,"O","D","07:00", LocalDate.now(),10,20,1,List.of(), null);
        RutaVuelo r2 = new RutaVuelo("RB","d","dc",aeroA,"O","D","07:00", LocalDate.now(),10,20,1,List.of(), null);
        r2.setEstado(RutaVuelo.EstadoRuta.CONFIRMADA);
        mr.agregarRutaVuelo(r1, em);
        mr.agregarRutaVuelo(r2, em);

        List<RutaVuelo> todas = mr.getRutasPorAerolinea(aeroA.getNombre());
        assertTrue(todas.size() >= 2);

        List<RutaVuelo> filtradas = mr.getRutasPorEstadoYAerolinea(aeroA.getNombre(), RutaVuelo.EstadoRuta.CONFIRMADA);
        assertEquals(1, filtradas.size());

        // cambiar estado success
        mr.cambiarEstadoRuta("RA", RutaVuelo.EstadoRuta.RECHAZADA, em);
        assertEquals(RutaVuelo.EstadoRuta.RECHAZADA, mr.getRuta("RA").getEstado());

        // cambiar estado ruta inexistente -> lanza IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> mr.cambiarEstadoRuta("NO_R", RutaVuelo.EstadoRuta.CONFIRMADA, em));
    }

    @Test
    void actualizarImagenRutaAndObtenerVuelosPorRuta() {
        ManejadorRutaVuelo mr = ManejadorRutaVuelo.getInstance();
        Aerolinea aero = new Aerolinea("a5","Aero5","a5@mail","pwd","d","w",null);
        RutaVuelo ruta = new RutaVuelo("RI","d","dc",aero,"O","D","06:00", LocalDate.now(),30,60,5,List.of(), null);
        mr.agregarRutaVuelo(ruta, em);

        // actualizar imagen
        mr.actualizarImagenRuta("RI", "img.png", em);
        verify(em, atLeastOnce()).merge(ruta);
        assertEquals("img.png", ruta.getImagenUrl());

        // actualizar imagen ruta inexistente -> exception
        assertThrows(IllegalArgumentException.class, () -> mr.actualizarImagenRuta("NOPE", "x", em));

        // agregar un vuelo y obtener dt vuelos
        Vuelo v = new Vuelo("V2", aero.getNombre(), ruta, LocalDate.now(), 60, 50, 10, LocalDate.now());
        ruta.agregarVuelo(v);
        List<DtVuelo> dtV = mr.obtenerVuelosPorRuta("RI");
        assertEquals(1, dtV.size());
    }

}