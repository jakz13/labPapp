package logica;

import DataTypes.DtCliente;
import DataTypes.DtItemPaquete;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ManejadorPaqueteTest {

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
        // reset singletons to ensure fresh state
        setManagerInstance(ManejadorPaquete.class, null);
        setManagerInstance(ManejadorCliente.class, null);
    }

    @AfterEach
    void tearDown() throws Exception {
        // limpiar singletons
        setManagerInstance(ManejadorPaquete.class, null);
        setManagerInstance(ManejadorCliente.class, null);
    }

    private void setManagerInstance(Class<?> cls, Object instance) throws Exception {
        Field f = cls.getDeclaredField("instancia");
        f.setAccessible(true);
        f.set(null, instance);
    }

    @Test
    void agregarPaquete_SuccessAndDuplicate() {
        ManejadorPaquete mp = ManejadorPaquete.getInstance();
        Paquete p = new Paquete("Pack1", "Desc", 10, 30);

        // éxito
        mp.agregarPaquete(p, em);
        verify(em).persist(p);
        assertEquals(p, mp.obtenerPaquete("Pack1"));

        // duplicado -> excepción
        assertThrows(IllegalArgumentException.class, () -> mp.agregarPaquete(p, em));
    }

    @Test
    void agregarRutaAPaquete_SuccessAndDuplicate() {
        ManejadorPaquete mp = ManejadorPaquete.getInstance();

        Aerolinea aero = new Aerolinea("a1", "Aero", "a@mail", "pwd", "desc", "web", null);
        RutaVuelo ruta = new RutaVuelo("R1", "descR", "dc", aero, "ORIG", "DEST", "10:00", LocalDate.now(), 100.0, 200.0, 30.0, List.of(), null);

        Paquete paquete = new Paquete("PackR", "Desc", 20, 10);
        // caso éxito: paquete sin items
        mp.agregarPaquete(paquete, em);
        mp.agregarRutaAPaquete(paquete, ruta, 2, TipoAsiento.TURISTA, em);
        // se mergea
        verify(em, atLeastOnce()).merge(paquete);
        assertEquals(1, paquete.getItemPaquetes().size());

        // caso duplicado: agregar misma ruta y tipo provoca excepción
        Paquete paquete2 = new Paquete("PackR2", "Desc2", 0, 5);
        ItemPaquete existing = new ItemPaquete(ruta, 1, TipoAsiento.TURISTA);
        paquete2.getItemPaquetes().add(existing);
        mp.agregarPaquete(paquete2, em);
        assertThrows(IllegalArgumentException.class, () -> mp.agregarRutaAPaquete(paquete2, ruta, 1, TipoAsiento.TURISTA, em));
    }

    @Test
    void compraPaquete_SuccessAndClientMissing() throws Exception {
        ManejadorPaquete mp = ManejadorPaquete.getInstance();
        Paquete p = new Paquete("PackC", "DescC", 0, 30);
        mp.agregarPaquete(p, em);

        // preparar manejador cliente mock
        ManejadorCliente mcMock = mock(ManejadorCliente.class);
        Cliente clienteObj = new Cliente("nick1", "Nom", "Ape", "c@mail", LocalDate.now(), "Pais", TipoDoc.CEDULAIDENTIDAD, "123", "pwd", null);
        when(mcMock.obtenerClienteReal("nick1")).thenReturn(clienteObj);
        setManagerInstance(ManejadorCliente.class, mcMock);

        DtCliente dt = new DtCliente("nick1", "Nom", "c@mail", null, "Ape", LocalDate.now(), "Pais", TipoDoc.CEDULAIDENTIDAD, "123", LocalDate.now(), List.of(), List.of());

        mp.compraPaquete(p, dt, 10, LocalDate.now(), 500.0, em);
        // persist compra y merge paquete/cliente
        verify(em).persist(any(CompraPaqLogica.class));
        verify(em, atLeastOnce()).merge(p);
        verify(em, atLeastOnce()).merge(clienteObj);

        // cliente inexistente -> excepción
        when(mcMock.obtenerClienteReal("nope")).thenReturn(null);
        DtCliente dt2 = new DtCliente("nope", "X", "x@mail", null, "A", LocalDate.now(), "P", TipoDoc.CEDULAIDENTIDAD, "999", LocalDate.now(), List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> mp.compraPaquete(p, dt2, 5, LocalDate.now(), 100.0, em));
    }

    @Test
    void cargarPaquetesDesdeBD_ShouldLoadPaquetes() {
        ManejadorPaquete mp = ManejadorPaquete.getInstance();

        Paquete p = new Paquete("PackBD", "DescBD", 0, 7);
        @SuppressWarnings("unchecked")
        TypedQuery<Paquete> query = mock(TypedQuery.class);
        when(em.createQuery("SELECT p FROM Paquete p", Paquete.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(p));

        mp.cargarPaquetesDesdeBD(em);
        assertEquals(p, mp.obtenerPaquete("PackBD"));
    }

    @Test
    void getPaquetes_getPaquetesDisp_obtenerDtItemsPaquete() {
        ManejadorPaquete mp = ManejadorPaquete.getInstance();

        Paquete p1 = new Paquete("P1", "D1", 0, 10);
        Paquete p2 = new Paquete("P2", "D2", 10, 5);
        mp.agregarPaquete(p1, em);
        mp.agregarPaquete(p2, em);

        // agregar item a p1
        Aerolinea aero = new Aerolinea("a2", "Aero2", "a2@mail", "pwd", "desc", "web", null);
        RutaVuelo ruta = new RutaVuelo("R2", "descR2", "dc2", aero, "OR", "DE", "09:00", LocalDate.now(), 50.0, 80.0, 10.0, List.of(), null);
        ItemPaquete ip = new ItemPaquete(ruta, 3, TipoAsiento.EJECUTIVO);
        p1.getItemPaquetes().add(ip);

        // marcar p2 como comprado
        Cliente clienteObj = new Cliente("nick2", "N", "A", "c2@mail", LocalDate.now(), "P", TipoDoc.CEDULAIDENTIDAD, "111", "pwd", null);
        CompraPaqLogica compra = new CompraPaqLogica(clienteObj, p2, LocalDate.now(), 5, 200.0);
        p2.getCompras().add(compra);

        List<DataTypes.DtPaquete> todos = mp.getPaquetes();
        List<DataTypes.DtPaquete> disp = mp.getPaquetesDisp();
        assertEquals(2, todos.size());
        assertEquals(1, disp.size());

        List<DtItemPaquete> items = mp.obtenerDtItemsPaquete("P1");
        assertEquals(1, items.size());
        assertEquals(3, items.get(0).getCantAsientos());
    }

}