
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Manejador de clientes
public class ManejadorCliente {
    // Métodos de manejo

    private Map<String, Cliente> clientes;
    public static ManejadorCliente instancia = null;

    private ManejadorCliente() {
        clientes = new HashMap<String, Cliente>();
    }

    public static ManejadorCliente getInstance() {
        if (instancia == null) {
            instancia = new ManejadorCliente();
        }
        return instancia;
    }

    public void agregarCliente(Cliente c) {
        String nickname = c.getNickname();
        if (this.obtenerCliente(nickname) == null && this.obtenerClientePorDocumento(c.getNumeroDocumento()) == null && this.obtenerClientePorEmail(c.getEmail()) == null) {            clientes.put(nickname, c);
        } else {
            throw new IllegalArgumentException("Ya existe un cliente con el mismo nickname, documento o email.");
        }
    }

    public Cliente obtenerCliente(String nickname) {
        return ((Cliente) clientes.get(nickname));
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

    public void agregarReserva(Reserva reserva, String nicknameCliente, String idReserva) {
        Cliente cliente = this.obtenerCliente(nicknameCliente);
        if (cliente != null) {
            cliente.agregarReserva(idReserva, reserva);
        } else {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    }


    // ProyectoAerolineas/src/Logica/ManejadorCliente.java
    public void modificarDatosCliente(Cliente clienteTemporal) {
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
        // No se modifica nickname ni email
    }


}
