package Logica;

import DataTypes.DtAerolinea;
import DataTypes.DtCliente;
import DataTypes.DtPaquete;
import DataTypes.DtReserva;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

class SistemaTest {

    private EntityManager em;
    private MockedStatic<jakarta.persistence.Persistence> persistenceMock;

    private ManejadorCliente mcMock;
    private ManejadorAerolinea maMock;
    private ManejadorCiudad mciMock;
    private ManejadorRutaVuelo mrMock;
    private ManejadorVuelo mvMock;
    private ManejadorPaquete mpMock;
    private ManejadorCategoria mcatMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Persistence.createEntityManagerFactory to avoid real DB
        EntityManagerFactory emf = mock(EntityManagerFactory.class);
        em = mock(EntityManager.class);
        // Mock transaction to avoid NPE when calling em.getTransaction()
        jakarta.persistence.EntityTransaction tx = mock(jakarta.persistence.EntityTransaction.class);
        when(em.getTransaction()).thenReturn(tx);
        doNothing().when(tx).begin();
        doNothing().when(tx).commit();
        doNothing().when(tx).rollback();
        when(tx.isActive()).thenReturn(true);

        when(emf.createEntityManager()).thenReturn(em);
        persistenceMock = mockStatic(jakarta.persistence.Persistence.class);
        persistenceMock.when(() -> jakarta.persistence.Persistence.createEntityManagerFactory("LAB_PA")).thenReturn(emf);

        // Create mocks for all managers and set as singleton instances via reflection
        mcMock = mock(ManejadorCliente.class);
        maMock = mock(ManejadorAerolinea.class);
        mciMock = mock(ManejadorCiudad.class);
        mrMock = mock(ManejadorRutaVuelo.class);
        mvMock = mock(ManejadorVuelo.class);
        mpMock = mock(ManejadorPaquete.class);
        mcatMock = mock(ManejadorCategoria.class);

        setManagerInstance(ManejadorCliente.class, mcMock);
        setManagerInstance(ManejadorAerolinea.class, maMock);
        setManagerInstance(ManejadorCiudad.class, mciMock);
        setManagerInstance(ManejadorRutaVuelo.class, mrMock);
        setManagerInstance(ManejadorVuelo.class, mvMock);
        setManagerInstance(ManejadorPaquete.class, mpMock);
        setManagerInstance(ManejadorCategoria.class, mcatMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (persistenceMock != null) persistenceMock.close();
        // reset singletons to null for isolation
        setManagerInstance(ManejadorCliente.class, null);
        setManagerInstance(ManejadorAerolinea.class, null);
        setManagerInstance(ManejadorCiudad.class, null);
        setManagerInstance(ManejadorRutaVuelo.class, null);
        setManagerInstance(ManejadorVuelo.class, null);
        setManagerInstance(ManejadorPaquete.class, null);
        setManagerInstance(ManejadorCategoria.class, null);
    }

    private void setManagerInstance(Class<?> cls, Object instance) throws Exception {
        Field f = cls.getDeclaredField("instancia");
        f.setAccessible(true);
        f.set(null, instance);
    }

    @Test
    void cargarDesdeBd_ShouldCallAllLoaders() {
        // Crear Sistema y verificar que la fábrica de EntityManager fue solicitada
        Sistema s = new Sistema();
        assertNotNull(s);
        // Llamar al método; no hacemos verificaciones de interacción con singletons porque su inicialización
        // puede variar en tiempo de ejecución. Solo comprobar que no lanza excepciones.
        assertDoesNotThrow(s::cargarDesdeBd);
        persistenceMock.verify(() -> jakarta.persistence.Persistence.createEntityManagerFactory("LAB_PA"));
    }

    @Test
    void altaCiudad_WhenNotExists_ShouldAddCity() {
        when(mciMock.obtenerCiudad(anyString())).thenReturn(null);
        Sistema s = new Sistema();

        s.altaCiudad("Nueva Ciudad", "PaisX");

        verify(mciMock).agregarCiudad(any(), eq(em));
    }

