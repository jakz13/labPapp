package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ManejadorCliente {
    private Map<String, Cliente> clientes;
    public static ManejadorCliente instancia = null;

    private ManejadorCliente() {
        clientes = new HashMap<>();
    }

    public static ManejadorCliente getInstance() {
        if (instancia == null) {
            instancia = new ManejadorCliente();
        }
        return instancia;
    }

    public void cargarClientesDesdeBD(EntityManager em) {
        TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
        List<Cliente> clientesPersistidos = query.getResultList();
        for (Cliente c : clientesPersistidos) {
            clientes.put(c.getNickname(), c);
        }
    }

    public void agregarCliente(Cliente c, EntityManager em) {
        if (this.obtenerCliente(c.getNickname()) == null) {
            em.getTransaction().begin();
            em.persist(c);
            em.getTransaction().commit();
            clientes.put(c.getNickname(), c);
        } else {
            throw new IllegalArgumentException("Ya existe un cliente con ese nickname");
        }
    }


    public Cliente obtenerCliente(String nickname) {
        return clientes.get(nickname);
    }

    public Cliente obtenerClientePorDocumento(String numeroDocumento) {
        for (Cliente cliente : clientes.values()) {
            if (cliente.getNumeroDocumento().equals(numeroDocumento)) {
                return cliente;
            }
        }
        return null;
    }

    public Cliente obtenerClientePorEmail(String email) {
        for (Cliente cliente : clientes.values()) {
            if (cliente.getEmail().equals(email)) {
                return cliente;
            }
        }
        return null;
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes.values());
    }

    public void agregarReserva(Reserva reserva, String nicknameCliente, String idReserva, EntityManager em) {
        Cliente cliente = this.obtenerCliente(nicknameCliente);
        if (cliente != null) {
            cliente.agregarReserva(idReserva, reserva);
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(reserva);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    }

    public void modificarDatosCliente(Cliente clienteTemporal, EntityManager em) {
        Cliente clienteOriginal = obtenerCliente(clienteTemporal.getNickname());
        if (clienteOriginal == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        if (clienteTemporal.getNombre() != null && !clienteTemporal.getNombre().isEmpty()) {
            clienteOriginal.setNombre(clienteTemporal.getNombre());
        }
        if (clienteTemporal.getApellido() != null && !clienteTemporal.getApellido().isEmpty()) {
            clienteOriginal.setApellido(clienteTemporal.getApellido());
        }
        if (clienteTemporal.getNacionalidad() != null && !clienteTemporal.getNacionalidad().isEmpty()) {
            clienteOriginal.setNacionalidad(clienteTemporal.getNacionalidad());
        }
        if (clienteTemporal.getTipoDocumento() != null && !clienteTemporal.getTipoDocumento().isEmpty()) {
            clienteOriginal.setTipoDocumento(clienteTemporal.getTipoDocumento());
        }
        if (clienteTemporal.getNumeroDocumento() != null && !clienteTemporal.getNumeroDocumento().isEmpty()) {
            clienteOriginal.setNumeroDocumento(clienteTemporal.getNumeroDocumento());
        }
        if (clienteTemporal.getFechaNacimiento() != null) {
            clienteOriginal.setFechaNacimiento(clienteTemporal.getFechaNacimiento());
        }
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(clienteOriginal);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }
}

