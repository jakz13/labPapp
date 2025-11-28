package ServiciosWeb;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebResult;
import jakarta.jws.WebParam;
import java.util.List;
import DataTypes.DtAerolinea;
import DataTypes.DtRutaVuelo;
import DataTypes.DtCiudad;
import DataTypes.DtCliente;
import DataTypes.DtVuelo;
import DataTypes.DtPaquete;
import DataTypes.DtReserva;
import DataTypes.DtItemPaquete;
import DataTypes.DtCategoria;

@WebService(name = "JuanViajesWS", targetNamespace = "http://ServiciosWeb/")
public interface IWebServices {

    @WebMethod
    @WebResult(name = "respuesta")
    String ping(@WebParam(name = "nombre") String nombre);

    // Operaciones ya expuestas
    @WebMethod
    @WebResult(name = "aerolineas")
    List<DtAerolinea> listarAerolineas();

    @WebMethod
    @WebResult(name = "aerolinea")
    DtAerolinea obtenerAerolinea(@WebParam(name = "nickname") String nickname);

    @WebMethod
    @WebResult(name = "rutas")
    List<DtRutaVuelo> listarRutasPorAerolinea(@WebParam(name = "nombreAerolinea") String nombreAerolinea);

    @WebMethod
    void incrementarVisitasRuta(@WebParam(name = "nombreRuta") String nombreRuta);

    @WebMethod
    @WebResult(name = "topRutas")
    List<DtRutaVuelo> obtenerTopRutasMasVisitadas(@WebParam(name = "limite") int limite);

    @WebMethod
    @WebResult(name = "ciudades")
    List<DtCiudad> listarCiudades();

    @WebMethod
    @WebResult(name = "costo")
    double calcularCostoReserva(@WebParam(name = "nombreVuelo") String nombreVuelo,
                                @WebParam(name = "tipoAsiento") String tipoAsiento,
                                @WebParam(name = "cantidadPasajes") int cantidadPasajes,
                                @WebParam(name = "unidadesEquipajeExtra") int unidadesEquipajeExtra);

    // ----------------- Funciones solicitadas -----------------

    // 1. Críticas
    @WebMethod
    void cargarDesdeBd();

    @WebMethod
    @WebResult(name = "ok")
    boolean verificarLogin(@WebParam(name = "email") String email, @WebParam(name = "password") String password);

    @WebMethod
    @WebResult(name = "cliente")
    DtCliente obtenerCliente(@WebParam(name = "nickname") String nickname);

    @WebMethod
    @WebResult(name = "clientes")
    List<DtCliente> listarClientes();

    // 2. Usuarios
    @WebMethod
    void altaCliente(@WebParam(name = "nickname") String nickname,
                     @WebParam(name = "nombre") String nombre,
                     @WebParam(name = "apellido") String apellido,
                     @WebParam(name = "correo") String correo,
                     @WebParam(name = "fechaNacimiento") String fechaNacimiento, // ISO yyyy-MM-dd
                     @WebParam(name = "nacionalidad") String nacionalidad,
                     @WebParam(name = "tipoDoc") String tipoDoc,
                     @WebParam(name = "numDoc") String numDoc,
                     @WebParam(name = "password") String password,
                     @WebParam(name = "imagenUrl") String imagenUrl);

    @WebMethod
    void altaAerolinea(@WebParam(name = "nickname") String nickname,
                       @WebParam(name = "nombre") String nombre,
                       @WebParam(name = "descripcion") String descripcion,
                       @WebParam(name = "email") String email,
                       @WebParam(name = "sitioWeb") String sitioWeb,
                       @WebParam(name = "password") String password,
                       @WebParam(name = "imagenUrl") String imagenUrl);

