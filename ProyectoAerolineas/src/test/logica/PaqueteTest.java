package logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

class PaqueteTest {

    private Paquete paquete;
    private LocalDate fechaActual;

    @BeforeEach
    void configurar() {
        fechaActual = LocalDate.now();
        paquete = new Paquete("Paquete Test", "Descripción del paquete", 10, 30);
    }

    @Test
    void constructor_DeberiaInicializarCorrectamente() {
        assertEquals("Paquete Test", paquete.getNombre());
        assertEquals("Descripción del paquete", paquete.getDescripcion());
        assertEquals(10, paquete.getDescuentoPorc());
        assertEquals(30, paquete.getPeriodoValidezDias());
        assertEquals(fechaActual, paquete.getFechaAlta());
        assertEquals(0.0, paquete.getCosto());
        assertTrue(paquete.getItemPaquetes().isEmpty());
        assertTrue(paquete.getCompras().isEmpty());
    }

    @Test
    void establecerCosto_DeberiaEstablecerCostoCorrectamente() {
        // En lugar de setCosto, prueba el cálculo real
        ItemPaquete item = mock(ItemPaquete.class);
        RutaVuelo ruta = mock(RutaVuelo.class);

        when(item.getTipoAsiento()).thenReturn(TipoAsiento.TURISTA);
        when(item.getRutaVuelo()).thenReturn(ruta);
        when(item.getCantAsientos()).thenReturn(2);
        when(ruta.getCostoTurista()).thenReturn(750.25); // 750.25 * 2 = 1500.50

        paquete.getItemPaquetes().add(item);

        // Ahora getCosto() calculará 1500.50 * 0.9 = 1350.45
        double costoCalculado = paquete.getCosto();
        assertEquals(1350.45, costoCalculado, 0.001);
    }

    @Test
    void obtenerCosto_SinItems_DeberiaRetornarCero() {
        double costo = paquete.getCosto();
        assertEquals(0.0, costo);
    }

    @Test
    void obtenerCosto_ConItems_DeberiaCalcularCorrectamente() {
        // Configurar items mock
        ItemPaquete item1 = mock(ItemPaquete.class);
        ItemPaquete item2 = mock(ItemPaquete.class);

        RutaVuelo ruta1 = mock(RutaVuelo.class);
        RutaVuelo ruta2 = mock(RutaVuelo.class);

        when(item1.getTipoAsiento()).thenReturn(TipoAsiento.TURISTA);
        when(item2.getTipoAsiento()).thenReturn(TipoAsiento.EJECUTIVO);
        when(item1.getRutaVuelo()).thenReturn(ruta1);
        when(item2.getRutaVuelo()).thenReturn(ruta2);
        when(item1.getCantAsientos()).thenReturn(2);
        when(item2.getCantAsientos()).thenReturn(1);
        when(ruta1.getCostoTurista()).thenReturn(200.0);
        when(ruta2.getCostoEjecutivo()).thenReturn(500.0);

        paquete.getItemPaquetes().add(item1);
        paquete.getItemPaquetes().add(item2);

        // Cálculo esperado: (2*200 + 1*500) * (1 - 0.10) = (400 + 500) * 0.90 = 810.0
        double costoEsperado = 810.0;
        double costoCalculado = paquete.getCosto();

        assertEquals(costoEsperado, costoCalculado, 0.001);
    }

    @Test
    void obtenerDescuento_ConDescuento_DeberiaRetornarStringFormateado() {
        assertEquals("Descuento: 10%", paquete.getDescuento());

        Paquete paqueteSinDescuento = new Paquete("Sin Desc", "Desc", 0, 15);
        assertEquals("Sin descuento", paqueteSinDescuento.getDescuento());
    }

    @Test
    void obtenerPeriodoValidez_ConPeriodoValido_DeberiaRetornarStringFormateado() {
        assertEquals("Periodo de validez: 30 días", paquete.getPeriodoValidez());

        Paquete paqueteSinValidez = new Paquete("Sin Validez", "Desc", 5, 0);
        assertEquals("Sin periodo de validez", paqueteSinValidez.getPeriodoValidez());
    }

    @Test
    void estaComprado_SinCompras_DeberiaRetornarFalso() {
        assertFalse(paquete.estaComprado());
    }

    @Test
    void estaComprado_ConCompras_DeberiaRetornarVerdadero() {
        CompraPaqLogica compra = mock(CompraPaqLogica.class);
        paquete.getCompras().add(compra);

        assertTrue(paquete.estaComprado());
    }

    @Test
    void calcularCostoReservaPaquete_SinItems_DeberiaRetornarCero() {
        double costo = paquete.calcularCostoReservaPaquete();

        assertEquals(0.0, costo, 0.001);
        assertEquals(0.0, paquete.getCosto(), 0.001);
    }

