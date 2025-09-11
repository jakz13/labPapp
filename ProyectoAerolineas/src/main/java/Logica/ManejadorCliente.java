package Logica;

import DataTypes.DtCliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.*;

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

    // =================== CRUD BD ===================
    public void cargarClientesDesdeBD(EntityManager em) {
        TypedQuery<Cliente> query = em.createQuery("SELECT c FROM Cliente c", Cliente.class);
        List<Cliente> clientesPersistidos = query.getResultList();
        for (Cliente c : clientesPersistidos) {
            clientes.put(c.getNickname(), c);
        }
    }

    public void agregarCliente(Cliente c, EntityManager em) {
        if (this.obtenerCliente(c.getNickname()) == null &&
                this.obtenerClientePorDocumento(c.getNumeroDocumento()) == null &&
                this.obtenerClientePorEmail(c.getEmail()) == null) {

            clientes.put(c.getNickname(), c);
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(c);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("Ya existe un cliente con el mismo nickname, documento o email.");
        }
    }

    public void modificarDatosCliente(DtCliente clienteTemporal, EntityManager em) {
        DtCliente clienteOriginal = obtenerCliente(clienteTemporal.getNickname());
        if (clienteOriginal == null) throw new IllegalArgumentException("Cliente no encontrado");

        if (clienteTemporal.getNombre() != null) clienteOriginal.setNombre(clienteTemporal.getNombre());
        if (clienteTemporal.getApellido() != null) clienteOriginal.setApellido(clienteTemporal.getApellido());
        if (clienteTemporal.getNacionalidad() != null) clienteOriginal.setNacionalidad(clienteTemporal.getNacionalidad());
        if (clienteTemporal.getTipoDocumento() != null) clienteOriginal.setTipoDocumento(clienteTemporal.getTipoDocumento());
        if (clienteTemporal.getNumeroDocumento() != null) clienteOriginal.setNumeroDocumento(clienteTemporal.getNumeroDocumento());
        if (clienteTemporal.getFechaNacimiento() != null) clienteOriginal.setFechaNacimiento(clienteTemporal.getFechaNacimiento());

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

    // =================== Consultas en memoria ===================
    public DtCliente obtenerCliente(String nickname) {
        Cliente cliente = clientes.get(nickname);
        return (cliente != null) ? new DtCliente(cliente) : null;
    }

    public Cliente obtenerClientePorDocumento(String numeroDocumento) {
        return clientes.values().stream()
                .filter(c -> c.getNumeroDocumento().equals(numeroDocumento))
                .findFirst().orElse(null);
    }

    public Cliente obtenerClientePorEmail(String email) {
        return null;
    }

    public List<DtCliente> getClientes() {
        List<DtCliente> lista = new ArrayList<>();
        for (Cliente c : clientes.values()) {
            lista.add(new DtCliente(c));
        }
        return lista;
    }

    public void agregarReserva(Reserva reserva, String nicknameCliente, String idReserva) {
        DtCliente cliente = obtenerCliente(nicknameCliente);
        Cliente clienteObj = clientes.get(nicknameCliente);
        if (cliente != null) clienteObj.agregarReserva(reserva);
        else throw new IllegalArgumentException("Cliente no encontrado");
    }


}
