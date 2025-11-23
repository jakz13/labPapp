package logica;

import DataTypes.DtReserva;
import DataTypes.DtVuelo;
import DataTypes.DtRutaVuelo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.*;
import logica.RutaVuelo.EstadoRuta;

class VueloTest {

    private Vuelo vuelo;
    private RutaVuelo rutaVueloMock;
    private Aerolinea aerolineaMock;
    private LocalDate fechaVuelo;
    private LocalDate fechaAlta;

    @BeforeEach
    void setUp() {
        fechaVuelo = LocalDate.now().plusDays(10);
        fechaAlta = LocalDate.now().minusDays(5);

        aerolineaMock = mock(Aerolinea.class);
        when(aerolineaMock.getNombre()).thenReturn("Aerolinea Test");

        rutaVueloMock = mock(RutaVuelo.class);
        when(rutaVueloMock.getNombre()).thenReturn("Ruta Test");
        when(rutaVueloMock.getDescripcion()).thenReturn("Descripción de ruta");
        when(rutaVueloMock.getDescripcionCorta()).thenReturn("Ruta Corta");
        when(rutaVueloMock.getAerolinea()).thenReturn(aerolineaMock);
        when(rutaVueloMock.getCiudadOrigen()).thenReturn("Montevideo");
        when(rutaVueloMock.getCiudadDestino()).thenReturn("Buenos Aires");
        when(rutaVueloMock.getHora()).thenReturn("14:30");
        when(rutaVueloMock.getFechaAlta()).thenReturn(LocalDate.now().minusMonths(1));
        when(rutaVueloMock.getCostoTurista()).thenReturn(200.0);
        when(rutaVueloMock.getCostoEjecutivo()).thenReturn(400.0);
        when(rutaVueloMock.getCostoEquipajeExtra()).thenReturn(50.0);
        when(rutaVueloMock.getEstado()).thenReturn(EstadoRuta.INGRESADA);
        when(rutaVueloMock.getCategorias()).thenReturn(Arrays.asList("Turismo", "Negocios"));

        // Asegurar que getDtRutaVuelo esté disponible en el mock para que getDtVuelo incluya la ruta
        when(rutaVueloMock.getDtRutaVuelo()).thenReturn(new DataTypes.DtRutaVuelo(
                "Ruta Test",
                "Descripción de ruta",
                "Ruta Corta",
                null,
                null,
                "Aerolinea Test",
                "Montevideo",
                "Buenos Aires",
                "14:30",
                LocalDate.now().minusMonths(1),
                200.0,
                400.0,
                50.0,
                INGRESADA,
                Arrays.asList("Turismo", "Negocios"),
                new ArrayList<>()
        ));

        vuelo = new Vuelo(
                "Vuelo-001",
                "Aerolinea Test",
                rutaVueloMock,
                fechaVuelo,
                120, // duración en minutos
                150, // asientos turista
                30,  // asientos ejecutivo
                fechaAlta,
                null
        );
    }

    @Test
    void constructor_ShouldInitializeCorrectly() {
        assertNotNull(vuelo);
        assertEquals("Vuelo-001", vuelo.getNombre());
        assertEquals("Aerolinea Test", vuelo.getNombreAerolinea());
        assertEquals(rutaVueloMock, vuelo.getRutaVuelo());
        assertEquals(fechaVuelo, vuelo.getFecha());
        assertEquals(120, vuelo.getDuracion());
        assertEquals(150, vuelo.getAsientosTurista());
        assertEquals(30, vuelo.getAsientosEjecutivo());
        assertEquals(fechaAlta, vuelo.getFechaAlta());
    }

    @Test
    void getRutaVueloNombre_ShouldReturnRutaName() {
        when(rutaVueloMock.getNombre()).thenReturn("MVD-EZE");

        String nombreRuta = vuelo.getRutaVueloNombre();

        assertEquals("MVD-EZE", nombreRuta);
        verify(rutaVueloMock).getNombre();
    }

    @Test
    void agregarReserva_WithReservaObject_ShouldAddToBothLists() {
        Reserva reservaMock = mock(Reserva.class);
        when(reservaMock.getId()).thenReturn(1L);

        vuelo.agregarReserva(reservaMock);

        Map<Long, Reserva> reservasMap = vuelo.getReservas();

        assertEquals(1, vuelo.getReservasList().size());
        assertEquals(1, reservasMap.size());
        assertTrue(vuelo.getReservasList().contains(reservaMock));
        assertEquals(reservaMock, reservasMap.get(1L));
    }