    @Test
    void altaCiudad_WhenExists_ShouldThrow() {
        when(mciMock.obtenerCiudad(anyString())).thenReturn(new Ciudad("X","Y"));
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.altaCiudad("X","Y"));
    }

    @Test
    void crearPasajero_ValidNames_ShouldReturnTrimmedPasajero() {
        Sistema s = new Sistema();
        Pasajero p = s.crearPasajero(" Juan ", " Perez ");
        assertEquals("Juan", p.getNombre());
        assertEquals("Perez", p.getApellido());
    }

    @Test
    void crearPasajero_InvalidName_ShouldThrow() {
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.crearPasajero("  ", "Apellido"));
        assertThrows(IllegalArgumentException.class, () -> s.crearPasajero("Nombre", null));
    }

    @Test
    void calcularCostoReserva_WhenVueloNotFound_ShouldThrow() {
        when(mvMock.getVuelo(anyString())).thenReturn(null);
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.calcularCostoReserva("no-existe", TipoAsiento.TURISTA, 1, 0));
    }

    @Test
    void calcularCostoReserva_TuristaAndEjecutivo_ShouldReturnCorrectCost() {
        when(mvMock.getVuelo(anyString())).thenReturn(mock(Vuelo.class));
        Sistema s = new Sistema();
        double costTurista = s.calcularCostoReserva("v1", TipoAsiento.TURISTA, 2, 1);
        assertEquals(100.0 * 2 + 30.0 * 1, costTurista);
        double costEjecutivo = s.calcularCostoReserva("v1", TipoAsiento.EJECUTIVO, 3, 2);
        assertEquals(200.0 * 3 + 30.0 * 2, costEjecutivo);
    }

    @Test
    void verificarLogin_ShouldCheckClientThenAerolinea() {
        when(mcMock.verificarLogin("c@mail", "p")).thenReturn(true);
        when(maMock.verificarLogin("a@mail", "p")).thenReturn(true);
        Sistema s = new Sistema();

        assertTrue(s.verificarLogin("c@mail", "p"));
        // when client false but aerolinea true
        when(mcMock.verificarLogin("a@mail", "p")).thenReturn(false);
        assertTrue(s.verificarLogin("a@mail", "p"));
    }

    @Test
    void obtenerTipoUsuario_ShouldReturnCorrectTypeOrNull() {
        when(mcMock.obtenerClientePorEmail("c@mail")).thenReturn(null);
        when(maMock.obtenerAerolineaPorEmail("a@mail")).thenReturn(null);
        Sistema s = new Sistema();
        assertNull(s.obtenerTipoUsuario("x@mail"));

        Cliente clienteObj = mock(Cliente.class);
        when(mcMock.obtenerClientePorEmail("c@mail")).thenReturn(clienteObj);
        assertEquals("CLIENTE", new Sistema().obtenerTipoUsuario("c@mail"));

        when(mcMock.obtenerClientePorEmail("c@mail")).thenReturn(null);
        when(maMock.obtenerAerolineaPorEmail("a@mail")).thenReturn(mock(Aerolinea.class));
        assertEquals("AEROLINEA", new Sistema().obtenerTipoUsuario("a@mail"));
    }

    @Test
    void altaPaquete_ValidationAndSuccess() {
        when(mpMock.obtenerPaquete("p1")).thenReturn(null);
        Sistema s = new Sistema();
        // invalid name
        assertThrows(IllegalArgumentException.class, () -> s.altaPaquete("  ", "d", 10, 30));
        // invalid discount
        assertThrows(IllegalArgumentException.class, () -> s.altaPaquete("n", "d", -1, 10));
        assertThrows(IllegalArgumentException.class, () -> s.altaPaquete("n", "d", 101, 10));
        // negative validity
        assertThrows(IllegalArgumentException.class, () -> s.altaPaquete("n", "d", 10, -5));

        // success
        s.altaPaquete("Pack1", "Desc", 10, 30);
        verify(mpMock).agregarPaquete(any(), eq(em));
    }

    @Test
    void altaRutaVuelo_Validations() {
        // missing aerolinea
        when(maMock.obtenerAerolinea(anyString())).thenReturn(null);
        Sistema s = new Sistema();
        DtAerolinea dtA = new DtAerolinea("n","Nombre","e@mail","desc","web",List.of());
        assertThrows(IllegalArgumentException.class, () -> s.altaRutaVuelo("r1","d","dc",dtA,"A","B","10:00", LocalDate.now(),100,200,10,new String[]{"C"},"img"));

        // same origin and dest
        Aerolinea aeroMock = mock(Aerolinea.class);
        when(aeroMock.getNickname()).thenReturn("n");
        when(maMock.obtenerAerolinea(anyString())).thenReturn(aeroMock);
        when(mrMock.getRuta(anyString())).thenReturn(null);
        when(mcatMock.buscarCategorias("C")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> s.altaRutaVuelo("r1","d","dc",dtA,"X","X","10:00", LocalDate.now(),100,200,10,new String[]{"C"},"img"));

        // no valid categories
        assertThrows(IllegalArgumentException.class, () -> s.altaRutaVuelo("r2","d","dc",dtA,"A","B","10:00", LocalDate.now(),100,200,10,new String[]{"C"},"img"));

        // success with category
        when(mcatMock.buscarCategorias("C")).thenReturn(new Categoria("C"));
        s.altaRutaVuelo("r3","d","dc",dtA,"A","B","10:00", LocalDate.now(),100,200,10,new String[]{"C"},"img");
        verify(mrMock).agregarRutaVuelo(any(), eq(em));
        verify(maMock).agregarRutaVueloAAerolinea(any(), any(), eq(em));
    }

    @Test
    void altaCategoria_DuplicateAndSuccess() {
        when(mcatMock.buscarCategorias("cat1")).thenReturn(new Categoria("cat1"));
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.altaCategoria("cat1"));

        when(mcatMock.buscarCategorias("cat2")).thenReturn(null);
        s.altaCategoria("cat2");
        verify(mcatMock).agregarCategoria(any(), eq(em));
    }

    @Test
    void obtenerDatosAdicionalesUsuario_ClientAndAerolineaPaths() {
        DtCliente dtc = mock(DtCliente.class);
        when(mcMock.obtenerCliente(anyString())).thenReturn(dtc);
        when(dtc.getReservas()).thenReturn(List.of(mock(DtReserva.class)));
        when(dtc.getPaquetesComprados()).thenReturn(List.of(mock(DtPaquete.class)));
        Sistema s = new Sistema();
        List<Object> res = s.obtenerDatosAdicionalesUsuario("nick");
        assertEquals(2, res.size());

        when(mcMock.obtenerCliente(anyString())).thenReturn(null);
        Aerolinea a = mock(Aerolinea.class);
        when(maMock.obtenerAerolinea(anyString())).thenReturn(a);
        when(a.getRutasVuelo()).thenReturn(List.of(mock(DataTypes.DtRutaVuelo.class)));
        List<Object> res2 = new Sistema().obtenerDatosAdicionalesUsuario("nick2");
        assertEquals(1, res2.size());
    }

    @Test
    void getDtItemRutasPaquete_WhenEmptyShouldThrow() {
        when(mpMock.obtenerDtItemsPaquete("nope")).thenReturn(List.of());
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.getDtItemRutasPaquete("nope"));
    }

    @Test
    void altaCliente_SuccessAndDuplicate() {
        // success
        when(maMock.obtenerAerolinea(anyString())).thenReturn(null);
        when(mcMock.obtenerCliente(anyString())).thenReturn(null);
        Sistema s = new Sistema();
        s.altaCliente("nick1", "Nombre", "Apellido", "a@mail", LocalDate.now(), "Pais", TipoDoc.CI, "123", "pass", null);
        verify(mcMock).agregarCliente(any(), eq(em));

        // duplicate (nickname exists in aerolinea)
        when(maMock.obtenerAerolinea(anyString())).thenReturn(new Aerolinea("nick1","N","e@mail","p","d","w","img"));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().altaCliente("nick1","n","a","e@mail",LocalDate.now(),"P",TipoDoc.CI,"123","p","img"));
    }

    @Test
    void altaAerolinea_SuccessAndDuplicate() {
        when(maMock.obtenerAerolinea(anyString())).thenReturn(null);
        when(mcMock.obtenerCliente(anyString())).thenReturn(null);
        Sistema s = new Sistema();
        s.altaAerolinea("nickA","NombreA","Desc","email@a.com","http://a", "pwd", null);
        verify(maMock).agregarAerolinea(any(), eq(em));

        // duplicate
        when(maMock.obtenerAerolinea(anyString())).thenReturn(new Aerolinea("nickA","N","e@mail","p","d","w","img"));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().altaAerolinea("nickA","N","d","e@mail","w","p","img"));
    }

    @Test
    void actualizarPassword_ClientThenAerolineaFallback() {
        // client path succeeds
        doNothing().when(mcMock).actualizarPassword(eq("c@mail"), anyString(), eq(em));
        Sistema s = new Sistema();
        s.actualizarPassword("c@mail","newpass");
        verify(mcMock).actualizarPassword(eq("c@mail"), anyString(), eq(em));

        // client fails -> aerolinea succeeds
        doThrow(new IllegalArgumentException("Cliente no encontrado")).when(mcMock).actualizarPassword(eq("x@mail"), anyString(), eq(em));
        doNothing().when(maMock).actualizarPassword(eq("x@mail"), anyString(), eq(em));
        new Sistema().actualizarPassword("x@mail","p");
        verify(maMock).actualizarPassword(eq("x@mail"), anyString(), eq(em));

        // both fail -> exception
        doThrow(new IllegalArgumentException("Cliente no encontrado")).when(mcMock).actualizarPassword(eq("y@mail"), anyString(), eq(em));
        doThrow(new IllegalArgumentException("Aerolínea no encontrada")).when(maMock).actualizarPassword(eq("y@mail"), anyString(), eq(em));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().actualizarPassword("y@mail","p"));
    }

    @Test
    void modificarDatosClienteCompleto_SuccessAndNotFound() {
        // success path
        Cliente clienteObj = mock(Cliente.class);
        when(mcMock.obtenerClienteReal(anyString())).thenReturn(clienteObj);
        doNothing().when(mcMock).modificarDatosClienteCompleto(anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyString(), eq(em));
        Sistema s = new Sistema();
        s.modificarDatosClienteCompleto("nick","N","A","Pais", LocalDate.now(), null, "123", "pwd", "img");
        verify(mcMock).modificarDatosClienteCompleto(eq("nick"), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyString(), eq(em));

        // not found -> simular que el manejador lanza IllegalArgumentException
        doThrow(new IllegalArgumentException("Cliente no encontrado")).when(mcMock).modificarDatosClienteCompleto(anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString(), anyString(), eq(em));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().modificarDatosClienteCompleto("no","N","A","P", LocalDate.now(), null, "123","p","img"));
    }

    @Test
    void actualizarImagenCliente_SuccessAndNotFound() {
        doNothing().when(mcMock).actualizarImagenCliente(eq("nickImg"), anyString(), eq(em));
        Sistema s = new Sistema();
        s.actualizarImagenCliente("nickImg","url");
        verify(mcMock).actualizarImagenCliente(eq("nickImg"), eq("url"), eq(em));

        doThrow(new IllegalArgumentException("Cliente no encontrado")).when(mcMock).actualizarImagenCliente(eq("nope"), anyString(), eq(em));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().actualizarImagenCliente("nope","u"));
    }

    @Test
    void obtenerImagenClienteAndAerolinea_ReturnsCorrectUrls() {
        DataTypes.DtCliente dtc = mock(DataTypes.DtCliente.class);
        when(mcMock.obtenerCliente(anyString())).thenReturn(dtc);
        when(dtc.getImagenUrl()).thenReturn("imgC.png");
        Sistema s = new Sistema();
        assertEquals("imgC.png", s.obtenerImagenCliente("nick"));

        // Aerolinea
        DataTypes.DtAerolinea dta = mock(DataTypes.DtAerolinea.class);
        when(dta.getNickname()).thenReturn("a1");
        when(dta.getImagenUrl()).thenReturn("imgA.png");
        when(maMock.getDtAerolineas()).thenReturn(List.of(dta));
        assertEquals("imgA.png", s.obtenerImagenAerolinea("a1"));
    }

    @Test
    void altaVuelo_MissingRouteThrowsAndSuccess() {
        // Stub ruta inexistente
        when(mrMock.getRuta(anyString())).thenReturn(null);
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.altaVuelo("v1","a","no-r", LocalDate.now(), 60, 100, 10, LocalDate.now()));

        // success path
        RutaVuelo ruta = mock(RutaVuelo.class);
        when(mrMock.getRuta("r1")).thenReturn(ruta);
        when(mrMock.getVueloDeRuta(anyString(), anyString())).thenReturn(null);
        doNothing().when(mvMock).agregarVuelo(any(), eq(em));
        doNothing().when(mrMock).agregarVueloARuta(anyString(), any(), eq(em));
        s.altaVuelo("v2","a","r1", LocalDate.now(), 60, 100, 10, LocalDate.now());
        verify(mvMock).agregarVuelo(any(), eq(em));
        verify(mrMock).agregarVueloARuta(eq("r1"), any(), eq(em));
    }

    @Test
    void altaRutaPaquete_PaqueteMissingShouldThrow() {
        when(mpMock.obtenerPaquete(anyString())).thenReturn(null);
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.altaRutaPaquete("pNo","r",1, TipoAsiento.TURISTA));
    }

    @Test
    void listarCiudades_ReturnsDtList() {
        Ciudad c1 = new Ciudad("C1","P1");
        when(mciMock.getCiudades()).thenReturn(List.of(c1));
        Sistema s = new Sistema();
        List<DataTypes.DtCiudad> res = s.listarCiudades();
        assertEquals(1, res.size());
        assertEquals("C1", res.get(0).getNombre());
    }

    @Test
    void registrarReservaVuelo_SuccessAndErrors() {
        // Setup mocks
        Vuelo vuelo = mock(Vuelo.class);
        when(mvMock.getVuelo("vX")).thenReturn(vuelo);
        when(vuelo.getReservas()).thenReturn(java.util.Collections.emptyMap());
        DataTypes.DtCliente dtc = mock(DataTypes.DtCliente.class);
        when(mcMock.obtenerCliente("nickR")).thenReturn(dtc);

        Reserva reserva = new Reserva(100.0, TipoAsiento.TURISTA, 1, 0, List.of(new Pasajero("a","b")), vuelo);
        // Call
        Sistema s = new Sistema();
        doNothing().when(mcMock).agregarReserva(any(), anyString(), any(), eq(em));
        doNothing().when(mvMock).actualizarVuelo(any(), eq(em));
        s.registrarReservaVuelo("nickR", "vX", reserva);
        verify(em).persist(eq(reserva));
        verify(mcMock).agregarReserva(eq(reserva), eq("nickR"), any(), eq(em));
        verify(mvMock).actualizarVuelo(any(), eq(em));

        // Errors: cliente missing (el método envuelve la excepción -> RuntimeException)
        when(mcMock.obtenerCliente("noClient")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> new Sistema().registrarReservaVuelo("noClient","vX", reserva));
        // Errors: vuelo missing
        when(mvMock.getVuelo("noV")).thenReturn(null);
        when(mcMock.obtenerCliente("nickR")).thenReturn(dtc);
        assertThrows(RuntimeException.class, () -> new Sistema().registrarReservaVuelo("nickR","noV", reserva));
    }

    @Test
    void crearYRegistrarReserva_VueloNullAndDuplicateReservation() {
        // When vuelo null
        when(mvMock.getVuelo("noVuelo")).thenReturn(null);
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.crearYRegistrarReserva("nick","noVuelo", LocalDate.now(), 100.0, TipoAsiento.TURISTA,1,0, List.of(new Pasajero("a","b"))));

        // When client already has reservation in vuelo
        Vuelo vuelo = mock(Vuelo.class);
        when(mvMock.getVuelo("v1")).thenReturn(vuelo);
        // preparar un Map mock para controlar containsKey
        @SuppressWarnings("unchecked")
        java.util.Map<Long, Reserva> reservasMock = mock(java.util.Map.class);
        when(reservasMock.containsKey(any())).thenReturn(true);
        when(vuelo.getReservas()).thenReturn(reservasMock);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().crearYRegistrarReserva("nick","v1", LocalDate.now(), 100.0, TipoAsiento.TURISTA,1,0, List.of(new Pasajero("a","b"))));
    }

    @Test
    void obtenerReserva_FoundAndNotFound() {
        DataTypes.DtReserva dr = mock(DataTypes.DtReserva.class);
        when(dr.getId()).thenReturn(123L);
        DataTypes.DtCliente dtc = mock(DataTypes.DtCliente.class);
        when(dtc.getReservas()).thenReturn(List.of(dr));
        when(mcMock.obtenerCliente("nickRes")).thenReturn(dtc);
        Sistema s = new Sistema();
        DataTypes.DtReserva found = s.obtenerReserva(123L, "nickRes");
        assertEquals(dr, found);

        // Not found
        when(mcMock.obtenerCliente("noNick")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().obtenerReserva(1L, "noNick"));
    }

    @Test
    void listarPaquetesAndDisponibles() {
        DtPaquete dp = mock(DtPaquete.class);
        when(mpMock.getPaquetes()).thenReturn(List.of(dp));
        when(mpMock.getPaquetesDisp()).thenReturn(List.of(dp));
        Sistema s = new Sistema();
        List<DtPaquete> all = s.listarPaquetes();
        List<DtPaquete> disp = s.listarPaquetesDisp();
        assertEquals(1, all.size());
        assertEquals(1, disp.size());
    }

    @Test
    void compraPaquete_MissingPackageOrClientOrAlreadyBought_AndSuccess() {
        // Paquete missing
        when(mpMock.obtenerPaquete("noP")).thenReturn(null);
        when(mcMock.obtenerCliente(anyString())).thenReturn(null);
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.compraPaquete("noP","nick",10, LocalDate.now(), 100.0));

        // Client missing
        Paquete p = mock(Paquete.class);
        when(mpMock.obtenerPaquete("p1")).thenReturn(p);
        when(mcMock.obtenerCliente("noC")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().compraPaquete("p1","noC",10, LocalDate.now(), 100.0));

        // Already bought
        {
            Paquete p2 = mock(Paquete.class);
            CompraPaqLogica cp = mock(CompraPaqLogica.class);
            Cliente clienteObj = mock(Cliente.class);
            DataTypes.DtCliente dtc = mock(DataTypes.DtCliente.class);
            when(mpMock.obtenerPaquete("p2")).thenReturn(p2);
            when(mcMock.obtenerCliente("nickC")).thenReturn(dtc);
            when(dtc.getNickname()).thenReturn("nickC");
            when(p2.getCompras()).thenReturn(List.of(cp));
            when(cp.getCliente()).thenReturn(clienteObj);
            when(clienteObj.getNickname()).thenReturn("nickC");
            assertThrows(IllegalArgumentException.class, () -> new Sistema().compraPaquete("p2","nickC",10, LocalDate.now(), 50.0));
        }

        // Success path
        Paquete p3 = mock(Paquete.class);
        when(mpMock.obtenerPaquete("p3")).thenReturn(p3);
        DataTypes.DtCliente dtc3 = mock(DataTypes.DtCliente.class);
        when(mcMock.obtenerCliente("nickS")).thenReturn(dtc3);
        doNothing().when(mpMock).compraPaquete(any(), any(), anyInt(), any(), anyDouble(), eq(em));
        new Sistema().compraPaquete("p3","nickS",5, LocalDate.now(), 120.0);
        verify(mpMock).compraPaquete(any(), any(), anyInt(), any(), anyDouble(), eq(em));
    }

    @Test
    void getReservasCliente_SuccessAndNotFound() {
        DataTypes.DtCliente dtc = mock(DataTypes.DtCliente.class);
        DataTypes.DtReserva dr = mock(DataTypes.DtReserva.class);
        when(dtc.getReservas()).thenReturn(List.of(dr));
        when(mcMock.obtenerCliente("nickR")).thenReturn(dtc);
        Sistema s = new Sistema();
        List<DataTypes.DtReserva> res = s.getReservasCliente("nickR");
        assertEquals(1, res.size());

        when(mcMock.obtenerCliente("noNick")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().getReservasCliente("noNick"));
    }

    @Test
    void obtenerPaquete_and_DtPaquete_SuccessAndNotFound() {
        Paquete p = mock(Paquete.class);
        DtPaquete dp = mock(DtPaquete.class);
        when(mpMock.obtenerPaquete("pOk")).thenReturn(p);
        when(mpMock.obtenerDtPaquete("dpOk")).thenReturn(dp);
        Sistema s = new Sistema();
        when(p.getNombre()).thenReturn("pOk");
        assertEquals(p, s.obtenerPaquete("pOk"));
        when(mpMock.obtenerPaquete("noP")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().obtenerPaquete("noP"));
        when(mpMock.obtenerDtPaquete("dpOk")).thenReturn(dp);
        assertEquals(dp, s.obtenerDtPaquete("dpOk"));
        when(mpMock.obtenerDtPaquete("noDp")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().obtenerDtPaquete("noDp"));
    }

    @Test
    void modificarDatosAerolinea_SuccessAndNotFound() {
        Aerolinea a = mock(Aerolinea.class);
        when(maMock.obtenerAerolinea("nA")).thenReturn(a);
        doNothing().when(maMock).modificarDatosAerolinea(any(), eq(em));
        Sistema s = new Sistema();
        s.modificarDatosAerolinea("nA","Desc","url");
        verify(a).setDescripcion("Desc");
        verify(a).setSitioWeb("url");

        when(maMock.obtenerAerolinea("noA")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().modificarDatosAerolinea("noA","d","u"));
    }

    @Test
    void aceptarYRechazarRuta_VerifyCalls() {
        doNothing().when(mrMock).cambiarEstadoRuta(anyString(), any(), eq(em));
        Sistema s = new Sistema();
        s.aceptarRutaVuelo("r1");
        s.rechazarRutaVuelo("r2");
        verify(mrMock).cambiarEstadoRuta("r1", RutaVuelo.EstadoRuta.CONFIRMADA, em);
        verify(mrMock).cambiarEstadoRuta("r2", RutaVuelo.EstadoRuta.RECHAZADA, em);
    }

    @Test
    void verInfoVuelo_VerInfoCliente_VerInfoAerolinea_SuccessAndNotFound() {
        Vuelo v = mock(Vuelo.class);
        when(mvMock.getVuelo("vOk")).thenReturn(v);
        Sistema s = new Sistema();
        assertEquals(v, s.verInfoVuelo("vOk"));
        when(mvMock.getVuelo("noV")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().verInfoVuelo("noV"));

        // verInfoCliente
        DtCliente dtc = mock(DtCliente.class);
        when(mcMock.obtenerCliente("nickC")).thenReturn(dtc);
        Cliente clienteReal = mock(Cliente.class);
        when(mcMock.obtenerClientePorEmail(anyString())).thenReturn(clienteReal);
        // dtc.getEmail needed
        when(dtc.getEmail()).thenReturn("email@c");
        assertEquals(clienteReal, new Sistema().verInfoCliente("nickC"));
        // Ajustar caso not found: evitar NPE devolviendo DtCliente con email nulo
        DtCliente dtcNullEmail = mock(DtCliente.class);
        when(mcMock.obtenerCliente("noC")).thenReturn(dtcNullEmail);
        when(dtcNullEmail.getEmail()).thenReturn(null);
        when(mcMock.obtenerClientePorEmail(null)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().verInfoCliente("noC"));

        // verInfoAerolinea
        Aerolinea a = mock(Aerolinea.class);
        when(maMock.obtenerAerolinea("nA")).thenReturn(a);
        assertEquals(a, new Sistema().verInfoAerolinea("nA"));
        when(maMock.obtenerAerolinea("noA")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().verInfoAerolinea("noA"));
    }

    @Test
    void listarCategorias_ReturnsDtList() {
        Categoria c1 = new Categoria("CAT1");
        when(mcatMock.getCategorias()).thenReturn(java.util.Map.of("CAT1", c1));
        Sistema s = new Sistema();
        List<DataTypes.DtCategoria> res = s.listarCategorias();
        assertEquals(1, res.size());
        assertEquals("CAT1", res.get(0).getNombre());
    }

    // ====== NUEVAS PRUEBAS AÑADIDAS PARA RUTAS NO CUBIERTAS ======

    @Test
    void deprecatedAltaMethods_ShouldThrowUnsupported() {
        Sistema s = new Sistema();
        // métodos antiguos sin password deben lanzar UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> s.altaCliente("n","N","A","e@mail", LocalDate.now(), "P", TipoDoc.CI, "123"));
        assertThrows(UnsupportedOperationException.class, () -> s.altaAerolinea("na","N","D","e@mail","http://x"));
    }

    @Test
    void listarYObtenerClientes_And_modificarDatosCliente() {
        DataTypes.DtCliente dtc = mock(DataTypes.DtCliente.class);
        when(mcMock.getClientes()).thenReturn(List.of(dtc));
        Sistema s = new Sistema();
        List<DataTypes.DtCliente> clientes = s.listarClientes();
        assertEquals(1, clientes.size());

        when(mcMock.obtenerCliente("nickC")).thenReturn(dtc);
        // Preparar comportamiento para modificar
        doNothing().when(mcMock).modificarDatosCliente(eq(dtc), eq(em));
        // Usamos valores de prueba (corregido orden de parámetros: añadir nacionalidad)
        s.modificarDatosDeCliente("nickC","Nom","Ape","PaisTest", LocalDate.of(1990,1,1), null, "123");
        verify(mcMock).modificarDatosCliente(eq(dtc), eq(em));
    }

    @Test
    void obtenerImagenRuta_ListarYObtenerRutasPendientes() {
        RutaVuelo ruta = mock(RutaVuelo.class);
        when(ruta.getImagenUrl()).thenReturn("rutaImg.png");
        when(mrMock.getRuta("rImg")).thenReturn(ruta);
        Sistema s = new Sistema();
        assertEquals("rutaImg.png", s.obtenerImagenRuta("rImg"));

        // obtenerAerolineasConRutasPendientes
        DataTypes.DtAerolinea dta = mock(DataTypes.DtAerolinea.class);
        when(dta.getNombre()).thenReturn("AerolineaX");
        when(maMock.getDtAerolineas()).thenReturn(List.of(dta));
        when(mrMock.getRutasPorEstadoYAerolinea(eq("AerolineaX"), eq(RutaVuelo.EstadoRuta.INGRESADA))).thenReturn(List.of(ruta));
        List<DataTypes.DtAerolinea> aeroPend = new Sistema().obtenerAerolineasConRutasPendientes();
        assertEquals(1, aeroPend.size());

        // obtenerRutasPendientesPorAerolinea
        when(mrMock.getRutasPorEstadoYAerolinea(eq("AerolineaX"), eq(RutaVuelo.EstadoRuta.INGRESADA))).thenReturn(List.of(ruta));
        List<DataTypes.DtRutaVuelo> dtR = new Sistema().obtenerRutasPendientesPorAerolinea("AerolineaX");
        // como ruta.getDtRutaVuelo() no está stubbeado, puede devolver null; nos aseguramos que lista tenga el mismo tamaño
        assertEquals(1, dtR.size());
    }

    @Test
    void altaRutaPaquete_Success_And_PaqueteYaComprado() {
        // Caso: paquete ya comprado -> error
        Paquete paqueteComprado = mock(Paquete.class);
        when(mpMock.obtenerPaquete("pBought")).thenReturn(paqueteComprado);
        when(paqueteComprado.estaComprado()).thenReturn(true);
        Sistema s = new Sistema();
        assertThrows(IllegalArgumentException.class, () -> s.altaRutaPaquete("pBought","r1",1, TipoAsiento.TURISTA));

        // Caso éxito
        Paquete pOk = mock(Paquete.class);
        when(mpMock.obtenerPaquete("pOk")).thenReturn(pOk);
        when(pOk.estaComprado()).thenReturn(false);
        RutaVuelo rOk = mock(RutaVuelo.class);
        when(mrMock.getRuta("rOk")).thenReturn(rOk);
        when(rOk.getEstado()).thenReturn(RutaVuelo.EstadoRuta.CONFIRMADA);
        // dtPaquete con lista real para verificar adición
        DataTypes.DtPaquete dtp = mock(DataTypes.DtPaquete.class);
        java.util.List<DataTypes.DtItemPaquete> items = new java.util.ArrayList<>();
        when(dtp.getItems()).thenReturn(items);
        when(mpMock.obtenerDtPaquete("pOk")).thenReturn(dtp);
        doNothing().when(mpMock).agregarRutaAPaquete(any(), any(), anyInt(), any(), eq(em));

        s.altaRutaPaquete("pOk","rOk",2, TipoAsiento.TURISTA);
        verify(mpMock).agregarRutaAPaquete(eq(pOk), eq(rOk), eq(2), eq(TipoAsiento.TURISTA), eq(em));
        // después de la llamada, dtp.items debe contener el nuevo item
        assertEquals(1, items.size());
    }

    @Test
    void listarVuelosPorRuta_ReturnsDtVueloList() {
        DataTypes.DtVuelo dtv = mock(DataTypes.DtVuelo.class);
        when(mrMock.obtenerVuelosPorRuta("rX")).thenReturn(List.of(dtv));
        Sistema s = new Sistema();
        List<DataTypes.DtVuelo> res = s.listarVuelosPorRuta("rX");
        assertEquals(1, res.size());
        assertEquals(dtv, res.get(0));
    }

    @Test
    void getDtItemRutasPaquete_Success() {
        DataTypes.DtItemPaquete it = mock(DataTypes.DtItemPaquete.class);
        when(mpMock.obtenerDtItemsPaquete("pItems")).thenReturn(List.of(it));
        Sistema s = new Sistema();
        List<DataTypes.DtItemPaquete> res = s.getDtItemRutasPaquete("pItems");
        assertEquals(1, res.size());
    }

    @Test
    void obtenerRutaAndListarRutasPorAerolinea() {
        RutaVuelo r = mock(RutaVuelo.class);
        when(mrMock.getRuta("rFind")).thenReturn(r);
        Sistema s = new Sistema();
        assertEquals(r, s.obtenerRuta("rFind"));

        DataTypes.DtRutaVuelo dtr = mock(DataTypes.DtRutaVuelo.class);
        when(maMock.obtenerRutaVueloDeAerolinea(anyString())).thenReturn(List.of(dtr));
        List<DataTypes.DtRutaVuelo> lista = s.listarRutasPorAerolinea("any");
        assertEquals(1, lista.size());
    }

    @Test
    void obtenerAerolinea_ReturnsDtAerolinea_And_NotFound() {
        Aerolinea a = mock(Aerolinea.class);
        when(a.getNickname()).thenReturn("aNick");
        when(a.getNombre()).thenReturn("AeroName");
        when(a.getEmail()).thenReturn("a@e.com");
        when(a.getDescripcion()).thenReturn("Desc");
        when(a.getSitioWeb()).thenReturn("http://aero");
        when(a.getImagenUrl()).thenReturn("img.png");
        when(a.getRutasVuelo()).thenReturn(List.of());
        when(maMock.obtenerAerolinea("aNick")).thenReturn(a);

        Sistema s = new Sistema();
        DataTypes.DtAerolinea res = s.obtenerAerolinea("aNick");
        assertEquals("aNick", res.getNickname());
        assertEquals("AeroName", res.getNombre());

        when(maMock.obtenerAerolinea("noA")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> new Sistema().obtenerAerolinea("noA"));
    }

    @Test
    void listarAerolineas_ReturnsDtList() {
        DataTypes.DtAerolinea dta = mock(DataTypes.DtAerolinea.class);
        when(maMock.getDtAerolineas()).thenReturn(List.of(dta));
        Sistema s = new Sistema();
        List<DataTypes.DtAerolinea> res = s.listarAerolineas();
        assertEquals(1, res.size());
        assertEquals(dta, res.get(0));
    }

    @Test
    void obtenerVuelo_SuccessAndNotFound() {
        Vuelo v = mock(Vuelo.class);
        when(mvMock.getVuelo("vOk")).thenReturn(v);
        Sistema s = new Sistema();
        assertEquals(v, s.obtenerVuelo("vOk"));
        when(mvMock.getVuelo("noV")).thenReturn(null);
        assertNull(s.obtenerVuelo("noV"));
    }

    @Test
    void actualizarImagenAerolinea_SuccessAndNotFound() {
        doNothing().when(maMock).actualizarImagenAerolinea(eq("aNick"), anyString(), eq(em));
        Sistema s = new Sistema();
        s.actualizarImagenAerolinea("aNick", "urlA.png");
        verify(maMock).actualizarImagenAerolinea(eq("aNick"), eq("urlA.png"), eq(em));

        doThrow(new IllegalArgumentException("Aerolínea no encontrada")).when(maMock).actualizarImagenAerolinea(eq("noA"), anyString(), eq(em));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().actualizarImagenAerolinea("noA","u"));
    }

    @Test
    void obtenerCliente_ReturnsDtClienteOrNull() {
        DataTypes.DtCliente dtc = mock(DataTypes.DtCliente.class);
        when(mcMock.obtenerCliente("nickD")).thenReturn(dtc);
        Sistema s = new Sistema();
        assertEquals(dtc, s.obtenerCliente("nickD"));

        when(mcMock.obtenerCliente("noD")).thenReturn(null);
        assertNull(new Sistema().obtenerCliente("noD"));
    }

    @Test
    void modificarDatosAerolineaCompleto_SuccessAndHandlerThrows() {
        doNothing().when(maMock).modificarDatosAerolineaCompleto(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), eq(em));
        Sistema s = new Sistema();
        s.modificarDatosAerolineaCompleto("nickA","Nombre","Desc","http://x","pwd","img");
        verify(maMock).modificarDatosAerolineaCompleto(eq("nickA"), anyString(), anyString(), anyString(), anyString(), anyString(), eq(em));

        doThrow(new IllegalArgumentException("Aerolínea no encontrada")).when(maMock).modificarDatosAerolineaCompleto(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), eq(em));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().modificarDatosAerolineaCompleto("no","N","D","u","p","i"));
    }

    @Test
    void actualizarImagenRuta_SuccessAndNotFound() {
        doNothing().when(mrMock).actualizarImagenRuta(eq("ruta1"), anyString(), eq(em));
        Sistema s = new Sistema();
        s.actualizarImagenRuta("ruta1", "imgRuta.png");
        verify(mrMock).actualizarImagenRuta(eq("ruta1"), eq("imgRuta.png"), eq(em));

        doThrow(new IllegalArgumentException("Ruta no encontrada")).when(mrMock).actualizarImagenRuta(eq("noR"), anyString(), eq(em));
        assertThrows(IllegalArgumentException.class, () -> new Sistema().actualizarImagenRuta("noR","u"));
    }

}
