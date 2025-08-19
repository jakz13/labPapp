
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Manejador de clientes
public class ManejadorCliente {
    // Métodos de manejo

    private Map<String, Cliente> clientes;

    public ManejadorCliente() {
        this.clientes = new java.util.HashMap<>();
    }

    public void agregarCliente(Cliente c) {
        clientes.put(c.getNickname(), c);
    }

    public List<Cliente> getClientes() {
        return new ArrayList<>(clientes.values());
    }
}