    @Test
    void calcularCostoReservaPaquete_ConItems_DeberiaCalcularYActualizarCosto() {
        // Configurar items mock COMPLETAMENTE
        ItemPaquete item1 = mock(ItemPaquete.class);
        ItemPaquete item2 = mock(ItemPaquete.class);

        RutaVuelo ruta1 = mock(RutaVuelo.class);
        RutaVuelo ruta2 = mock(RutaVuelo.class);

        // Configurar las rutas para los items
        when(item1.getRutaVuelo()).thenReturn(ruta1);
        when(item2.getRutaVuelo()).thenReturn(ruta2);

        // Configurar costos de las rutas
        when(ruta1.getCostoTurista()).thenReturn(200.0);
        when(ruta1.getCostoEjecutivo()).thenReturn(400.0);
        when(ruta2.getCostoTurista()).thenReturn(300.0);
        when(ruta2.getCostoEjecutivo()).thenReturn(600.0);

        // Configurar tipos de asiento y cantidades
        when(item1.getTipoAsiento()).thenReturn(TipoAsiento.TURISTA);
        when(item2.getTipoAsiento()).thenReturn(TipoAsiento.EJECUTIVO);
        when(item1.getCantAsientos()).thenReturn(2);
        when(item2.getCantAsientos()).thenReturn(1);

        // Configurar calcularCostoItem para que use la lógica real O un valor fijo
        when(item1.calcularCostoItem()).thenReturn(400.0);  // 2 * 200
        when(item2.calcularCostoItem()).thenReturn(600.0);  // 1 * 600

        paquete.getItemPaquetes().add(item1);
        paquete.getItemPaquetes().add(item2);

        // Cálculo esperado: (400 + 600) * (1 - 0.10) = 1000 * 0.90 = 900.0
        double costoEsperado = 900.0;
        double costoCalculado = paquete.calcularCostoReservaPaquete();

        assertEquals(costoEsperado, costoCalculado, 0.001);
        assertEquals(costoEsperado, paquete.getCosto(), 0.001);
    }

    @Test
    void calcularCostoReservaPaquete_SinDescuento_NoDeberiaAplicarDescuento() {
        Paquete paqueteSinDescuento = new Paquete("Sin Desc", "Desc", 0, 30);

        // Mock COMPLETO del ItemPaquete
        ItemPaquete item = mock(ItemPaquete.class);

        // Configurar para calcularCostoItem()
        when(item.calcularCostoItem()).thenReturn(1000.0);

        // Configurar para getCosto() (que se llama después)
        RutaVuelo rutaMock = mock(RutaVuelo.class);
        when(item.getRutaVuelo()).thenReturn(rutaMock);
        when(item.getTipoAsiento()).thenReturn(TipoAsiento.TURISTA);
        when(item.getCantAsientos()).thenReturn(1);
        when(rutaMock.getCostoTurista()).thenReturn(1000.0);
        when(rutaMock.getCostoEjecutivo()).thenReturn(2000.0);

        paqueteSinDescuento.getItemPaquetes().add(item);

        double costo = paqueteSinDescuento.calcularCostoReservaPaquete();

        assertEquals(1000.0, costo, 0.001);
        assertEquals(1000.0, paqueteSinDescuento.getCosto(), 0.001);
    }

    @Test
    void toString_DeberiaRetornarNombre() {
        assertEquals("Paquete Test", paquete.toString());
    }

    @Test
    void obtenerItemPaquetes_DeberiaRetornarListaModificable() {
        List<ItemPaquete> items = paquete.getItemPaquetes();
        assertNotNull(items);
        assertTrue(items.isEmpty());

        // Verificar que la lista es modificable
        ItemPaquete item = mock(ItemPaquete.class);
        items.add(item);
        assertEquals(1, paquete.getItemPaquetes().size());
    }

    @Test
    void obtenerCompras_DeberiaRetornarListaModificable() {
        List<CompraPaqLogica> compras = paquete.getCompras();
        assertNotNull(compras);
        assertTrue(compras.isEmpty());

        // Verificar que la lista es modificable
        CompraPaqLogica compra = mock(CompraPaqLogica.class);
        compras.add(compra);
        assertEquals(1, paquete.getCompras().size());
    }

    @Test
    void constructor_ConDescuentoCeroYValidezCero_DeberiaInicializarCorrectamente() {
        Paquete paqueteCero = new Paquete("Test Cero", "Desc", 0, 0);

        assertEquals(0, paqueteCero.getDescuentoPorc());
        assertEquals(0, paqueteCero.getPeriodoValidezDias());
        assertEquals("Sin descuento", paqueteCero.getDescuento());
        assertEquals("Sin periodo de validez", paqueteCero.getPeriodoValidez());
    }

    @Test
    void obtenerCosto_ConMultiplesItemsYDescuento_DeberiaCalcularEscenarioComplejo() {
        // Escenario complejo con múltiples items y descuento
        Paquete paqueteComplejo = new Paquete("Complejo", "Desc", 20, 45);

        ItemPaquete itemTurista = mock(ItemPaquete.class);
        ItemPaquete itemEjecutivo = mock(ItemPaquete.class);

        RutaVuelo rutaTurista = mock(RutaVuelo.class);
        RutaVuelo rutaEjecutivo = mock(RutaVuelo.class);

        when(itemTurista.getTipoAsiento()).thenReturn(TipoAsiento.TURISTA);
        when(itemEjecutivo.getTipoAsiento()).thenReturn(TipoAsiento.EJECUTIVO);
        when(itemTurista.getRutaVuelo()).thenReturn(rutaTurista);
        when(itemEjecutivo.getRutaVuelo()).thenReturn(rutaEjecutivo);
        when(itemTurista.getCantAsientos()).thenReturn(3);
        when(itemEjecutivo.getCantAsientos()).thenReturn(2);
        when(rutaTurista.getCostoTurista()).thenReturn(150.0);
        when(rutaEjecutivo.getCostoEjecutivo()).thenReturn(350.0);

        paqueteComplejo.getItemPaquetes().add(itemTurista);
        paqueteComplejo.getItemPaquetes().add(itemEjecutivo);

        // Cálculo: (3*150 + 2*350) * (1 - 0.20) = (450 + 700) * 0.80 = 1150 * 0.80 = 920.0
        double costoEsperado = 920.0;
        double costoCalculado = paqueteComplejo.getCosto();

        assertEquals(costoEsperado, costoCalculado, 0.001);
    }
}