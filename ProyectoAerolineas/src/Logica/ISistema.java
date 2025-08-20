
import java.time.LocalDate;
import java.util.List;

public interface ISistema {
    // Usuarios
    public abstract void altaCliente(String nickname, String nombre, String apellido, String correo);
    public abstract Cliente verInfoCliente(String nickname);
    public abstract List<Cliente> listarClientes();


    public abstract void altaAerolinea(String nickname, String nombre, String descripcion, String correo);
    public abstract Aerolinea verInfoAerolinea(String nickname);
    public abstract List<Aerolinea> listarAerolineas();
    // Vuelos y rutas


    void altaCiudad(String nombre, String pais);
    void altaRutaVuelo(String nombre, String aerolinea, String origen, String destino, double costoTurista, double costoEjecutivo);
    void altaVuelo(String nombreRuta, String nombreVuelo, String fecha, int asientosTurista, int asientosEjecutivo);

    // Paquetes
    public abstract void altaPaquete(String nombre, String descripcion, double costo, LocalDate fechaAlta, int descuentoPorc, int periodoValidezDias);
}
