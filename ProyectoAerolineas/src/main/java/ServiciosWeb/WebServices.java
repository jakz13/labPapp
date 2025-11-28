package ServiciosWeb;

import logica.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.xml.ws.Endpoint;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;

import DataTypes.DtAerolinea;
import DataTypes.DtRutaVuelo;
import DataTypes.DtCiudad;
import DataTypes.DtCliente;
import DataTypes.DtVuelo;
import DataTypes.DtPaquete;
import DataTypes.DtReserva;
import DataTypes.DtItemPaquete;
import DataTypes.DtPasajero;
import DataTypes.DtCategoria;

@WebService(name = "JuanViajesWS", endpointInterface = "ServiciosWeb.IWebServices")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class WebServices implements IWebServices {

    private Endpoint endpoint = null;
    //Constructor
    public WebServices(){}

    //Operaciones las cuales quiero publicar

    @WebMethod(exclude = true)
    public void publicar(){
        // Delegar en la versión configurable; así si se define -Dws.port o WS_PORT
        // se respetará. Pasar null hace que publicar(String) busque la propiedad/env.
        publicar(null);
    }

    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }

    // Método de ejemplo público que será expuesto por SOAP
    @WebMethod
    @WebResult(name = "respuesta")
    public String ping(@WebParam(name = "nombre") String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "Hola";
        }
        return "Hola " + nombre.trim();
    }

    /**
     * Publica el servicio en el puerto definido por la propiedad del sistema 'ws.port'
     * o la variable de entorno 'WS_PORT'. Si no está definida, usa 8081.
     * Muestra mensajes de diagnóstico si el puerto ya está en uso.
     */
    @WebMethod(exclude = true)
    public void publicar(String portArg){
        String portStr = portArg;
        if (portStr == null || portStr.isEmpty()) {
            portStr = System.getProperty("ws.port");
        }
        if (portStr == null || portStr.isEmpty()) {
            portStr = System.getenv("WS_PORT");
        }
        if (portStr == null || portStr.isEmpty()) {
            portStr = "8081";
        }

        String url = "http://localhost:" + portStr + "/JuanViajes";
        try {
            endpoint = Endpoint.publish(url, this);
            System.out.println("Servicio publicado en: " + url + "?wsdl");
        } catch (Exception ex) {
            System.err.println("Error publicando servicio en " + url + ": " + ex.getMessage());
            Throwable cause = ex.getCause();
            if (cause != null) {
                System.err.println("Causa: " + cause);
            }
            System.err.println("Es probable que el puerto " + portStr + " ya esté en uso.");
            System.err.println("En Windows puedes identificar el proceso con:");
            System.err.println("  netstat -ano | findstr :" + portStr);
            System.err.println("y luego matar el proceso con (usando el PID obtenido):");
            System.err.println("  taskkill /PID <pid> /F");
            System.err.println("O ejecutar el publicador en otro puerto, por ejemplo 9090:\n");
            System.err.println("  mvn -DskipTests package && java -Dws.port=9090 -cp \"target/classes;target/dependency/*\" ServiciosWeb.Publicador");
            // rethrow para que si se usa en entornos automatizados, el fallo no pase desapercibido
            throw ex;
        }
    }

    // ----------------- Implementación de las operaciones SOAP -----------------

    @WebMethod
    @WebResult(name = "aerolineas")
    public List<DtAerolinea> listarAerolineas() {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarAerolineas();
        } catch (Exception e) {
            throw new RuntimeException("Error listando aerolineas: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "aerolinea")
    public DtAerolinea obtenerAerolinea(@WebParam(name = "nickname") String nickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerAerolinea(nickname);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo aerolinea: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "rutas")
    public List<DtRutaVuelo> listarRutasPorAerolinea(@WebParam(name = "nombreAerolinea") String nombreAerolinea) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarRutasPorAerolinea(nombreAerolinea);
        } catch (Exception e) {
            throw new RuntimeException("Error listando rutas por aerolinea: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void incrementarVisitasRuta(@WebParam(name = "nombreRuta") String nombreRuta) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            sis.incrementarVisitasRuta(nombreRuta);
        } catch (Exception e) {
            throw new RuntimeException("Error incrementando visitas: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "topRutas")
    public List<DtRutaVuelo> obtenerTopRutasMasVisitadas(@WebParam(name = "limite") int limite) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerTopRutasMasVisitadas(limite);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo top rutas: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "ciudades")
    public List<DtCiudad> listarCiudades() {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarCiudades();
        } catch (Exception e) {
            throw new RuntimeException("Error listando ciudades: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "costo")
    public double calcularCostoReserva(@WebParam(name = "nombreVuelo") String nombreVuelo,
                                       @WebParam(name = "tipoAsiento") String tipoAsiento,
                                       @WebParam(name = "cantidadPasajes") int cantidadPasajes,
                                       @WebParam(name = "unidadesEquipajeExtra") int unidadesEquipajeExtra) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            TipoAsiento ta = TipoAsiento.valueOf(tipoAsiento.toUpperCase());
            return sis.calcularCostoReserva(nombreVuelo, ta, cantidadPasajes, unidadesEquipajeExtra);
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Tipo de asiento inválido: " + tipoAsiento, iae);
        } catch (Exception e) {
            throw new RuntimeException("Error calculando costo reserva: " + e.getMessage(), e);
        }
    }

    // ----------------- Métodos solicitados por el usuario -----------------

    // 1. Críticas
    @WebMethod
    public void cargarDesdeBd() {
        ISistema sis = Fabrica.getInstance().getISistema();
        sis.cargarDesdeBd();
    }

    @WebMethod
    @WebResult(name = "ok")
    public boolean verificarLogin(@WebParam(name = "email") String email, @WebParam(name = "password") String password) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.verificarLogin(email, password);
        } catch (Exception e) {
            throw new RuntimeException("Error verificando login: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "cliente")
    public DtCliente obtenerCliente(@WebParam(name = "nickname") String nickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerCliente(nickname);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo cliente: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "clientes")
    public List<DtCliente> listarClientes() {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarClientes();
        } catch (Exception e) {
            throw new RuntimeException("Error listando clientes: " + e.getMessage(), e);
        }
    }

    // 2. Usuarios
    @WebMethod
    public void altaCliente(@WebParam(name = "nickname") String nickname,
                            @WebParam(name = "nombre") String nombre,
                            @WebParam(name = "apellido") String apellido,
                            @WebParam(name = "correo") String correo,
                            @WebParam(name = "fechaNacimiento") String fechaNacimiento,
                            @WebParam(name = "nacionalidad") String nacionalidad,
                            @WebParam(name = "tipoDoc") String tipoDoc,
                            @WebParam(name = "numDoc") String numDoc,
                            @WebParam(name = "password") String password,
                            @WebParam(name = "imagenUrl") String imagenUrl) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            LocalDate fn = (fechaNacimiento == null || fechaNacimiento.isEmpty()) ? null : LocalDate.parse(fechaNacimiento);
            TipoDoc td = (tipoDoc == null || tipoDoc.isEmpty()) ? null : TipoDoc.valueOf(tipoDoc.toUpperCase());
            sis.altaCliente(nickname, nombre, apellido, correo, fn, nacionalidad, td, numDoc, password, imagenUrl);
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Fecha de nacimiento inválida: " + fechaNacimiento, dtpe);
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            throw new RuntimeException("Error dando de alta cliente: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void altaAerolinea(@WebParam(name = "nickname") String nickname,
                              @WebParam(name = "nombre") String nombre,
                              @WebParam(name = "descripcion") String descripcion,
                              @WebParam(name = "email") String email,
                              @WebParam(name = "sitioWeb") String sitioWeb,
                              @WebParam(name = "password") String password,
                              @WebParam(name = "imagenUrl") String imagenUrl) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.altaAerolinea(nickname, nombre, descripcion, email, sitioWeb, password, imagenUrl);
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception e) {
            throw new RuntimeException("Error dando de alta aerolinea: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void modificarDatosClienteCompleto(@WebParam(name = "nickname") String nickname,
                                             @WebParam(name = "nombre") String nombre,
                                             @WebParam(name = "apellido") String apellido,
                                             @WebParam(name = "nacionalidad") String nacionalidad,
                                             @WebParam(name = "fechaNacimiento") String fechaNacimiento,
                                             @WebParam(name = "tipoDoc") String tipoDoc,
                                             @WebParam(name = "numDoc") String numDoc,
                                             @WebParam(name = "password") String password,
                                             @WebParam(name = "imagenUrl") String imagenUrl) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            LocalDate fn = (fechaNacimiento == null || fechaNacimiento.isEmpty()) ? null : LocalDate.parse(fechaNacimiento);
            TipoDoc td = (tipoDoc == null || tipoDoc.isEmpty()) ? null : TipoDoc.valueOf(tipoDoc.toUpperCase());
            sis.modificarDatosClienteCompleto(nickname, nombre, apellido, nacionalidad, fn, td, numDoc, password, imagenUrl);
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Fecha de nacimiento inválida: " + fechaNacimiento, dtpe);
        } catch (Exception e) {
            throw new RuntimeException("Error modificando cliente: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void modificarDatosAerolineaCompleto(@WebParam(name = "nickname") String nickname,
                                               @WebParam(name = "nombre") String nombre,
                                               @WebParam(name = "descripcion") String descripcion,
                                               @WebParam(name = "sitioWeb") String sitioWeb,
                                               @WebParam(name = "password") String password,
                                               @WebParam(name = "imagenUrl") String imagenUrl) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.modificarDatosAerolineaCompleto(nickname, nombre, descripcion, sitioWeb, password, imagenUrl);
        } catch (Exception e) {
            throw new RuntimeException("Error modificando aerolinea: " + e.getMessage(), e);
        }
    }

    // 3. Rutas y vuelos
    @WebMethod
    @WebResult(name = "rutasConfirmadas")
    public List<DtRutaVuelo> listarRutasConfirmadas(@WebParam(name = "limite") int limite) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarRutasConfirmadas(limite);
        } catch (Exception e) {
            throw new RuntimeException("Error listando rutas confirmadas: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void altaRutaVuelo(@WebParam(name = "nombre") String nombre,
                              @WebParam(name = "descripcion") String descripcion,
                              @WebParam(name = "descripcionCorta") String descripcionCorta,
                              @WebParam(name = "aerolineaNickname") String aerolineaNickname,
                              @WebParam(name = "ciudadOrigen") String ciudadOrigen,
                              @WebParam(name = "ciudadDestino") String ciudadDestino,
                              @WebParam(name = "hora") String hora,
                              @WebParam(name = "fechaAlta") String fechaAlta,
                              @WebParam(name = "costoTurista") double costoTurista,
                              @WebParam(name = "costoEjecutivo") double costoEjecutivo,
                              @WebParam(name = "costoEquipajeExtra") double costoEquipajeExtra,
                              @WebParam(name = "categorias") List<String> categorias,
                              @WebParam(name = "imagenUrl") String imagenUrl,
                              @WebParam(name = "videoUrl") String videoUrl) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            DtAerolinea dtAero = sis.obtenerAerolinea(aerolineaNickname);
            String[] cats = (categorias == null) ? new String[0] : categorias.toArray(new String[0]);
            LocalDate fa = (fechaAlta == null || fechaAlta.isEmpty()) ? LocalDate.now() : LocalDate.parse(fechaAlta);
            sis.altaRutaVuelo(nombre, descripcion, descripcionCorta, dtAero, ciudadOrigen, ciudadDestino, hora, fa, costoTurista, costoEjecutivo, costoEquipajeExtra, cats, imagenUrl, videoUrl);
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Fecha alta inválida: " + fechaAlta, dtpe);
        } catch (Exception e) {
            throw new RuntimeException("Error dando de alta ruta: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "vuelos")
    public List<DtVuelo> listarVuelosPorRuta(@WebParam(name = "nombreRuta") String nombreRuta) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarVuelosPorRuta(nombreRuta);
        } catch (Exception e) {
            throw new RuntimeException("Error listando vuelos por ruta: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "vuelo")
    public DtVuelo verInfoVueloDt(@WebParam(name = "nombreVuelo") String nombreVuelo) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.verInfoVueloDt(nombreVuelo);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo info de vuelo: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "vuelo")
    public DtVuelo obtenerVuelo(@WebParam(name = "nombreVuelo") String nombreVuelo) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            // El ISistema devuelve el objeto Vuelo para obtenerVuelo; aquí devolvemos el DTO equivalente
            return sis.verInfoVueloDt(nombreVuelo);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo vuelo: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void altaVuelo(@WebParam(name = "nombreVuelo") String nombreVuelo,
                         @WebParam(name = "nombreAerolinea") String nombreAerolinea,
                         @WebParam(name = "nombreRuta") String nombreRuta,
                         @WebParam(name = "fecha") String fecha,
                         @WebParam(name = "duracion") int duracion,
                         @WebParam(name = "asientosTurista") int asientosTurista,
                         @WebParam(name = "asientosEjecutivo") int asientosEjecutivo,
                         @WebParam(name = "fechaAlta") String fechaAlta,
                         @WebParam(name = "imagenUrl") String imagenUrl) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            LocalDate f = (fecha == null || fecha.isEmpty()) ? LocalDate.now() : LocalDate.parse(fecha);
            LocalDate fa = (fechaAlta == null || fechaAlta.isEmpty()) ? LocalDate.now() : LocalDate.parse(fechaAlta);
            sis.altaVuelo(nombreVuelo, nombreAerolinea, nombreRuta, f, duracion, asientosTurista, asientosEjecutivo, fa, imagenUrl);
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Fecha inválida: " + fecha + " / " + fechaAlta, dtpe);
        } catch (Exception e) {
            throw new RuntimeException("Error dando de alta vuelo: " + e.getMessage(), e);
        }
    }

    // 4. Reservas
    @WebMethod
    @WebResult(name = "reservas")
    public List<DtReserva> getReservasCliente(@WebParam(name = "clienteNickname") String clienteNickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.getReservasCliente(clienteNickname);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo reservas del cliente: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "pasajero")
    public DtPasajero crearPasajero(@WebParam(name = "nombre") String nombre, @WebParam(name = "apellido") String apellido) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            // usar la fábrica del sistema para validar/crear
            Pasajero p = sis.crearPasajero(nombre, apellido);
            return new DtPasajero(p.getNombre(), p.getApellido());
        } catch (Exception e) {
            throw new RuntimeException("Error creando pasajero: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void crearYRegistrarReserva(@WebParam(name = "nicknameCliente") String nicknameCliente,
                                       @WebParam(name = "nombreVuelo") String nombreVuelo,
                                       @WebParam(name = "fechaReserva") String fechaReserva,
                                       @WebParam(name = "costo") double costo,
                                       @WebParam(name = "tipoAsiento") String tipoAsiento,
                                       @WebParam(name = "cantidadPasajes") int cantidadPasajes,
                                       @WebParam(name = "unidadesEquipajeExtra") int unidadesEquipajeExtra,
                                       @WebParam(name = "pasajerosNombres") List<String> pasajerosNombres,
                                       @WebParam(name = "pasajerosApellidos") List<String> pasajerosApellidos) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            LocalDate fr = (fechaReserva == null || fechaReserva.isEmpty()) ? LocalDate.now() : LocalDate.parse(fechaReserva);
            TipoAsiento ta = TipoAsiento.valueOf(tipoAsiento.toUpperCase());

            List<Pasajero> pasajeros = new ArrayList<>();
            if (pasajerosNombres != null && pasajerosApellidos != null) {
                int n = Math.min(pasajerosNombres.size(), pasajerosApellidos.size());
                for (int i = 0; i < n; i++) {
                    Pasajero p = sis.crearPasajero(pasajerosNombres.get(i), pasajerosApellidos.get(i));
                    pasajeros.add(p);
                }
            }

            sis.crearYRegistrarReserva(nicknameCliente, nombreVuelo, fr, costo, ta, cantidadPasajes, unidadesEquipajeExtra, pasajeros);
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Fecha de reserva inválida: " + fechaReserva, dtpe);
        } catch (Exception e) {
            throw new RuntimeException("Error creando/registrando reserva: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "checkin")
    public DtReserva consultarCheckinReserva(@WebParam(name = "reservaId") Long reservaId, @WebParam(name = "clienteNickname") String clienteNickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.consultarCheckinReserva(reservaId, clienteNickname);
        } catch (Exception e) {
            throw new RuntimeException("Error consultando checkin reserva: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "reservasConCheckin")
    public List<DtReserva> obtenerReservasConCheckin(@WebParam(name = "clienteNickname") String clienteNickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerReservasConCheckin(clienteNickname);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo reservas con checkin: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void realizarCheckinReserva(@WebParam(name = "reservaId") Long reservaId,
                                       @WebParam(name = "asientosAsignados") List<String> asientosAsignados,
                                       @WebParam(name = "horaEmbarque") String horaEmbarque) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            LocalTime ht = (horaEmbarque == null || horaEmbarque.isEmpty()) ? null : LocalTime.parse(horaEmbarque);
            sis.realizarCheckinReserva(reservaId, asientosAsignados, ht);
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Hora de embarque inválida: " + horaEmbarque, dtpe);
        } catch (Exception e) {
            throw new RuntimeException("Error realizando checkin: " + e.getMessage(), e);
        }
    }

    // 5. Paquetes
    @WebMethod
    @WebResult(name = "paquetes")
    public List<DtPaquete> listarPaquetes() {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarPaquetes();
        } catch (Exception e) {
            throw new RuntimeException("Error listando paquetes: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "paquete")
    public DtPaquete obtenerDtPaquete(@WebParam(name = "paqueteId") String paqueteId) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerDtPaquete(paqueteId);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo paquete: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "items")
    public List<DtItemPaquete> getDtItemRutasPaquete(@WebParam(name = "paqueteId") String paqueteId) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.getDtItemRutasPaquete(paqueteId);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo items de paquete: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void compraPaquete(@WebParam(name = "paqueteId") String paqueteId,
                              @WebParam(name = "clienteId") String clienteId,
                              @WebParam(name = "validezDias") int validezDias,
                              @WebParam(name = "fechaCompra") String fechaCompra,
                              @WebParam(name = "costo") double costo) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            LocalDate fc = (fechaCompra == null || fechaCompra.isEmpty()) ? LocalDate.now() : LocalDate.parse(fechaCompra);
            sis.compraPaquete(paqueteId, clienteId, validezDias, fc, costo);
        } catch (DateTimeParseException dtpe) {
            throw new RuntimeException("Fecha de compra inválida: " + fechaCompra, dtpe);
        } catch (Exception e) {
            throw new RuntimeException("Error comprando paquete: " + e.getMessage(), e);
        }
    }
    // 6. Catálogos - FUNCIÓN FALTANTE
    @WebMethod
    @WebResult(name = "categorias")
    public List<DtCategoria> listarCategorias() {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            try {
                sis.cargarDesdeBd();
            } catch (Exception dbEx) {
                // No propagamos el fallo de la carga desde BD: devolvemos la lista en memoria
                System.err.println("Advertencia: falla al cargar desde BD en listarCategorias: " + dbEx.getMessage());
                // continuar y tratar de devolver lo que haya en memoria (podría ser vacío)
            }
            return sis.listarCategorias();
        } catch (Exception e) {
            throw new RuntimeException("Error listando categorias: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "codigo")
    public int puedeFinalizarRuta(@WebParam(name = "nombreRuta") String nombreRuta) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.puedeFinalizarRuta(nombreRuta);
        } catch (Exception e) {
            System.err.println("Error en puedeFinalizarRuta para ruta " + nombreRuta + ": " + e.getMessage());
            e.printStackTrace();
            return -1; // Código de error
        }
    }
    // Agrega este método en WebServices.java, junto con los otros métodos

    @WebMethod
    public void finalizarRutaVuelo(@WebParam(name = "nombreRuta") String nombreRuta) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            sis.finalizarRutaVuelo(nombreRuta);
        } catch (Exception e) {
            System.err.println("Error finalizando ruta " + nombreRuta + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error finalizando ruta: " + e.getMessage(), e);
        }
    }
    @WebMethod
    @WebResult(name = "rutasFinalizables")
    public List<DtRutaVuelo> listarRutasFinalizables(@WebParam(name = "nombreAerolinea") String nombreAerolinea) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.listarRutasFinalizables(nombreAerolinea);
        } catch (Exception e) {
            System.err.println("Error listando rutas finalizables para aerolínea " + nombreAerolinea + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error listando rutas finalizables: " + e.getMessage(), e);
        }
    }

    // =================== MÉTODOS DE SEGUIMIENTO ===================

    @WebMethod
    public void followUsuario(
            @WebParam(name = "followerNickname") String followerNickname,
            @WebParam(name = "targetNickname") String targetNickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            sis.followUsuario(followerNickname, targetNickname);
        } catch (Exception e) {
            throw new RuntimeException("Error siguiendo usuario: " + e.getMessage(), e);
        }
    }

    @WebMethod
    public void unfollowUsuario(
            @WebParam(name = "followerNickname") String followerNickname,
            @WebParam(name = "targetNickname") String targetNickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            sis.unfollowUsuario(followerNickname, targetNickname);
        } catch (Exception e) {
            throw new RuntimeException("Error dejando de seguir usuario: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "cantidadSeguidores")
    public int obtenerCantidadSeguidores(@WebParam(name = "nickname") String nickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerCantidadSeguidores(nickname);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo cantidad de seguidores: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "cantidadSeguidos")
    public int obtenerCantidadSeguidos(@WebParam(name = "nickname") String nickname) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerCantidadSeguidos(nickname);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo cantidad de seguidos: " + e.getMessage(), e);
        }
    }

    @WebMethod
    @WebResult(name = "siguiendo")
    public boolean verificarSeguimiento(
            @WebParam(name = "seguidorId") String seguidorId,
            @WebParam(name = "seguidoId") String seguidoId) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.verificarSeguimiento(seguidorId, seguidoId);
        } catch (Exception e) {
            throw new RuntimeException("Error verificando seguimiento: " + e.getMessage(), e);
        }
    }
    @WebMethod
    public String obtenerHoraRutaPorReserva(Long idReserva) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            String reserva = sis.obtenerHoraRutaPorReserva(idReserva);

            // Navegar a través de las relaciones: Reserva -> Vuelo -> RutaVuelo -> Hora
            return reserva;

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo hora de la ruta para reserva: " + idReserva, e);
        }
    }

    @WebMethod
    @WebResult(name = "asientosDisponibles")
    public List<String> obtenerAsientosDisponiblesVuelo(@WebParam(name = "reservaId") Long reservaId) {
        try {
            ISistema sis = Fabrica.getInstance().getISistema();
            sis.cargarDesdeBd();
            return sis.obtenerAsientosDisponiblesVuelo(reservaId);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo asientos disponibles para reserva: " + reservaId, e);
        }
    }

}
