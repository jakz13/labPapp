package logica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CompraPaqLogicaTest {

    private Cliente cliente;
    private Paquete paquete;

    @BeforeEach
    void setUp() {
        // Cliente de prueba
        cliente = new Cliente(
                "user123",
                "Juan",
                "Perez",
                "juan@mail.com",
                LocalDate.of(1990, 5, 10),
                "Uruguaya",
                TipoDoc.CEDULAIDENTIDAD,
                "12345678",
                "password123",
                "imagen.png"
        );

        // Paquete de prueba
        paquete = new Paquete("PaqueteTest", "Descripción prueba", 10, 30);
    }

    // --- Test constructor completo ---
    @Test
    void constructorDeberiaInicializarCamposCorrectamente() {
        LocalDate fechaCompra = LocalDate.of(2025, 10, 20);
        int validezDias = 30;
        double costo = 1000.0;

        CompraPaqLogica compra = new CompraPaqLogica(cliente, paquete, fechaCompra, validezDias, costo);

        assertEquals(cliente, compra.getCliente(), "Cliente debe ser asignado correctamente");
        assertEquals(paquete, compra.getPaquete(), "Paquete debe ser asignado correctamente");
        assertEquals(fechaCompra, compra.getFechaCompra(), "Fecha de compra debe ser asignada correctamente");
        assertEquals(fechaCompra.plusDays(validezDias), compra.getFechaVenc(), "Fecha de vencimiento debe calcularse correctamente");
        assertEquals(costo, compra.getCosto(), "Costo debe ser asignado correctamente");
    }

    // --- Test setters ---
    @Test
    void settersDeberianActualizarCamposCorrectamente() {
        CompraPaqLogica compra = new CompraPaqLogica();

        LocalDate fechaCompra = LocalDate.of(2025, 10, 20);
        LocalDate fechaVenc = fechaCompra.plusDays(15);
        double costo = 500.0;

        compra.setCliente(cliente);
        compra.setPaquete(paquete);
        compra.setFechaCompra(fechaCompra);
        compra.setFechaVenc(fechaVenc);
        compra.setCosto(costo);

        assertEquals(cliente, compra.getCliente());
        assertEquals(paquete, compra.getPaquete());
        assertEquals(fechaCompra, compra.getFechaCompra());
        assertEquals(fechaVenc, compra.getFechaVenc());
        assertEquals(costo, compra.getCosto());
    }

    // --- Test creación con validez 0 días ---
    @Test
    void constructorConValidezCeroDeberiaAsignarMismaFechaVenc() {
        LocalDate fechaCompra = LocalDate.now();
        CompraPaqLogica compra = new CompraPaqLogica(cliente, paquete, fechaCompra, 0, 100.0);

        assertEquals(fechaCompra, compra.getFechaVenc(), "Si la validez es 0 días, fecha de vencimiento debe ser igual a la fecha de compra");
    }

    @Test
    void noDeberiaPermitirCostoNegativo() {
        LocalDate fechaCompra = LocalDate.now();

        // Test constructor con costo negativo
        assertThrows(IllegalArgumentException.class, () ->
                        new CompraPaqLogica(cliente, paquete, fechaCompra, 10, -100.0),
                "El constructor debe lanzar excepción si el costo es negativo"
        );

        // Test setter con costo negativo
        CompraPaqLogica compra = new CompraPaqLogica(cliente, paquete, fechaCompra, 10, 100.0);
        assertThrows(IllegalArgumentException.class, () ->
                        compra.setCosto(-50.0),
                "El setter debe lanzar excepción si el costo es negativo"
        );
    }

    // --- Test toString (opcional, si quieres sobreescribir) ---
    @Test
    void toStringDeberiaRetornarInfoBasica() {
        CompraPaqLogica compra = new CompraPaqLogica(cliente, paquete, LocalDate.now(), 10, 100.0);
        String str = compra.toString();
        assertNotNull(str, "toString debería retornar algo aunque no esté sobreescrito");
    }
}