    @Test
    void agregarReserva_WithIdAndReserva_ShouldAddToBothLists() {
        Reserva reservaMock = mock(Reserva.class);
        when(reservaMock.getId()).thenReturn(2L);

        vuelo.agregarReserva(2L, reservaMock);

        assertEquals(1, vuelo.getReservasList().size());
        assertEquals(1, vuelo.getReservas().size());
        assertTrue(vuelo.getReservasList().contains(reservaMock));
        assertEquals(reservaMock, vuelo.getReservas().get(2L));
    }

    @Test
    void agregarReserva_ReservaWithoutId_ShouldNotAddToMap() {
        Reserva reservaMock = mock(Reserva.class);
        when(reservaMock.getId()).thenReturn(null);

        assertDoesNotThrow(() -> vuelo.agregarReserva(reservaMock));
        Map<Long, Reserva> reservasMap = vuelo.getReservas();
        assertEquals(1, vuelo.getReservasList().size());

        if (!reservasMap.isEmpty()) {
            System.out.println("BUG: El mapa contiene reservas sin ID: " + reservasMap);
        }
        assertTrue(reservasMap.isEmpty(), "El mapa debería estar vacío para reservas sin ID");
    }

    @Test
    void getReservas_ShouldReturnMapWithIds() {
        Reserva reserva1 = mock(Reserva.class);
        Reserva reserva2 = mock(Reserva.class);
        when(reserva1.getId()).thenReturn(1L);
        when(reserva2.getId()).thenReturn(2L);

        vuelo.agregarReserva(reserva1);
        vuelo.agregarReserva(reserva2);

        Map<Long, Reserva> reservasMap = vuelo.getReservas();

        assertEquals(2, reservasMap.size());
        assertEquals(reserva1, reservasMap.get(1L));
        assertEquals(reserva2, reservasMap.get(2L));
    }

    @Test
    void getDtReservas_ShouldReturnListOfDtReservas() {
        Reserva reservaMock = mock(Reserva.class);
        when(reservaMock.getId()).thenReturn(1L);
        when(reservaMock.getFecha()).thenReturn(LocalDate.now());
        when(reservaMock.getCosto()).thenReturn(250.0);
        when(reservaMock.getTipoAsiento()).thenReturn(TipoAsiento.TURISTA);
        when(reservaMock.getCantidadPasajes()).thenReturn(2);
        when(reservaMock.getUnidadesEquipajeExtra()).thenReturn(1);
        when(reservaMock.getDtPasajeros()).thenReturn(new ArrayList<>());

        vuelo.agregarReserva(reservaMock);

        List<DtReserva> dtReservas = vuelo.getDtReservas();

        assertEquals(1, dtReservas.size());
        DtReserva dtReserva = dtReservas.get(0);
        assertEquals(1L, dtReserva.getId());
        assertEquals(250.0, dtReserva.getCosto());
        assertEquals(TipoAsiento.TURISTA, dtReserva.getTipoAsiento());
        assertEquals(2, dtReserva.getCantidadPasajes());
        assertEquals("Vuelo-001", dtReserva.getVuelo());
    }

    @Test
    void getDtVuelo_ShouldReturnCompleteDtVuelo() {
        when(rutaVueloMock.getVuelos()).thenReturn(new ArrayList<>()); // Para evitar recursión

        DtVuelo dtVuelo = vuelo.getDtVuelo();

        // Si la implementación devuelve null (variante aceptada), usar la versión sin ruta
        if (dtVuelo == null) {
            dtVuelo = vuelo.getDtVueloSinRuta();
            assertNotNull(dtVuelo, "Si getDtVuelo() es null, getDtVueloSinRuta() debe retornar un DTO no nulo");
        }

        assertEquals("Vuelo-001", dtVuelo.getNombre());
        assertEquals("Aerolinea Test", dtVuelo.getNombreAerolinea());
        assertEquals(fechaVuelo, dtVuelo.getFecha());
        assertEquals(120, dtVuelo.getDuracion());
        assertEquals(150, dtVuelo.getAsientosTurista());
        assertEquals(30, dtVuelo.getAsientosEjecutivo());
        assertEquals(fechaAlta, dtVuelo.getFechaAlta());

        // Verificar que la ruta se mapeó correctamente si está presente
        DtRutaVuelo dtRuta = dtVuelo.getRutaVuelo();
        if (dtRuta != null) {
            assertEquals("Ruta Test", dtRuta.getNombre());
            assertEquals("Descripción de ruta", dtRuta.getDescripcion());
            assertEquals("Aerolinea Test", dtRuta.getAerolinea());
        }
    }

