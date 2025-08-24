
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
    public void cargarDatosEjemplo();

    void altaCiudad(String nombre, String pais);
    public void altaRutaVuelo(String nombre, String codigo, String aerolinea, String origen, String destino, String tipo, LocalDate fecha, double costoTurista, double costoEjecutivo, double otroCosto, String[] servicios);
    public List<RutaVuelo> listarRutasPorAerolinea(String nombreAerolinea);
    public List<Vuelo> listarVuelosPorRuta(String nombreRuta);
    Vuelo verInfoVuelo(String nombreVuelo);
    String altaVueloAux(String nombreAerolinea, String nombreRuta, String nombreVuelo, String fecha, int duracion, int asientosTurista, int asientosEjecutivo);
    public boolean altaVuelo(String nombreVuelo, String nombreRuta, String fecha, int duracion, int asientosTurista, int asientosEjecutivo);
    // --- PAQUETES ---
    void altaPaquete(String nombre, String descripcion, double costo, LocalDate fechaAlta, int descuentoPorc, int periodoValidezDias);
    public List<Paquete> listarPaquetes();

    public abstract Aerolinea obtenerAerolinea(String nombreAerolinea);
}
