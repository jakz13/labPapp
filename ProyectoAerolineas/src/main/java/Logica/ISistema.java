package Logica;

import java.time.LocalDate;
import java.util.List;
import DataTypes.*;
import Logica.TipoDoc;


public interface ISistema {
    // Usuarios
    public abstract void altaCliente(String nickname, String nombre, String apellido, String correo, LocalDate fechaNac, String nacionalidad, TipoDoc tipoDoc, String numDoc);

    public abstract Cliente verInfoCliente(String nickname);

    public abstract List<DtCliente> listarClientes();

    public abstract void altaAerolinea(String nickname, String nombre, String descripcion, String correo, String sitioweb);

    public abstract Aerolinea verInfoAerolinea(String nickname);

    public abstract List<DtAerolinea> listarAerolineas();

    // Vuelos y rutas
    // public void cargarDatosEjemplo();

    void altaCiudad(String nombre, String pais);

    public void altaRutaVuelo(String nombre, String descripcion, DtAerolinea aerolinea, String ciudadOrigen, String ciudadDestino, String hora, LocalDate fechaAlta, double costoTurista, double costoEjecutivo, double costoEquipajeExtra, String[] categorias);

    public abstract RutaVuelo obtenerRuta(String nombreRuta);

    public List<DtRutaVuelo> listarRutasPorAerolinea(String nombreAerolinea);

    public List<Vuelo> listarVuelosPorRuta(String nombreRuta);

    Vuelo verInfoVuelo(String nombreVuelo);

    //String altaVueloAux(String nombreAerolinea, String nombreRuta, String nombreVuelo, String fecha, int duracion,
    //int asientosTurista, int asientosEjecutivo);

    public void altaVuelo(String nombreVuelo, String nombreAereolinea, String nombreRuta, LocalDate fecha, int duracion, int asientosTurista,
                          int asientosEjecutivo, LocalDate fechaAlta);

    public void registrarReservaVuelo(String nicknameCliente, String nombreVuelo, Reserva reserva);

    public void crearYRegistrarReserva(String nicknameCliente, String nombreVuelo, LocalDate fechaReserva,
                                       double costo,
                                       TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra, List<Pasajero> pasajeros);

    public Vuelo obtenerVuelo(String nombreVuelo);

    // --- PAQUETES ---

    public void altaPaquete(String nombre, String descripcion, int descuentoPorc, int periodoValidezDias);

    public List<DtPaquete> listarPaquetes();

    public abstract DtAerolinea obtenerAerolinea(String nombreAerolinea);

    public abstract void modificarDatosDeCliente(String nickname, String nombre, String apellido, String nacionalidad, LocalDate fechaNacimiento, TipoDoc tipoDoc, String numeroDocumento);

    public abstract void modificarDatosAerolinea(String nickname, String Descripcion ,String URL);

    public abstract void altaRutaPaquete(String nombrePaquete, String nomRuta, int cantidadAsientos, TipoAsiento tipoAsiento);

    public abstract DtCliente obtenerCliente(String nickname);

    double calcularCostoReserva(String nombreVuelo, TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra);

    Pasajero crearPasajero(String nombre, String apellido);

    public void compraPaquete(String nomPaquete, String nomCliente, int validezDias, LocalDate fechaC, double costo);

    public List<Object> obtenerDatosAdicionalesUsuario(String nickname);

    public void altaCategoria(String nomCat);

    public List<DtCiudad> listarCiudades();

    public List<DtCategoria> listarCategorias();

    public DtReserva obtenerReserva(String idReserva, String nicknameCliente);

}