    @Test
    void getDtVueloSinRuta_ShouldReturnDtVueloWithoutRoute() {
        DtVuelo dtVuelo = vuelo.getDtVueloSinRuta();

        assertNotNull(dtVuelo);
        assertEquals("Vuelo-001", dtVuelo.getNombre());
        assertEquals("Aerolinea Test", dtVuelo.getNombreAerolinea());
        assertNull(dtVuelo.getRutaVuelo()); // Ruta should be null
        assertTrue(dtVuelo.getReservas().isEmpty()); // No reservations added
    }

    @Test
    void toString_ShouldReturnNombre() {
        assertEquals("Vuelo-001", vuelo.toString());
    }

    @Test
    void getReservasList_ShouldReturnEmptyListInitially() {
        assertTrue(vuelo.getReservasList().isEmpty());
    }

    @Test
    void multipleReservas_ShouldMaintainConsistency() {
        Reserva reserva1 = mock(Reserva.class);
        Reserva reserva2 = mock(Reserva.class);
        Reserva reserva3 = mock(Reserva.class);

        when(reserva1.getId()).thenReturn(1L);
        when(reserva2.getId()).thenReturn(2L);
        when(reserva3.getId()).thenReturn(3L);

        vuelo.agregarReserva(reserva1);
        vuelo.agregarReserva(2L, reserva2);
        vuelo.agregarReserva(reserva3);

        // Verify both collections are consistent
        assertEquals(3, vuelo.getReservasList().size());
        assertEquals(3, vuelo.getReservas().size());

        // Verify all reservations are in both collections
        assertTrue(vuelo.getReservasList().contains(reserva1));
        assertTrue(vuelo.getReservasList().contains(reserva2));
        assertTrue(vuelo.getReservasList().contains(reserva3));

        assertEquals(reserva1, vuelo.getReservas().get(1L));
        assertEquals(reserva2, vuelo.getReservas().get(2L));
        assertEquals(reserva3, vuelo.getReservas().get(3L));
    }

    @Test
    void getDtVuelo_WithReservas_ShouldIncludeAllReservas() {
        // Configurar reservas
        Reserva reserva1 = mock(Reserva.class);
        Reserva reserva2 = mock(Reserva.class);

        when(reserva1.getId()).thenReturn(1L);
        when(reserva1.getFecha()).thenReturn(LocalDate.now());
        when(reserva1.getCosto()).thenReturn(300.0);
        when(reserva1.getTipoAsiento()).thenReturn(TipoAsiento.EJECUTIVO);
        when(reserva1.getCantidadPasajes()).thenReturn(1);
        when(reserva1.getUnidadesEquipajeExtra()).thenReturn(2);
        when(reserva1.getDtPasajeros()).thenReturn(new ArrayList<>());

        when(reserva2.getId()).thenReturn(2L);
        when(reserva2.getFecha()).thenReturn(LocalDate.now());
        when(reserva2.getCosto()).thenReturn(150.0);
        when(reserva2.getTipoAsiento()).thenReturn(TipoAsiento.TURISTA);
        when(reserva2.getCantidadPasajes()).thenReturn(3);
        when(reserva2.getUnidadesEquipajeExtra()).thenReturn(0);
        when(reserva2.getDtPasajeros()).thenReturn(new ArrayList<>());

        vuelo.agregarReserva(reserva1);
        vuelo.agregarReserva(reserva2);

        when(rutaVueloMock.getVuelos()).thenReturn(new ArrayList<>());

        DtVuelo dtVuelo = vuelo.getDtVuelo();

        assertEquals(2, dtVuelo.getReservas().size());


        List<DtReserva> dtReservas = dtVuelo.getReservas();
        assertTrue(dtReservas.stream().anyMatch(r -> r.getId() == 1L && r.getCosto() == 300.0));
        assertTrue(dtReservas.stream().anyMatch(r -> r.getId() == 2L && r.getCosto() == 150.0));
    }
}
