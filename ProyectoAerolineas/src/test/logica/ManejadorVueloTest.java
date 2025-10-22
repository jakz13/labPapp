package logica;

import DataTypes.DtVuelo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorVueloTest {

    private ManejadorVuelo mgr;

    @BeforeEach
    void setUp() {
        mgr = ManejadorVuelo.getInstance();
        mgr.limpiarCache();
    }

    @Test
    void agregaYObtieneVuelo() {
        Vuelo v = mock(Vuelo.class);
        when(v.getNombre()).thenReturn("V1");
        when(v.getDtVuelo()).thenReturn(new DtVuelo("V1","Aero", LocalDate.now(), 0,0,0, LocalDate.now(), null, Collections.emptyList()));

        EntityManager em = mock(EntityManager.class);
        mgr.agregarVuelo(v, em);

        assertTrue(mgr.existeVuelo("V1"));
        assertSame(v, mgr.getVuelo("V1"));
        List<Vuelo> list = mgr.getVuelos();
        assertEquals(1, list.size());
    }

    @Test
    void agregarConTransaccionHaceCommit() {
        Vuelo v = mock(Vuelo.class);
        when(v.getNombre()).thenReturn("Vtx");
        when(v.getDtVuelo()).thenReturn(new DtVuelo("Vtx","X", LocalDate.now(), 0,0,0, LocalDate.now(), null, Collections.emptyList()));

        EntityManager em = mock(EntityManager.class);
        var tx = mock(jakarta.persistence.EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        mgr.agregarVueloConTransaccion(v, em);

        verify(tx).begin();
        verify(em).persist(v);
        verify(tx).commit();
        assertTrue(mgr.existeVuelo("Vtx"));
    }

    @Test
    void agregarConTransaccionRollbackOnException() {
        // Crear un vuelo mock
        Vuelo v = mock(Vuelo.class);
        when(v.getNombre()).thenReturn("VX2");

        // Mockear EntityManager y EntityTransaction
        EntityManager em = mock(EntityManager.class);
        EntityTransaction tx = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        when(tx.isActive()).thenReturn(true);

        // Simular que persist lanza excepción
        //doThrow(new RuntimeException("boom")).when(em).persist(v);
        //doThrow(new PersistenceException()).when(em).persist(any(Vuelo.class));
        doThrow(new PersistenceException("boom")).when(em).persist(any(Vuelo.class));

        // Ejecutar y verificar que lanza la RuntimeException con el mensaje esperado
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> mgr.agregarVueloConTransaccion(v, em));

        assertTrue(ex.getMessage().contains("Error al agregar el vuelo"),
                "El mensaje de la excepción debe contener 'Error al agregar el vuelo'");

        // Verificar transacción: begin y rollback
        verify(tx).begin();
        verify(tx).rollback();

        // Verificar que no quedó el vuelo en el mapa
        assertFalse(mgr.existeVuelo("VX2"),
                "El vuelo no debe existir en el mapa si hubo rollback");
    }



    @Test
    void obtieneVuelosPorAerolineaYPorRuta() {
        Vuelo v1 = mock(Vuelo.class);
        when(v1.getNombre()).thenReturn("VA1");
        when(v1.getNombreAerolinea()).thenReturn("A1");
        RutaVuelo r1 = mock(RutaVuelo.class);
        when(r1.getNombre()).thenReturn("R1");
        when(v1.getRutaVuelo()).thenReturn(r1);

        Vuelo v2 = mock(Vuelo.class);
        when(v2.getNombre()).thenReturn("VA2");
        when(v2.getNombreAerolinea()).thenReturn("A2");
        when(v2.getRutaVuelo()).thenReturn(null);

        EntityManager em = mock(EntityManager.class);
        mgr.agregarVuelo(v1, em);
        mgr.agregarVuelo(v2, em);

        List<Vuelo> resA1 = mgr.getVuelosPorAerolinea("A1");
        assertEquals(1, resA1.size());
        assertEquals("VA1", resA1.get(0).getNombre());

        List<Vuelo> resR1 = mgr.getVuelosPorRuta("R1");
        assertEquals(1, resR1.size());
        assertEquals("VA1", resR1.get(0).getNombre());

        List<Vuelo> resNo = mgr.getVuelosPorAerolinea("NOPE");
        assertTrue(resNo.isEmpty());
    }

    @Test
    void tieneReservaDeClienteFunciona() {
        Vuelo v = mock(Vuelo.class);
        when(v.getNombre()).thenReturn("Vreg");
        // Mockear un Map<Long, Reserva> y stubear containsKey para aceptar la búsqueda por nickname
        @SuppressWarnings("unchecked")
        Map<Long, Reserva> reservasMock = mock(Map.class);
        when(reservasMock.containsKey("nick1")).thenReturn(true);
        when(reservasMock.containsKey("otro")).thenReturn(false);
        when(v.getReservas()).thenReturn((Map) reservasMock);

        EntityManager em = mock(EntityManager.class);
        mgr.agregarVuelo(v, em);

        assertTrue(mgr.tieneReservaDeCliente("nick1", v));
        assertFalse(mgr.tieneReservaDeCliente("otro", v));
    }

    @Test
    void getDtVuelosRetornaListaDeDt() {
        Vuelo v = mock(Vuelo.class);
        when(v.getNombre()).thenReturn("Vdt");
        DtVuelo dt = new DtVuelo("Vdt","A", LocalDate.of(2025,1,1), 0,0,0, LocalDate.of(2025,1,1), null, Collections.emptyList());
        when(v.getDtVuelo()).thenReturn(dt);

        EntityManager em = mock(EntityManager.class);
        mgr.agregarVuelo(v, em);

        List<DtVuelo> dts = mgr.getDtVuelos();
        assertEquals(1, dts.size());
        assertEquals("Vdt", dts.get(0).getNombre());
    }

    @Test
    void cargarVuelosDesdeBDSinResultadosNoFalla() {
        EntityManager em = mock(EntityManager.class);
        TypedQuery<Vuelo> q = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Vuelo.class))).thenReturn(q);
        when(q.getResultList()).thenReturn(Collections.emptyList());

        mgr.cargarVuelosDesdeBD(em);

        assertTrue(mgr.getVuelos().isEmpty());
    }

    @Test
    void eliminarVueloConTransaccionElimina() {
        Vuelo v = mock(Vuelo.class);
        when(v.getNombre()).thenReturn("Vdel");
        EntityManager em = mock(EntityManager.class);
        var tx = mock(jakarta.persistence.EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);

        mgr.agregarVuelo(v, em);
        assertTrue(mgr.existeVuelo("Vdel"));

        mgr.eliminarVuelo("Vdel", em);
        verify(tx).begin();
        verify(em).remove(v);
        verify(tx).commit();
        assertFalse(mgr.existeVuelo("Vdel"));
    }

    @Test
    void limpiarCacheVacia() {
        mgr.limpiarCache();
        assertTrue(mgr.getVuelos().isEmpty());
    }
}
