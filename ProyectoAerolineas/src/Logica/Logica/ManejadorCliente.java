package Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Manejador de clientes
public class ManejadorCliente {
    // Métodos de manejo

    private Map<String, Cliente> clientes;
    public static ManejadorCliente instancia=null;

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
        if (this.obtenerCliente(nickname) == null) {
            clientes.put(nickname, c);
        }else {
            throw new IllegalArgumentException("El cliente con el nickname " + nickname + " ya existe.");
        }
    }

    public Cliente obtenerCliente(String nickname) {
        return ((Cliente) clientes.get(nickname));
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
}
