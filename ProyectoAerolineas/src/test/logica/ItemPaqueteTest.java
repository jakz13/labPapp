package logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemPaqueteTest {

    private RutaVuelo rutaTurista;
    private RutaVuelo rutaEjecutivo;

    @BeforeEach
    void setUp() {
        // Creamos rutas de ejemplo con costos diferentes para cada tipo de asiento
        rutaTurista = new RutaVuelo();
        rutaTurista.setCostoTurista(500.0);
        rutaTurista.setCostoEjecutivo(1000.0);

        rutaEjecutivo = new RutaVuelo();
        rutaEjecutivo.setCostoTurista(300.0);
        rutaEjecutivo.setCostoEjecutivo(800.0);
    }

    // --- Test constructor y getters ---
    @Test
    void constructorYGettersDeberianRetornarValoresCorrectos() {
        ItemPaquete item = new ItemPaquete(rutaTurista, 2, TipoAsiento.TURISTA);

        assertEquals(rutaTurista, item.getRutaVuelo(), "La ruta de vuelo debe coincidir con la asignada");
        assertEquals(2, item.getCantAsientos(), "La cantidad de asientos debe coincidir con la asignada");
        assertEquals(TipoAsiento.TURISTA, item.getTipoAsiento(), "El tipo de asiento debe coincidir con el asignado");
    }

    // --- Test incrementar cantidad ---
    @Test
    void incrementarCantidadDeberiaSumarAsientos() {
        ItemPaquete item = new ItemPaquete(rutaTurista, 2, TipoAsiento.TURISTA);
        item.incrementarCantidad(3);

        assertEquals(5, item.getCantAsientos(), "La cantidad de asientos debe incrementarse correctamente");
    }

    // --- Test calcular costo para turista ---
    @Test
    void calcularCostoItemDeberiaRetornarCostoCorrectoTurista() {
        ItemPaquete item = new ItemPaquete(rutaTurista, 2, TipoAsiento.TURISTA);
        double costo = item.calcularCostoItem();

        assertEquals(1000.0, costo, "El costo debe ser cantidad * costo turista de la ruta");
    }

    // --- Test calcular costo para ejecutivo ---
    @Test
    void calcularCostoItemDeberiaRetornarCostoCorrectoEjecutivo() {
        ItemPaquete item = new ItemPaquete(rutaEjecutivo, 3, TipoAsiento.EJECUTIVO);
        double costo = item.calcularCostoItem();

        assertEquals(2400.0, costo, "El costo debe ser cantidad * costo ejecutivo de la ruta");
    }

    // --- Test constructor lanza excepción si la ruta de vuelo es null ---
    @Test
    void constructorDeberiaLanzarExcepcionSiRutaVueloEsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemPaquete(null, 2, TipoAsiento.TURISTA),
                "Debe lanzar IllegalArgumentException si la ruta de vuelo es null");
    }

    // --- Test constructor lanza excepción si la cantidad de asientos es menor o igual a cero ---
    @Test
    void constructorDeberiaLanzarExcepcionSiCantidadAsientosNoEsValida() {
        RutaVuelo ruta = new RutaVuelo();
        ruta.setCostoTurista(500);
        ruta.setCostoEjecutivo(1000);

        assertThrows(IllegalArgumentException.class,
                () -> new ItemPaquete(ruta, 0, TipoAsiento.TURISTA),
                "Debe lanzar IllegalArgumentException si la cantidad de asientos es 0");

        assertThrows(IllegalArgumentException.class,
                () -> new ItemPaquete(ruta, -3, TipoAsiento.EJECUTIVO),
                "Debe lanzar IllegalArgumentException si la cantidad de asientos es negativa");
    }

    // --- Test constructor lanza excepción si el tipo de asiento es null ---
    @Test
    void constructorDeberiaLanzarExcepcionSiTipoAsientoEsNull() {
        RutaVuelo ruta = new RutaVuelo();
        ruta.setCostoTurista(500);
        ruta.setCostoEjecutivo(1000);

        assertThrows(IllegalArgumentException.class,
                () -> new ItemPaquete(ruta, 2, null),
                "Debe lanzar IllegalArgumentException si el tipo de asiento es null");
    }

}