    @WebMethod
    void modificarDatosClienteCompleto(@WebParam(name = "nickname") String nickname,
                                      @WebParam(name = "nombre") String nombre,
                                      @WebParam(name = "apellido") String apellido,
                                      @WebParam(name = "nacionalidad") String nacionalidad,
                                      @WebParam(name = "fechaNacimiento") String fechaNacimiento,
                                      @WebParam(name = "tipoDoc") String tipoDoc,
                                      @WebParam(name = "numDoc") String numDoc,
                                      @WebParam(name = "password") String password,
                                      @WebParam(name = "imagenUrl") String imagenUrl);

    @WebMethod
    void modificarDatosAerolineaCompleto(@WebParam(name = "nickname") String nickname,
                                        @WebParam(name = "nombre") String nombre,
                                        @WebParam(name = "descripcion") String descripcion,
                                        @WebParam(name = "sitioWeb") String sitioWeb,
                                        @WebParam(name = "password") String password,
                                        @WebParam(name = "imagenUrl") String imagenUrl);

    // 3. Rutas y vuelos
    @WebMethod
    @WebResult(name = "rutasConfirmadas")
    List<DtRutaVuelo> listarRutasConfirmadas(@WebParam(name = "limite") int limite);

    @WebMethod
    void altaRutaVuelo(@WebParam(name = "nombre") String nombre,
                       @WebParam(name = "descripcion") String descripcion,
                       @WebParam(name = "descripcionCorta") String descripcionCorta,
                       @WebParam(name = "aerolineaNickname") String aerolineaNickname,
                       @WebParam(name = "ciudadOrigen") String ciudadOrigen,
                       @WebParam(name = "ciudadDestino") String ciudadDestino,
                       @WebParam(name = "hora") String hora,
                       @WebParam(name = "fechaAlta") String fechaAlta, // yyyy-MM-dd
                       @WebParam(name = "costoTurista") double costoTurista,
                       @WebParam(name = "costoEjecutivo") double costoEjecutivo,
                       @WebParam(name = "costoEquipajeExtra") double costoEquipajeExtra,
                       @WebParam(name = "categorias") List<String> categorias,
                       @WebParam(name = "imagenUrl") String imagenUrl,
                       @WebParam(name = "videoUrl") String videoUrl);

    @WebMethod
    @WebResult(name = "vuelos")
    List<DtVuelo> listarVuelosPorRuta(@WebParam(name = "nombreRuta") String nombreRuta);

    @WebMethod
    @WebResult(name = "vuelo")
    DtVuelo verInfoVueloDt(@WebParam(name = "nombreVuelo") String nombreVuelo);

    @WebMethod
    @WebResult(name = "vuelo")
    DtVuelo obtenerVuelo(@WebParam(name = "nombreVuelo") String nombreVuelo);

    @WebMethod
    void altaVuelo(@WebParam(name = "nombreVuelo") String nombreVuelo,
                  @WebParam(name = "nombreAerolinea") String nombreAerolinea,
                  @WebParam(name = "nombreRuta") String nombreRuta,
                  @WebParam(name = "fecha") String fecha, // yyyy-MM-dd
                  @WebParam(name = "duracion") int duracion,
                  @WebParam(name = "asientosTurista") int asientosTurista,
                  @WebParam(name = "asientosEjecutivo") int asientosEjecutivo,
                  @WebParam(name = "fechaAlta") String fechaAlta,
                  @WebParam(name = "imagenUrl") String imagenUrl);

    // 4. Reservas
    @WebMethod
    @WebResult(name = "reservas")
    List<DtReserva> getReservasCliente(@WebParam(name = "clienteNickname") String clienteNickname);

    @WebMethod
    @WebResult(name = "pasajero")
    DataTypes.DtPasajero crearPasajero(@WebParam(name = "nombre") String nombre, @WebParam(name = "apellido") String apellido);

