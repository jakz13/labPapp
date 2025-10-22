package logica;

import DataTypes.DtCliente;
import DataTypes.DtPaquete;
import DataTypes.DtReserva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Inicializa un cliente de prueba antes de cada test
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
    }

    // --- Test constructor y getters ---
    @Test
    void constructorYGettersDeberianRetornarDatosCorrectos() {
        // Verifica que el constructor asigna correctamente todos los campos
        assertEquals("user123", cliente.getNickname());
        assertEquals("Juan", cliente.getNombre());
        assertEquals("Perez", cliente.getApellido());
        assertEquals("juan@mail.com", cliente.getEmail());
        assertEquals(LocalDate.of(1990, 5, 10), cliente.getFechaNacimiento());
        assertEquals("Uruguaya", cliente.getNacionalidad());
        assertEquals(TipoDoc.CEDULAIDENTIDAD, cliente.getTipoDocumento());
        assertEquals("12345678", cliente.getNumeroDocumento());
        assertEquals("imagen.png", cliente.getImagenUrl());
        assertNotNull(cliente.getFechaAlta(), "La fecha de alta debe asignarse automáticamente");
    }

    // --- Test setters ---
    @Test
    void settersDeberianActualizarCamposCorrectamente() {
        cliente.setApellido("Gomez");
        cliente.setNacionalidad("Argentina");
        cliente.setNumeroDocumento("87654321");

        assertEquals("Gomez", cliente.getApellido());
        assertEquals("Argentina", cliente.getNacionalidad());
        assertEquals("87654321", cliente.getNumeroDocumento());
    }

    // --- Test agregar reservas ---
    @Test
    void agregarReservaDeberiaAñadirReservaALaLista() {
        Reserva reserva = new Reserva();
        cliente.agregarReserva(reserva);

        List<Reserva> reservas = cliente.getReservas();
        assertEquals(1, reservas.size(), "La lista de reservas debe contener la reserva agregada");
        assertSame(reserva, reservas.get(0), "La reserva agregada debe ser la misma instancia");
    }

    // --- Test getDtCliente ---
    @Test
    void getDtClienteDeberiaRetornarDtoCorrectamente() {
        // Agregamos una reserva de prueba para probar la conversión
        Reserva reserva = new Reserva();
        cliente.agregarReserva(reserva);

        DtCliente dto = cliente.getDtCliente();
        assertEquals(cliente.getNickname(), dto.getNickname());
        assertEquals(cliente.getApellido(), dto.getApellido());
        assertEquals(cliente.getNacionalidad(), dto.getNacionalidad());

        // Verifica que las reservas y paquetes sean listas DTO
        assertEquals(1, dto.getReservas().size(), "Debe incluir la reserva agregada en el DTO");
        assertNotNull(dto.getPaquetesComprados(), "Debe devolver lista de paquetes comprados, aunque esté vacía");
    }

    // --- Test getDtReservas con varias reservas ---
    @Test
    void getDtReservasDeberiaRetornarListaDeDtos() {
        Reserva r1 = new Reserva();
        Reserva r2 = new Reserva();
        cliente.agregarReserva(r1);
        cliente.agregarReserva(r2);

        List<DtReserva> dtReservas = cliente.getDtReservas();
        assertEquals(2, dtReservas.size(), "Debe devolver todas las reservas convertidas a DTO");
    }

    // --- Test getDtPaquetesComprados ---
    @Test
    void getDtPaquetesCompradosDeberiaRetornarListaDto() {
        // Creamos un paquete de prueba
        Paquete paquete = new Paquete("PaqueteTest", "Descripción prueba", 10, 30);

        // Creamos la compra usando el constructor correcto
        LocalDate fechaCompra = LocalDate.now();
        double costo = 1000.0;  // costo de ejemplo
        CompraPaqLogica compra = new CompraPaqLogica(cliente, paquete, fechaCompra, 30, costo);

        // Asociamos la compra al cliente
        cliente.getComprasPaquetes().add(compra);

        // Obtenemos la lista de paquetes comprados como DTOs
        List<DtPaquete> dtPaquetes = cliente.getDtPaquetesComprados();

        // Verificamos que la lista contenga exactamente un elemento
        assertEquals(1, dtPaquetes.size(), "Debe devolver todos los paquetes comprados como DTO");

        // Verificamos que el DTO tenga los datos correctos
        assertEquals("PaqueteTest", dtPaquetes.get(0).getNombre(), "El nombre del paquete debe coincidir con el original");
    }

    // --- Test toString ---
    @Test
    void toStringDeberiaRetornarNombre() {
        assertEquals("Juan", cliente.toString(), "toString debe devolver el nombre del cliente");
    }

    @Test
    void agregarReservaNullNoDeberiaAgregar() {
        assertThrows(IllegalArgumentException.class, () -> cliente.agregarReserva(null));
    }

    @Test
    void crearClienteConNicknameNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente(null, "Nombre", "Apellido", "email@mail.com", LocalDate.now(), "Uruguaya", TipoDoc.CEDULAIDENTIDAD, "1234", "pass", "img.png"));
    }

    @Test
    void crearClienteConNombreNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", null, "Apellido", "email@mail.com", LocalDate.now(), "Uruguaya", TipoDoc.CEDULAIDENTIDAD, "1234", "pass", "img.png"));
    }

    @Test
    void crearClienteConApellidoNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", "Nombre", null, "email@mail.com", LocalDate.now(), "Uruguaya", TipoDoc.CEDULAIDENTIDAD, "1234", "pass", "img.png"));
    }

    @Test
    void crearClienteConEmailNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", "Nombre", "Apellido", null, LocalDate.now(), "Uruguaya", TipoDoc.CEDULAIDENTIDAD, "1234", "pass", "img.png"));
    }

    @Test
    void crearClienteConFechaNacNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", "Nombre", "Apellido", "email@mail.com", null, "Uruguaya", TipoDoc.CEDULAIDENTIDAD, "1234", "pass", "img.png"));
    }

    @Test
    void crearClienteConNacionalidadNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", "Nombre", "Apellido", "email@mail.com", LocalDate.now(), null, TipoDoc.CEDULAIDENTIDAD, "1234", "pass", "img.png"));
    }

    @Test
    void crearClienteConTipoDocNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", "Nombre", "Apellido", "email@mail.com", LocalDate.now(), "Uruguaya", null, "1234", "pass", "img.png"));
    }

    @Test
    void crearClienteConNumDocNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", "Nombre", "Apellido", "email@mail.com", LocalDate.now(), "Uruguaya", TipoDoc.CEDULAIDENTIDAD, null, "pass", "img.png"));
    }

    @Test
    void crearClienteConContraseniaNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Cliente("nickname", "Nombre", "Apellido", "email@mail.com", LocalDate.now(), "Uruguaya", TipoDoc.CEDULAIDENTIDAD, "1234", null, "img.png"));
    }





}
