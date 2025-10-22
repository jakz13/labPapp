package logica;

import DataTypes.DtCliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;


class ManejadorClienteTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field f = ManejadorCliente.class.getDeclaredField("instancia");
        f.setAccessible(true);
        f.set(null, null);
    }

    @Test
    void getInstanceRetornaMismaInstancia() {
        ManejadorCliente instance1 = ManejadorCliente.getInstance();
        ManejadorCliente instance2 = ManejadorCliente.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    void agregarClientePersisteYSeAgrega() {
        EntityManager em = org.mockito.Mockito.mock(EntityManager.class);
        EntityTransaction tx = org.mockito.Mockito.mock(EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(cliente.getNickname()).thenReturn("nick1");
        org.mockito.Mockito.when(cliente.getNumeroDocumento()).thenReturn("123456");
        org.mockito.Mockito.when(cliente.getEmail()).thenReturn("email@test.com");
        DtCliente dto = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(cliente.getDtCliente()).thenReturn(dto);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(cliente, em);

        assertNotNull(manejador.obtenerCliente("nick1"));
        org.mockito.Mockito.verify(tx).begin();
        org.mockito.Mockito.verify(em).persist(cliente);
        org.mockito.Mockito.verify(tx).commit();
    }

    @Test
    void agregarClienteLanzaErrorDeDuplicado() {
        EntityManager em = org.mockito.Mockito.mock(EntityManager.class);
        EntityTransaction tx = org.mockito.Mockito.mock(EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(cliente.getNickname()).thenReturn("nick1");
        org.mockito.Mockito.when(cliente.getNumeroDocumento()).thenReturn("123456");
        org.mockito.Mockito.when(cliente.getEmail()).thenReturn("email@test.com");
        DtCliente dto = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(cliente.getDtCliente()).thenReturn(dto);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(cliente, em);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                manejador.agregarCliente(cliente, em));
        assertTrue(ex.getMessage().contains("Ya existe un cliente"));
    }

    @Test
    void modificarDatosClienteActualizaCampos() {
        EntityManager em = org.mockito.Mockito.mock(EntityManager.class);
        EntityTransaction tx = org.mockito.Mockito.mock(EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(cliente.getNickname()).thenReturn("nick1");
        DtCliente dto = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(cliente.getDtCliente()).thenReturn(dto);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(cliente, em);

        org.mockito.Mockito.clearInvocations(em, tx);

        DtCliente clienteTemporal = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(clienteTemporal.getNickname()).thenReturn("nick1");
        org.mockito.Mockito.when(clienteTemporal.getNombre()).thenReturn("NuevoNombre");

        manejador.modificarDatosCliente(clienteTemporal, em);

        org.mockito.Mockito.verify(cliente).setNombre("NuevoNombre");
        org.mockito.Mockito.verify(tx).begin();
        org.mockito.Mockito.verify(em).merge(cliente);
        org.mockito.Mockito.verify(tx).commit();
    }

    @Test
    void obtenerClienteRealPorDocumentoYEmail() {
        jakarta.persistence.EntityManager em = org.mockito.Mockito.mock(jakarta.persistence.EntityManager.class);
        jakarta.persistence.EntityTransaction tx = org.mockito.Mockito.mock(jakarta.persistence.EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente c1 = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(c1.getNickname()).thenReturn("nick-gets-1");
        org.mockito.Mockito.when(c1.getNumeroDocumento()).thenReturn("DOC-1");
        org.mockito.Mockito.when(c1.getEmail()).thenReturn("mail1@test.com");
        DtCliente dto1 = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(c1.getDtCliente()).thenReturn(dto1);

        Cliente c2 = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(c2.getNickname()).thenReturn("nick-gets-2");
        org.mockito.Mockito.when(c2.getNumeroDocumento()).thenReturn("DOC-2");
        org.mockito.Mockito.when(c2.getEmail()).thenReturn("mail2@test.com");
        DtCliente dto2 = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(c2.getDtCliente()).thenReturn(dto2);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(c1, em);
        manejador.agregarCliente(c2, em);

        assertSame(c2, manejador.obtenerClientePorDocumento("DOC-2"));
        assertSame(c1, manejador.obtenerClientePorEmail("mail1@test.com"));
        assertNull(manejador.obtenerClientePorDocumento("DOC-XYZ"));
        assertNull(manejador.obtenerClientePorEmail("no@no.com"));
    }

    @Test
    void verificarLoginRetornaVerdaderoEnCredencialesValidas() {
        EntityManager em = org.mockito.Mockito.mock(EntityManager.class);
        EntityTransaction tx = org.mockito.Mockito.mock(EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(cliente.getNickname()).thenReturn("nick-login");
        org.mockito.Mockito.when(cliente.getNumeroDocumento()).thenReturn("DOC-login");
        org.mockito.Mockito.when(cliente.getEmail()).thenReturn("email@test.com");
        org.mockito.Mockito.when(cliente.verificarPassword("password")).thenReturn(true);
        DtCliente dtoLogin = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(cliente.getDtCliente()).thenReturn(dtoLogin);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(cliente, em);

        assertTrue(manejador.verificarLogin("email@test.com", "password"));
        assertFalse(manejador.verificarLogin("email@test.com", "wrong"));
        assertFalse(manejador.verificarLogin("no@test.com", "password"));
    }

    @Test
    void actualizarPassword() {
        EntityManager em = org.mockito.Mockito.mock(EntityManager.class);
        EntityTransaction tx = org.mockito.Mockito.mock(EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente cliente = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(cliente.getNickname()).thenReturn("luis789");
        org.mockito.Mockito.when(cliente.getNumeroDocumento()).thenReturn("87654321");
        org.mockito.Mockito.when(cliente.getEmail()).thenReturn("luis@mail.com");
        DtCliente dtoPass = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(cliente.getDtCliente()).thenReturn(dtoPass);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(cliente, em);

        org.mockito.Mockito.clearInvocations(em, tx);

        manejador.actualizarPassword("luis@mail.com", "newPassword", em);

        org.mockito.Mockito.verify(cliente).setPassword("newPassword");
        org.mockito.Mockito.verify(tx).begin();
        org.mockito.Mockito.verify(em).merge(cliente);
        org.mockito.Mockito.verify(tx).commit();
    }

    @Test
    void modificarDatosClienteCompletoActualizaCamposYPersiste() {
        jakarta.persistence.EntityManager em = org.mockito.Mockito.mock(jakarta.persistence.EntityManager.class);
        jakarta.persistence.EntityTransaction tx = org.mockito.Mockito.mock(jakarta.persistence.EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente c = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(c.getNickname()).thenReturn("nick-mdc-1");
        org.mockito.Mockito.when(c.getNumeroDocumento()).thenReturn("DOC-MDC-1");
        org.mockito.Mockito.when(c.getEmail()).thenReturn("mdc1@test.com");
        DtCliente dtoC = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(c.getDtCliente()).thenReturn(dtoC);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(c, em);

        org.mockito.Mockito.clearInvocations(em, tx);

        java.time.LocalDate fn = java.time.LocalDate.of(1990, 1, 1);

        manejador.modificarDatosClienteCompleto(
                "nick-mdc-1",
                " Nombre ",
                " Apellido ",
                " UY ",
                fn,
                null,
                " 123 ",
                " pass ",
                " img.jpg ",
                em
        );

        org.mockito.Mockito.verify(c).setNombre("Nombre");
        org.mockito.Mockito.verify(c).setApellido("Apellido");
        org.mockito.Mockito.verify(c).setNacionalidad("UY");
        org.mockito.Mockito.verify(c).setFechaNacimiento(fn);
        org.mockito.Mockito.verify(c).setNumeroDocumento("123");
        org.mockito.Mockito.verify(c).setPassword("pass");
        org.mockito.Mockito.verify(c).setImagenUrl("img.jpg");
        org.mockito.Mockito.verify(tx).begin();
        org.mockito.Mockito.verify(em).merge(c);
        org.mockito.Mockito.verify(tx).commit();
    }


    @Test
    void modificarDatosClienteCompletoNoActualizaPasswordEImagenNulasSiVacias() {
        jakarta.persistence.EntityManager em = org.mockito.Mockito.mock(jakarta.persistence.EntityManager.class);
        jakarta.persistence.EntityTransaction tx = org.mockito.Mockito.mock(jakarta.persistence.EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente c = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(c.getNickname()).thenReturn("nick-mdc-2");
        org.mockito.Mockito.when(c.getNumeroDocumento()).thenReturn("DOC-MDC-2");
        org.mockito.Mockito.when(c.getEmail()).thenReturn("mdc2@test.com");
        DtCliente dtoC2 = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(c.getDtCliente()).thenReturn(dtoC2);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(c, em);

        manejador.modificarDatosClienteCompleto(
                "nick-mdc-2",
                " n ",
                " a ",
                " uy ",
                java.time.LocalDate.of(2000, 2, 2),
                null,
                " doc ",
                "   ",
                "   ",
                em
        );

        org.mockito.Mockito.verify(c, org.mockito.Mockito.never()).setPassword(org.mockito.Mockito.anyString());
        org.mockito.Mockito.verify(c).setImagenUrl(null);
        org.mockito.Mockito.verify(em).merge(c);
    }

    @Test
    void actualizarImagenClienteActualizaYPersisteYLanzaSiNoExiste() {
        jakarta.persistence.EntityManager em = org.mockito.Mockito.mock(jakarta.persistence.EntityManager.class);
        jakarta.persistence.EntityTransaction tx = org.mockito.Mockito.mock(jakarta.persistence.EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente c = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(c.getNickname()).thenReturn("nick-img-1");
        org.mockito.Mockito.when(c.getNumeroDocumento()).thenReturn("DOC-IMG-1");
        org.mockito.Mockito.when(c.getEmail()).thenReturn("img1@test.com");
        DtCliente dtoImg = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(c.getDtCliente()).thenReturn(dtoImg);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(c, em);

        org.mockito.Mockito.clearInvocations(em, tx);

        manejador.actualizarImagenCliente("nick-img-1", "avatar.png", em);

        org.mockito.Mockito.verify(c).setImagenUrl("avatar.png");
        org.mockito.Mockito.verify(tx).begin();
        org.mockito.Mockito.verify(em).merge(c);
        org.mockito.Mockito.verify(tx).commit();

        jakarta.persistence.EntityManager em2 = org.mockito.Mockito.mock(jakarta.persistence.EntityManager.class);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> manejador.actualizarImagenCliente("no-nick", "img.jpg", em2));
        assertTrue(ex.getMessage().contains("Cliente no encontrado"));
        org.mockito.Mockito.verify(em2, org.mockito.Mockito.never()).getTransaction();
    }

    @Test
    void agregarReservaAgregaYMergeaYLanzaSiNoExiste() {
        jakarta.persistence.EntityManager em = org.mockito.Mockito.mock(jakarta.persistence.EntityManager.class);
        jakarta.persistence.EntityTransaction tx = org.mockito.Mockito.mock(jakarta.persistence.EntityTransaction.class);
        org.mockito.Mockito.when(em.getTransaction()).thenReturn(tx);

        Cliente c = org.mockito.Mockito.mock(Cliente.class);
        org.mockito.Mockito.when(c.getNickname()).thenReturn("nick-res-1");
        org.mockito.Mockito.when(c.getNumeroDocumento()).thenReturn("DOC-RES-1");
        org.mockito.Mockito.when(c.getEmail()).thenReturn("res1@test.com");
        DtCliente dtoRes = org.mockito.Mockito.mock(DtCliente.class);
        org.mockito.Mockito.when(c.getDtCliente()).thenReturn(dtoRes);

        ManejadorCliente manejador = ManejadorCliente.getInstance();
        manejador.agregarCliente(c, em);

        Reserva r = org.mockito.Mockito.mock(Reserva.class);
        manejador.agregarReserva(r, "nick-res-1", 1L, em);

        org.mockito.Mockito.verify(c).agregarReserva(r);
        org.mockito.Mockito.verify(em).merge(c);

        jakarta.persistence.EntityManager em2 = org.mockito.Mockito.mock(jakarta.persistence.EntityManager.class);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> manejador.agregarReserva(r, "no-existe", 2L, em2));
        assertTrue(ex.getMessage().contains("Cliente no encontrado"));
        org.mockito.Mockito.verify(em2, org.mockito.Mockito.never()).merge(org.mockito.Mockito.any());
    }
}

