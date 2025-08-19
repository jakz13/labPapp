
import java.util.List;

public interface ISistema {
    // Usuarios
    void altaCliente(String nickname, String nombre, String apellido, String correo);
    void altaAerolinea(String nickname, String nombre, String descripcion, String correo);

    List<Cliente> listarClientes();
    List<Aerolinea> listarAerolineas();

    // Vuelos y rutas
    void altaCiudad(String nombre, String pais);
    void altaRutaVuelo(String nombre, String aerolinea, String origen, String destino, double costoTurista, double costoEjecutivo);
    void altaVuelo(String nombreRuta, String nombreVuelo, String fecha, int asientosTurista, int asientosEjecutivo);

    // Paquetes
    void crearPaquete(String nombre, String descripcion, int validezDias, double descuento);
}
