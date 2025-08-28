
import java.time.LocalDate;
import java.util.List;

public interface ISistema {
    // Usuarios
    public abstract void altaCliente(String nickname, String nombre, String apellido, String correo, LocalDate fechaNac, String nacionalidad, String tipoDoc, String numDoc);

    public abstract Cliente verInfoCliente(String nickname);

    public abstract List<Cliente> listarClientes();

    public abstract void altaAerolinea(String nickname, String nombre, String descripcion, String correo, String sitioweb);

    public abstract Aerolinea verInfoAerolinea(String nickname);

    public abstract List<Aerolinea> listarAerolineas();

    // Vuelos y rutas
   // public void cargarDatosEjemplo();

    void altaCiudad(String nombre, String pais);

    public void altaRutaVuelo(String nombre, String descripcion, Aerolinea aerolinea, String ciudadOrigen, String ciudadDestino, String hora, LocalDate fechaAlta, double costoTurista, double costoEjecutivo, double costoEquipajeExtra, String[] categorias);

        public List<RutaVuelo> listarRutasPorAerolinea(String nombreAerolinea);

    public List<Vuelo> listarVuelosPorRuta(String nombreRuta);

    Vuelo verInfoVuelo(String nombreVuelo);

    String altaVueloAux(String nombreAerolinea, String nombreRuta, String nombreVuelo, String fecha, int duracion,
            int asientosTurista, int asientosEjecutivo);

    public boolean altaVuelo(String nombreVuelo, String nombreRuta, String fecha, int duracion, int asientosTurista,
            int asientosEjecutivo);

    public void registrarReservaVuelo(String nicknameCliente, String nombreVuelo, Reserva reserva);

    public String crearYRegistrarReserva(String nicknameCliente, String nombreVuelo, LocalDate fechaReserva,
            double costo,
            TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra, List<Pasajero> pasajeros);

    // --- PAQUETES ---
    void altaPaquete(String nombre, String descripcion, double costo, LocalDate fechaAlta, int descuentoPorc,
            int periodoValidezDias);

    public List<Paquete> listarPaquetes();

    public abstract Aerolinea obtenerAerolinea(String nombreAerolinea);
}