    @WebMethod
    void crearYRegistrarReserva(@WebParam(name = "nicknameCliente") String nicknameCliente,
                                @WebParam(name = "nombreVuelo") String nombreVuelo,
                                @WebParam(name = "fechaReserva") String fechaReserva, // yyyy-MM-dd
                                @WebParam(name = "costo") double costo,
                                @WebParam(name = "tipoAsiento") String tipoAsiento,
                                @WebParam(name = "cantidadPasajes") int cantidadPasajes,
                                @WebParam(name = "unidadesEquipajeExtra") int unidadesEquipajeExtra,
                                @WebParam(name = "pasajerosNombres") List<String> pasajerosNombres,
                                @WebParam(name = "pasajerosApellidos") List<String> pasajerosApellidos);

    @WebMethod
    @WebResult(name = "checkin")
    DataTypes.DtReserva consultarCheckinReserva(@WebParam(name = "reservaId") Long reservaId, @WebParam(name = "clienteNickname") String clienteNickname);

    @WebMethod
    @WebResult(name = "reservasConCheckin")
    List<DtReserva> obtenerReservasConCheckin(@WebParam(name = "clienteNickname") String clienteNickname);

    @WebMethod
    void realizarCheckinReserva(@WebParam(name = "reservaId") Long reservaId,
                                @WebParam(name = "asientosAsignados") List<String> asientosAsignados,
                                @WebParam(name = "horaEmbarque") String horaEmbarque); // HH:mm[:ss]

    // 5. Paquetes
    @WebMethod
    @WebResult(name = "paquetes")
    List<DtPaquete> listarPaquetes();

    @WebMethod
    @WebResult(name = "paquete")
    DtPaquete obtenerDtPaquete(@WebParam(name = "paqueteId") String paqueteId);

    @WebMethod
    @WebResult(name = "items")
    List<DtItemPaquete> getDtItemRutasPaquete(@WebParam(name = "paqueteId") String paqueteId);

    @WebMethod
    void compraPaquete(@WebParam(name = "paqueteId") String paqueteId,
                       @WebParam(name = "clienteId") String clienteId,
                       @WebParam(name = "validezDias") int validezDias,
                       @WebParam(name = "fechaCompra") String fechaCompra, // yyyy-MM-dd
                       @WebParam(name = "costo") double costo);

    @WebMethod
    @WebResult(name = "categorias")
    List<DtCategoria> listarCategorias();

    @WebMethod
    @WebResult(name = "codigo")
    int puedeFinalizarRuta(@WebParam(name = "nombreRuta") String nombreRuta);

    @WebMethod
    void finalizarRutaVuelo(@WebParam(name = "nombreRuta") String nombreRuta);

    @WebMethod
    @WebResult(name = "rutasFinalizables")
    List<DtRutaVuelo> listarRutasFinalizables(@WebParam(name = "nombreAerolinea") String nombreAerolinea);

// =================== MÉTODOS DE SEGUIMIENTO ===================

    @WebMethod
    public void followUsuario(
            @WebParam(name = "followerNickname") String followerNickname,
            @WebParam(name = "targetNickname") String targetNickname);

    @WebMethod
    public void unfollowUsuario(
            @WebParam(name = "followerNickname") String followerNickname,
            @WebParam(name = "targetNickname") String targetNickname);

    @WebMethod
    @WebResult(name = "cantidadSeguidores")
    public int obtenerCantidadSeguidores(@WebParam(name = "nickname") String nickname);

    @WebMethod
    @WebResult(name = "cantidadSeguidos")
    public int obtenerCantidadSeguidos(@WebParam(name = "nickname") String nickname);

    @WebMethod
    @WebResult(name = "siguiendo")
    public boolean verificarSeguimiento(
            @WebParam(name = "seguidorId") String seguidorId,
            @WebParam(name = "seguidoId") String seguidoId);
    @WebMethod
    public String obtenerHoraRutaPorReserva(@WebParam(name = "idReserva") Long idReserva);

    @WebMethod
    List<String> obtenerAsientosDisponiblesVuelo(@WebParam(name = "reservaId") Long reservaId);
}
