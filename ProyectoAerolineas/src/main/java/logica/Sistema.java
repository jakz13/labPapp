package logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

import DataTypes.DtCliente;
import DataTypes.DtAerolinea;
import DataTypes.DtCiudad;
import DataTypes.DtCategoria;
import DataTypes.DtVuelo;
import DataTypes.DtReserva;
import DataTypes.DtPaquete;
import DataTypes.DtRutaVuelo;
import DataTypes.DtItemPaquete;


import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import static logica.EstadoRuta.*;

/**
 * Implementación de la interfaz ISistema que actúa como fachada para la
 * lógica de negocio. Maneja inicialización, operaciones CRUD y coordinación
 * entre los manejadores (clientes, aerolíneas, rutas, vuelos, paquetes, etc.).
 */
public class Sistema implements ISistema {

    private static final Logger LOGGER = Logger.getLogger(Sistema.class.getName());

    private EntityManagerFactory emf;
    private EntityManager entManager;

    private ManejadorCliente manejadorCliente;
    private ManejadorAerolinea manejadorAerolinea;
    private ManejadorCiudad manejadorCiudad;
    private ManejadorRutaVuelo manejadorRutaVuelo;
    private ManejadorVuelo manejadorVuelo;
    private ManejadorPaquete manejadorPaquete;
    private ManejadorCategoria manejadorCategoria;

    public Sistema() {
        this.emf = Persistence.createEntityManagerFactory("LAB_PA");
        this.entManager = emf.createEntityManager();
        this.manejadorCliente = ManejadorCliente.getInstance();
        this.manejadorPaquete = ManejadorPaquete.getInstance();
        this.manejadorAerolinea = ManejadorAerolinea.getInstance();
        this.manejadorRutaVuelo = ManejadorRutaVuelo.getInstance();
        this.manejadorVuelo = ManejadorVuelo.getInstance();
        this.manejadorCiudad = ManejadorCiudad.getInstance();
        this.manejadorCategoria = ManejadorCategoria.getInstance();

        // Precargar datos si la base está vacía

    }

    /** Carga todos los datos desde la base de datos a los manejadores en memoria. */
    @Override
    public void cargarDesdeBd() {
        manejadorCliente.cargarClientesDesdeBD(entManager);
        manejadorAerolinea.cargarAerolineasDesdeBD(entManager);
        manejadorCiudad.cargarCiudadesDesdeBD(entManager);
        manejadorRutaVuelo.cargarRutasDesdeBD(entManager);
        manejadorVuelo.cargarVuelosDesdeBD(entManager);
        manejadorPaquete.cargarPaquetesDesdeBD(entManager);
        manejadorCategoria.cargarCategoriasDesdeBD(entManager);
    }

    // =================== USUARIOS CON CONTRASEÑA ===================

    /**
     * Alta de cliente con contraseña. Valida unicidad y persiste el cliente.
     */
    @Override
    public void altaCliente(String nickname, String nombre, String apellido, String correo,
                            LocalDate fechaNac, String nacionalidad, TipoDoc tipoDoc,
                            String numDoc, String password, String imagenUrl) {
        if (manejadorAerolinea.obtenerAerolinea(nickname) == null
                && manejadorCliente.obtenerCliente(nickname) == null) {
            Cliente cliente = new Cliente(nickname, nombre, apellido, correo, fechaNac, nacionalidad, tipoDoc, numDoc, password, imagenUrl);
            manejadorCliente.agregarCliente(cliente, entManager);
        } else {
            throw new IllegalArgumentException("Ya existe un usuario con ese nickname");
        }
    }

    /** Método de compatibilidad: indica que se debe usar la versión con contraseña. */
    public void altaCliente(String nickname, String nombre, String apellido, String correo,
                            LocalDate fechaNac, String nacionalidad, TipoDoc tipoDoc, String numDoc) {
        throw new UnsupportedOperationException("Use el método con contraseña: altaCliente(..., password)");
    }

    /** Alta de aerolínea con contraseña y persistencia. */
    @Override
    public void altaAerolinea(String nickname, String nombre, String descripcion,
                              String email, String sitioWeb, String password, String imagenUrl) {
        if (manejadorAerolinea.obtenerAerolinea(nickname) == null
                && manejadorCliente.obtenerCliente(nickname) == null) {
            Aerolinea aerolinea = new Aerolinea(nickname, nombre, email, password, descripcion, sitioWeb, imagenUrl);
            manejadorAerolinea.agregarAerolinea(aerolinea, entManager);
        } else {
            throw new IllegalArgumentException("Ya existe un usuario con ese nickname");
        }
    }

    /** Método de compatibilidad para alta de aerolínea sin contraseña (no soportado). */
    public void altaAerolinea(String nickname, String nombre, String descripcion,
                              String email, String sitioWeb) {
        throw new UnsupportedOperationException("Use el método con contraseña: altaAerolinea(..., password)");
    }

    /**
     * Actualiza la contraseña de un usuario (cliente o aerolínea) identificado por email.
     */
    @Override
    public void actualizarPassword(String email, String nuevaPassword) {
        try {
            manejadorCliente.actualizarPassword(email, nuevaPassword, entManager);
            LOGGER.info(() -> "Contraseña actualizada para cliente: " + email);
            return;
        } catch (IllegalArgumentException e) {
            // Si no es cliente, continuar
            LOGGER.log(Level.FINE, () -> "El usuario con email " + email + " no es un cliente. Intentando con aerolínea...");
        }

        try {
            manejadorAerolinea.actualizarPassword(email, nuevaPassword, entManager);
            LOGGER.info(() -> "Contraseña actualizada para aerolínea: " + email);
            return;
        } catch (IllegalArgumentException e) {
            // Si no es aerolínea, lanzar error
            LOGGER.log(Level.FINE, () -> "El usuario con email " + email + " no es una aerolínea.");
        }
        throw new IllegalArgumentException("Usuario no encontrado: " + email);
    }

    /** Modifica los datos completos de un cliente y persiste los cambios. */
    @Override
    public void modificarDatosClienteCompleto(String nickname, String nombre, String apellido,
                                              String nacionalidad, LocalDate fechaNacimiento, TipoDoc tipoDocumento,
                                              String numeroDocumento, String password, String imagenUrl) {
        manejadorCliente.modificarDatosClienteCompleto(nickname, nombre, apellido, nacionalidad, fechaNacimiento,
                tipoDocumento, numeroDocumento, password, imagenUrl, entManager);
    }

    /** Modifica los datos completos de una aerolínea y persiste los cambios. */
    @Override
    public void modificarDatosAerolineaCompleto(String nickname, String nombre, String descripcion,
                                                String sitioWeb, String password, String imagenUrl) {
        manejadorAerolinea.modificarDatosAerolineaCompleto(nickname, nombre, descripcion, sitioWeb, password, imagenUrl, entManager);
    }

    // =================== LOGIN ===================

    /** Verifica credenciales buscando primero en clientes y luego en aerolíneas. */
    @Override
    public boolean verificarLogin(String email, String password) {
        // Primero buscar como cliente
        if (manejadorCliente.verificarLogin(email, password)) {
            return true;
        }
        // Si no es cliente, buscar como aerolínea
        return manejadorAerolinea.verificarLogin(email, password);
    }

    /** Devuelve el tipo de usuario ('CLIENTE' o 'AEROLINEA') según el email. */
    @Override
    public String obtenerTipoUsuario(String email) {
        if (manejadorCliente.obtenerClientePorEmail(email) != null) {
            return "CLIENTE";
        } else if (manejadorAerolinea.obtenerAerolineaPorEmail(email) != null) {
            return "AEROLINEA";
        }
        return null;
    }
// =================== MÉTODOS PARA IMÁGENES ===================

    /** Actualiza la URL de la imagen de un cliente. */
    @Override
    public void actualizarImagenCliente(String nickname, String imagenUrl) {
        manejadorCliente.actualizarImagenCliente(nickname, imagenUrl, entManager);
    }

    /** Actualiza la URL de la imagen de una aerolínea. */
    @Override
    public void actualizarImagenAerolinea(String nickname, String imagenUrl) {
        manejadorAerolinea.actualizarImagenAerolinea(nickname, imagenUrl, entManager);
    }

    /** Actualiza la imagen de una ruta y persiste el cambio. */
    @Override
    public void actualizarImagenRuta(String nombreRuta, String imagenUrl) {
        manejadorRutaVuelo.actualizarImagenRuta(nombreRuta, imagenUrl, entManager);
    }

    // Métodos para obtener URLs de imagen (si los necesitan)
    public String obtenerImagenCliente(String nickname) {
        DtCliente cliente = manejadorCliente.obtenerCliente(nickname);
        return cliente != null ? cliente.getImagenUrl() : null;
    }

    public String obtenerImagenAerolinea(String nickname) {
        DtAerolinea aerolinea = manejadorAerolinea.getDtAerolineas().stream()
                .filter(a -> a.getNickname().equals(nickname))
                .findFirst()
                .orElse(null);
        return aerolinea != null ? aerolinea.getImagenUrl() : null;
    }

    public String obtenerImagenRuta(String nombreRuta) {
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        return ruta != null ? ruta.getImagenUrl() : null;
    }

    // ======================================


    @Override
    public List<DtCliente> listarClientes() {
        return manejadorCliente.getClientes();
    }

    @Override
    public Cliente verInfoCliente(String nickname) {
        DtCliente cliente = manejadorCliente.obtenerCliente(nickname);
        Cliente cliente1 = manejadorCliente.obtenerClientePorEmail(cliente.getEmail());
        if (cliente1 != null) {
            return cliente1;
        } else {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    }

    @Override
    public DtCliente obtenerCliente(String nickname) {
        DtCliente cliente = manejadorCliente.obtenerCliente(nickname);
        return cliente;
    }

    @Override
    public Aerolinea verInfoAerolinea(String nickname) {
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nickname);
        if (aerolinea != null) {
            return aerolinea;
        } else {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }
    }

    @Override
    public DtAerolinea obtenerAerolinea(String nickname) {
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nickname);
        if (aerolinea == null) {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }
        return new DtAerolinea(
                aerolinea.getNickname(),
                aerolinea.getNombre(),
                aerolinea.getEmail(),
                aerolinea.getDescripcion(),
                aerolinea.getSitioWeb(),
                aerolinea.getImagenUrl(),
                aerolinea.getRutasVuelo()
        );
    }

    @Override
    public List<DtAerolinea> listarAerolineas() {
        return manejadorAerolinea.getDtAerolineas();
    }

    // --- CIUDADES ---
    @Override
    public void altaCiudad(String nombre, String pais) {
        String clave = nombre.trim().toLowerCase();

        if (manejadorCiudad.obtenerCiudad(clave) != null) {
            throw new IllegalArgumentException("Ya existe una ciudad con el nombre: " + nombre);
        }

        Ciudad ciudad = new Ciudad(nombre, pais);
        manejadorCiudad.agregarCiudad(ciudad, entManager);
    }

    @Override
    public void altaRutaVuelo(String nombre, String descripcion, String descripcionCorta, DtAerolinea aerolinea,
                              String ciudadOrigen, String ciudadDestino, String hora,
                              LocalDate fechaAlta, double costoTurista, double costoEjecutivo,
                              double costoEquipajeExtra, String[] categorias, String imagenUrl, String videoUrl) {

        Aerolinea aero = manejadorAerolinea.obtenerAerolinea(aerolinea.getNickname());
        if (aero == null) {
            throw new IllegalArgumentException("No existe Aerolínea con nickname: " + aerolinea.getNickname());
        }

        if (manejadorRutaVuelo.getRuta(nombre) != null) {
            throw new IllegalArgumentException("Ya existe una ruta de vuelo con el nombre: " + nombre);
        }

        if (ciudadOrigen.equalsIgnoreCase(ciudadDestino)) {
            throw new IllegalArgumentException("La ciudad de origen y destino no pueden ser iguales.");
        }

        List<String> categoriaValida = new ArrayList<>();
        for (String cat : categorias) {
            Categoria categoria = manejadorCategoria.buscarCategorias(cat);
            if (categoria != null) {
                categoriaValida.add(cat);
            }
        }
        if (categoriaValida.isEmpty()) {
            throw new IllegalArgumentException("La ruta debe pertenecer a al menos una categoría válida.");
        }

        RutaVuelo rutaVuelo = new RutaVuelo(nombre, descripcion, descripcionCorta, aero, ciudadOrigen, ciudadDestino,
                hora, fechaAlta, costoTurista, costoEjecutivo,
                costoEquipajeExtra, categoriaValida, imagenUrl, videoUrl);

        manejadorRutaVuelo.agregarRutaVuelo(rutaVuelo, entManager);
        manejadorAerolinea.agregarRutaVueloAAerolinea(aero.getNickname(), rutaVuelo, entManager);
    }

    @Override
    public List<DtAerolinea> obtenerAerolineasConRutasPendientes() {
        List<DtAerolinea> aerolineasConPendientes = new ArrayList<>();
        for (DtAerolinea aero : manejadorAerolinea.getDtAerolineas()) {
            List<RutaVuelo> rutasPendientes = manejadorRutaVuelo.getRutasPorEstadoYAerolinea(
                    aero.getNombre(), INGRESADA);
            if (!rutasPendientes.isEmpty()) {
                aerolineasConPendientes.add(aero);
            }
        }

        return aerolineasConPendientes;
    }

    @Override
    public List<DtRutaVuelo> obtenerRutasPendientesPorAerolinea(String nombreAerolinea) {
        List<RutaVuelo> rutasPendientes = manejadorRutaVuelo.getRutasPorEstadoYAerolinea(
                nombreAerolinea, INGRESADA);

        List<DtRutaVuelo> dtRutasPendientes = new ArrayList<>();
        for (RutaVuelo ruta : rutasPendientes) {
            dtRutasPendientes.add(ruta.getDtRutaVuelo());
        }
        return dtRutasPendientes;
    }

    @Override
    public void aceptarRutaVuelo(String nombreRuta) {
        manejadorRutaVuelo.cambiarEstadoRuta(nombreRuta,CONFIRMADA, entManager);
    }

    @Override
    public void rechazarRutaVuelo(String nombreRuta) {
        manejadorRutaVuelo.cambiarEstadoRuta(nombreRuta, RECHAZADA, entManager);
    }

    @Override
    public RutaVuelo obtenerRuta(String nombreRuta) {
        return manejadorRutaVuelo.getRuta(nombreRuta);
    }

    @Override
    public List<DtRutaVuelo> listarRutasPorAerolinea(String nombreAerolinea) {
        return manejadorAerolinea.obtenerRutaVueloDeAerolinea(nombreAerolinea);
    }

    @Override
    public void altaVuelo(String nombreVuelo, String nombreAereolinea, String nombreRuta,
                          LocalDate fecha, int duracion, int asientosTurista,
                          int asientosEjecutivo, LocalDate fechaAlta, String imagenUrl) {

        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta " + nombreRuta + " no existe.");
        }

        if (manejadorRutaVuelo.getVueloDeRuta(nombreRuta, nombreVuelo) != null) {
            throw new IllegalArgumentException("Ya existe un vuelo con el nombre " + nombreVuelo + " en la ruta " + nombreRuta);
        }

        Vuelo vuelo = new Vuelo(nombreVuelo, nombreAereolinea, ruta, fecha, duracion,
                asientosTurista, asientosEjecutivo, fechaAlta, imagenUrl);

        manejadorVuelo.agregarVuelo(vuelo, entManager);
        manejadorRutaVuelo.agregarVueloARuta(nombreRuta, vuelo, entManager);
    }


    public Vuelo obtenerVuelo(String nombreVuelo) {
        return manejadorVuelo.getVuelo(nombreVuelo);
    }

    @Override
    public List<DtVuelo> listarVuelosPorRuta(String nombreRuta) {
        return manejadorRutaVuelo.obtenerVuelosPorRuta(nombreRuta);
    }

    @Override
    public DtVuelo verInfoVueloDt(String nombreVuelo) {
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("Vuelo no encontrado");
        }
        return vuelo.getDtVuelo();
    }

    @Override
    public Vuelo verInfoVuelo(String nombreVuelo) {
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("Vuelo no encontrado");
        }
        return vuelo;
    }

    // --- RESERVA ---
    /*@Override
    public void crearYRegistrarReserva(String nicknameCliente, String nombreVuelo, LocalDate fechaReserva, double costo,
                                       TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra,
                                       List<Pasajero> pasajeros) {
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("El vuelo no existe: " + nombreVuelo);
        }

        if (ManejadorVuelo.getInstance().tieneReservaDeCliente(nicknameCliente, vuelo)) {
            throw new IllegalArgumentException(
                    "El cliente " + nicknameCliente + " ya tiene una reserva para el vuelo " + nombreVuelo +
                            ". Debe elegir otro vuelo o ruta."
            );
        }

        DtCliente cliente = manejadorCliente.obtenerCliente(nicknameCliente);
        if (cliente != null) {
            for (DtReserva r : cliente.getReservas()) {
                if (r.getVuelo().equals(nombreVuelo)) {
                    throw new IllegalArgumentException(
                            "El cliente " + nicknameCliente + " ya tiene una reserva para el vuelo " + nombreVuelo + "."
                    );
                }
            }
        }

        // CREAR RESERVA SIN ID - se generará automáticamente
        Reserva reserva = new Reserva(
                costo, tipoAsiento, cantidadPasajes, unidadesEquipajeExtra, pasajeros, vuelo
        );

        registrarReservaVuelo(nicknameCliente, nombreVuelo, reserva);
    }*/

    @Override
    public void crearYRegistrarReserva(String nicknameCliente, String nombreVuelo, LocalDate fechaReserva, double costo,
                                       TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra,
                                       List<Pasajero> pasajeros) {
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("El vuelo no existe: " + nombreVuelo);
        }
        List<DtReserva> reservas= vuelo.getDtReservas();
        int asientosOcupadosTurista = 0;
        int asientosOcupadosEjecutivo = 0;
        // Comprobar disponibilidad de asientos
        for (DtReserva reserva : reservas) {
            if (reserva.getTipoAsiento() == tipoAsiento) {
                if (tipoAsiento == TipoAsiento.TURISTA) {
                    asientosOcupadosTurista +=reserva.getCantidadPasajes();

                } else if (tipoAsiento == TipoAsiento.EJECUTIVO) {
                    asientosOcupadosEjecutivo += reserva.getCantidadPasajes();

                }
            }
        }
        if (tipoAsiento == TipoAsiento.TURISTA) {
            if (asientosOcupadosTurista + cantidadPasajes > vuelo.getAsientosTurista()) {
                throw new IllegalArgumentException(
                        "No hay suficientes asientos disponibles en clase TURISTA para el vuelo " + nombreVuelo
                );
            }
        }else {
            if (asientosOcupadosEjecutivo + cantidadPasajes > vuelo.getAsientosEjecutivo()) {
                throw new IllegalArgumentException(
                        "No hay suficientes asientos disponibles en clase EJECUTIVO para el vuelo " + nombreVuelo
                );
            }
        }

        // Comprobar si el cliente ya tiene una reserva en ese vuelo (buscar por nickname en las reservas del vuelo)
        if (manejadorVuelo.tieneReservaDeCliente(nicknameCliente, vuelo)) {
            throw new IllegalArgumentException(
                    "El cliente " + nicknameCliente + " ya tiene una reserva para el vuelo " + nombreVuelo +
                            ". Debe elegir otro vuelo o ruta."
            );
        }

        DtCliente cliente = manejadorCliente.obtenerCliente(nicknameCliente);
        if (cliente != null) {
            for (DtReserva r : cliente.getReservas()) {
                if (r.getVuelo().equals(nombreVuelo)) {
                    throw new IllegalArgumentException(
                            "El cliente " + nicknameCliente + " ya tiene una reserva para el vuelo " + nombreVuelo + "."
                    );
                }
            }
        }

        // CREAR RESERVA SIN ID - se generará automáticamente
        Reserva reserva = new Reserva(
                costo, tipoAsiento, cantidadPasajes, unidadesEquipajeExtra, pasajeros, vuelo
        );

        registrarReservaVuelo(nicknameCliente, nombreVuelo, reserva);
    }


    @Override
    public void registrarReservaVuelo(String nicknameCliente, String nombreVuelo, Reserva reserva) {
        EntityTransaction entTransaction = entManager.getTransaction();
        try {
            entTransaction.begin();

            DtCliente cliente = manejadorCliente.obtenerCliente(nicknameCliente);
            Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);

            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no encontrado");
            }
            if (vuelo == null) {
                throw new IllegalArgumentException("Vuelo no encontrado");
            }

            if (manejadorVuelo.tieneReservaDeCliente(nicknameCliente, vuelo)) {
                throw new IllegalArgumentException("El cliente ya tiene una reserva para este vuelo");
            }

            // Persistir reserva para generar ID automático
            entManager.persist(reserva);
            entManager.flush(); // Forzar generación del ID

            // Agregar reserva al vuelo y al cliente
            vuelo.agregarReserva(reserva);
            manejadorCliente.agregarReserva(reserva, nicknameCliente, reserva.getId(), entManager);
            manejadorVuelo.actualizarVuelo(vuelo, entManager); // Ahora sin transacción interna

            entTransaction.commit();

        } catch (PersistenceException e) {
            if (entTransaction.isActive()) {
                entTransaction.rollback();
            }
            throw new IllegalStateException("Error al registrar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public DtReserva obtenerReserva(Long idReserva, String nicknameCliente) { // Cambiado a Long
        DtCliente cliente = manejadorCliente.obtenerCliente(nicknameCliente);
        try {
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no encontrado");
            }
            for (DtReserva r : cliente.getReservas()) {
                if (r.getId().equals(idReserva)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("Reserva no encontrada");
        } catch (PersistenceException e) {
            throw new IllegalArgumentException("Error al obtener la reserva: " + e.getMessage());
        }
    }

    // --- PAQUETES ---
    @Override
    public void altaPaquete(String nombre, String descripcion, int descuentoPorc, int periodoValidezDias) {
        if (manejadorPaquete.obtenerPaquete(nombre) != null) {
            throw new IllegalArgumentException("Ya existe un paquete con el nombre: " + nombre);
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paquete no puede estar vacío.");
        }
        if (descuentoPorc < 0 || descuentoPorc > 100) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100 por ciento.");
        }
        if (periodoValidezDias < 0) {
            throw new IllegalArgumentException("El período de validez no puede ser negativo.");
        }

        Paquete paquete = new Paquete(nombre.trim(), descripcion, descuentoPorc, periodoValidezDias);
        manejadorPaquete.agregarPaquete(paquete, entManager);
    }

    @Override
    public List<DtPaquete> listarPaquetes() {
        return manejadorPaquete.getPaquetes();
    }

    @Override
    public void modificarDatosDeCliente(String nickname, String nombre, String apellido, String nacionalidad, LocalDate fechaNacimiento, TipoDoc tipoDoc, String numeroDocumento) {
        DtCliente cliente = manejadorCliente.obtenerCliente(nickname);
        if (cliente != null) {
            cliente.setApellido(apellido);
            cliente.setNombre(nombre);
            cliente.setNacionalidad(nacionalidad);
            cliente.setFechaNacimiento(fechaNacimiento);
            cliente.setTipoDocumento(tipoDoc);
            cliente.setNumeroDocumento(numeroDocumento);
            manejadorCliente.modificarDatosCliente(cliente, entManager);
        } else {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    }

    @Override
    public void modificarDatosAerolinea(String nickname, String descripcion, String url) {
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nickname);
        if (aerolinea != null) {
            aerolinea.setDescripcion(descripcion);
            aerolinea.setSitioWeb(url);
            manejadorAerolinea.modificarDatosAerolinea(aerolinea, entManager);
        } else {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }
    }

    @Override
    public List<DtPaquete> listarPaquetesDisp() {
        return manejadorPaquete.getPaquetesDisp();
    }

    @Override
    public double calcularCostoReserva(String nombreVuelo, TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra) {
        ManejadorVuelo manejadorVuelo = ManejadorVuelo.getInstance();
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);

        if (vuelo == null) {
            throw new IllegalArgumentException("Vuelo no encontrado: " + nombreVuelo);
        }

        double costoBase;
        if (tipoAsiento == TipoAsiento.TURISTA) {
            costoBase = 100.0;
        } else {
            costoBase = 200.0;
        }

        double costoPasajes = costoBase * cantidadPasajes;
        double costoEquipaje = 30.0 * unidadesEquipajeExtra;

        return costoPasajes + costoEquipaje;
    }

    @Override
    public Pasajero crearPasajero(String nombre, String apellido) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del pasajero no puede estar vacío.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del pasajero no puede estar vacío.");
        }
        return new Pasajero(nombre.trim(), apellido.trim());
    }

    @Override
    public void compraPaquete(String nomPaquete, String nomCliente, int validezDias, LocalDate fechaC, double costo) {
        Paquete paquete = manejadorPaquete.obtenerPaquete(nomPaquete);
        if (paquete == null) {
            throw new IllegalArgumentException("Paquete no encontrado");
        }

        DtCliente dtCliente = manejadorCliente.obtenerCliente(nomCliente);
        if (dtCliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        for (CompraPaqLogica cp : paquete.getCompras()) {
            if (cp.getCliente().getNickname().equals(dtCliente.getNickname())) {
                throw new IllegalArgumentException("Error, el cliente " + dtCliente.getNombre() + " ya compró el paquete " + paquete.getNombre() + ". Elija otro o cancele.");
            }
        }

        try {
            manejadorPaquete.compraPaquete(paquete, dtCliente, validezDias, fechaC, costo, entManager);
            LOGGER.info(() -> "Paquete comprado correctamente para cliente: " + dtCliente.getNickname() + ", paquete: " + paquete.getNombre());
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, () -> "Error comprando paquete: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void altaCategoria(String nomCat) {
        Categoria cat = manejadorCategoria.buscarCategorias(nomCat);
        if (cat != null) {
            throw new IllegalArgumentException("Error, la categoria ya existe.");
        }

        try {
            Categoria categoria = new Categoria(nomCat);
            manejadorCategoria.agregarCategoria(categoria, entManager);
            LOGGER.info(() -> "Categoría creada correctamente: " + nomCat);
        } catch (IllegalStateException e) {
            LOGGER.log(Level.SEVERE, () -> "Error creando categoría: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Object> obtenerDatosAdicionalesUsuario(String nickname) {
        List<Object> adicionales = new ArrayList<>();
        DtCliente cliente = manejadorCliente.obtenerCliente(nickname);
        if (cliente != null) {
            adicionales.addAll(cliente.getReservas());
            adicionales.addAll(cliente.getPaquetesComprados());
            return adicionales;
        }
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nickname);
        if (aerolinea != null) {
            adicionales.addAll(aerolinea.getRutasVuelo());
            return adicionales;
        }
        return adicionales;
    }

    @Override
    public List<DtCiudad> listarCiudades() {
        List<DtCiudad> dtCiudades = new ArrayList<>();
        for (Ciudad ciudad : manejadorCiudad.getCiudades()) {
            dtCiudades.add(new DtCiudad(ciudad.getNombre(), ciudad.getPais()));
        }
        return dtCiudades;
    }

    @Override
    public List<DtCategoria> listarCategorias() {
        List<DtCategoria> lista = new ArrayList<>();
        for (Categoria c : manejadorCategoria.getCategorias().values()) {
            lista.add(new DtCategoria(c.getNombre()));
        }
        return lista;
    }

    @Override
    public void altaRutaPaquete(String nombrePaquete, String nomRuta, int cantidadAsientos, TipoAsiento tipoAsiento) {
        Paquete paquete = manejadorPaquete.obtenerPaquete(nombrePaquete);
        if (paquete == null) {
            throw new IllegalArgumentException("Paquete no encontrado");
        }

        if (paquete.estaComprado()) {
            throw new IllegalArgumentException("No se pueden agregar rutas a un paquete que ya ha sido comprado.");
        }

        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nomRuta);
        if (ruta == null) {
            throw new IllegalArgumentException("No se encontró la ruta con ese nombre.");
        }

        if (ruta.getEstado() != CONFIRMADA) {
            throw new IllegalArgumentException("No se puede agregar una ruta que no esté CONFIRMADA al paquete. Ruta actual: " + ruta.getEstado());
        }

        manejadorPaquete.agregarRutaAPaquete(paquete, ruta, cantidadAsientos, tipoAsiento, entManager);

        DtPaquete dtPaquete = obtenerDtPaquete(nombrePaquete);
        if (dtPaquete != null) {
            DtRutaVuelo dtRuta = ruta.getDtRutaVuelo();
            DtItemPaquete dtItem = new DtItemPaquete(dtRuta, cantidadAsientos, tipoAsiento.toString());
            dtPaquete.getItems().add(dtItem);
        }
        LOGGER.info(() -> "Ruta agregada al paquete correctamente: " + nombrePaquete + ", ruta: " + nomRuta);
    }

    @Override
    public Paquete obtenerPaquete(String nombrePaquete) {
        Paquete paquete = manejadorPaquete.obtenerPaquete(nombrePaquete);
        if (paquete == null) {
            throw new IllegalArgumentException("Paquete no encontrado");
        }
        return paquete;
    }

    @Override
    public DtPaquete obtenerDtPaquete(String nombrePaquete) {
        DtPaquete paquete = manejadorPaquete.obtenerDtPaquete(nombrePaquete);
        if (paquete == null) {
            throw new IllegalArgumentException("Paquete no encontrado");
        }
        return paquete;
    }

    @Override
    public List<DtAerolinea> getAerolineas() {
        return manejadorAerolinea.getDtAerolineas();
    }

    @Override
    public List<DtPaquete> getPaquetesDisp() {
        return manejadorPaquete.getPaquetes();
    }

    @Override
    public List<DtCliente> getClientes() {
        return manejadorCliente.getClientes();
    }

    @Override
    public List<DtReserva> getReservasCliente(String nickname) {
        DtCliente cliente = manejadorCliente.obtenerCliente(nickname);
        if (cliente != null) {
            return cliente.getReservas();
        } else {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    }

    @Override
    public List<DtItemPaquete> getDtItemRutasPaquete(String nombrePaquete) {
        List<DtItemPaquete> items = manejadorPaquete.obtenerDtItemsPaquete(nombrePaquete);
        if (items != null && !items.isEmpty()) {
            return items;
        } else {
            throw new IllegalArgumentException("Paquete no encontrado o sin rutas asociadas");
        }
    }

    public List<DtRutaVuelo> listarRutasConfirmadas(int limite) {
        List<DtRutaVuelo> rutasConfirmadas = new ArrayList<>();
        List<DtAerolinea> aerolineas = listarAerolineas();

        for (DtAerolinea aerolinea : aerolineas) {
            try {
                List<DtRutaVuelo> rutasAerolinea = listarRutasPorAerolinea(aerolinea.getNickname());
                for (DtRutaVuelo ruta : rutasAerolinea) {
                    if ("CONFIRMADA".equals(ruta.getEstado())) {
                        rutasConfirmadas.add(ruta);
                        if (rutasConfirmadas.size() >= limite) {
                            return rutasConfirmadas;
                        }
                    }
                }
            } catch (PersistenceException  e) {
                continue;
            }
        }
        return rutasConfirmadas;
    }

    @Override
    public List<DtRutaVuelo> obtenerTopRutasMasVisitadas(int limite) {
        try {
            List<RutaVuelo> rutasMasVisitadas = manejadorRutaVuelo.getTopRutasConfirmadasMasVisitadas(limite);
            List<DtRutaVuelo> dtRutas = new ArrayList<>();

            for (RutaVuelo ruta : rutasMasVisitadas) {
                dtRutas.add(ruta.getDtRutaVuelo());
            }

            System.out.println("[SISTEMA] ✅ Top " + dtRutas.size() + " rutas más visitadas obtenidas");
            return dtRutas;

        } catch (Exception e) {
            System.err.println("[SISTEMA] ❌ Error obteniendo DTOs de rutas visitadas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void incrementarVisitasRuta(String nombreRuta) {
        try {
            manejadorRutaVuelo.incrementarVisitasRuta(nombreRuta, entManager);
        } catch (Exception e) {
            System.err.println("[SISTEMA] ❌ Error incrementando visitas: " + e.getMessage());
            throw new RuntimeException("Error al incrementar visitas para ruta: " + nombreRuta, e);
        }
    }

    @Override
    public int obtenerTotalVisitasRuta(String nombreRuta) {
        try {
            RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
            return ruta != null ? ruta.getContadorVisitas() : 0;
        } catch (Exception e) {
            System.err.println("[SISTEMA] ❌ Error obteniendo total de visitas: " + e.getMessage());
            return 0;
        }
    }
/*
    // =================== MÉTODOS DE SEGUIMIENTO (FOLLOW) ===================
    @Override
    public void followUsuario(String followerNickname, String targetNickname) {
        if (followerNickname == null || targetNickname == null) throw new IllegalArgumentException("Nick null");
        if (followerNickname.equals(targetNickname)) throw new IllegalArgumentException("No puede seguirse a si mismo");

        // buscar en manejadores
        Cliente c = manejadorCliente.obtenerClienteReal(followerNickname);
        Aerolinea a = manejadorAerolinea.obtenerAerolinea(followerNickname);
        Usuario follower = (c != null) ? c : a;

        Cliente c2 = manejadorCliente.obtenerClienteReal(targetNickname);
        Aerolinea a2 = manejadorAerolinea.obtenerAerolinea(targetNickname);
        Usuario target = (c2 != null) ? c2 : a2;

        if (follower == null) throw new IllegalArgumentException("Seguidor no encontrado: " + followerNickname);
        if (target == null) throw new IllegalArgumentException("Usuario a seguir no encontrado: " + targetNickname);

        if (follower.estaSiguiendoA(target)) throw new IllegalArgumentException("Ya sigue a ese usuario");

        EntityTransaction tx = entManager.getTransaction();
        try {
            tx.begin();
            // persistir follow en tabla follows
            Follow follow = new Follow(followerNickname, targetNickname);
            entManager.persist(follow);

            // actualizar colecciones en memoria
            follower.seguirA(target);
            if (target instanceof Cliente) ((Cliente) target).addSeguidor(follower);
            else if (target instanceof Aerolinea) ((Aerolinea) target).addSeguidor(follower);

            // merge de las entidades que son persistentes (Cliente/Aerolinea)
            if (follower instanceof Cliente) entManager.merge((Cliente) follower);
            else if (follower instanceof Aerolinea) entManager.merge((Aerolinea) follower);
            if (target instanceof Cliente) entManager.merge((Cliente) target);
            else if (target instanceof Aerolinea) entManager.merge((Aerolinea) target);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw new IllegalStateException("Error al persistir follow: " + e.getMessage(), e);
        }
    }

    @Override
    public void unfollowUsuario(String followerNickname, String targetNickname) {
        if (followerNickname == null || targetNickname == null) throw new IllegalArgumentException("Nick null");
        if (followerNickname.equals(targetNickname)) throw new IllegalArgumentException("No puede dejar de seguirse a si mismo");

        Cliente c = manejadorCliente.obtenerClienteReal(followerNickname);
        Aerolinea a = manejadorAerolinea.obtenerAerolinea(followerNickname);
        Usuario follower = (c != null) ? c : a;

        Cliente c2 = manejadorCliente.obtenerClienteReal(targetNickname);
        Aerolinea a2 = manejadorAerolinea.obtenerAerolinea(targetNickname);
        Usuario target = (c2 != null) ? c2 : a2;

        if (follower == null) throw new IllegalArgumentException("Seguidor no encontrado: " + followerNickname);
        if (target == null) throw new IllegalArgumentException("Usuario a dejar de seguir no encontrado: " + targetNickname);

        if (!follower.estaSiguiendoA(target)) throw new IllegalArgumentException("El usuario no seguía a ese usuario");

        EntityTransaction tx = entManager.getTransaction();
        try {
            tx.begin();
            // borrar el Follow
            jakarta.persistence.Query q = entManager.createQuery("DELETE FROM Follow f WHERE f.id.follower = :f AND f.id.followed = :t");
            q.setParameter("f", followerNickname);
            q.setParameter("t", targetNickname);
            q.executeUpdate();

            // actualizar colecciones en memoria
            follower.dejarDeSeguirA(target);
            if (target instanceof Cliente) ((Cliente) target).removeSeguidor(follower);
            else if (target instanceof Aerolinea) ((Aerolinea) target).removeSeguidor(follower);

            if (follower instanceof Cliente) entManager.merge((Cliente) follower);
            else if (follower instanceof Aerolinea) entManager.merge((Aerolinea) follower);
            if (target instanceof Cliente) entManager.merge((Cliente) target);
            else if (target instanceof Aerolinea) entManager.merge((Aerolinea) target);

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw new IllegalStateException("Error al borrar follow: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isFollowing(String followerNickname, String targetNickname) {
        Cliente c = manejadorCliente.obtenerClienteReal(followerNickname);
        Aerolinea a = manejadorAerolinea.obtenerAerolinea(followerNickname);
        Usuario follower = (c != null) ? c : a;

        Cliente c2 = manejadorCliente.obtenerClienteReal(targetNickname);
        Aerolinea a2 = manejadorAerolinea.obtenerAerolinea(targetNickname);
        Usuario target = (c2 != null) ? c2 : a2;

        if (follower == null || target == null) return false;
        return follower.estaSiguiendoA(target);
    }

    @Override
    public int countFollowers(String nickname) {
        Cliente c = manejadorCliente.obtenerClienteReal(nickname);
        Aerolinea a = manejadorAerolinea.obtenerAerolinea(nickname);
        Usuario u = (c != null) ? c : a;
        return u != null ? u.getSeguidores().size() : 0;
    }

    @Override
    public int countFollowing(String nickname) {
        Cliente c = manejadorCliente.obtenerClienteReal(nickname);
        Aerolinea a = manejadorAerolinea.obtenerAerolinea(nickname);
        Usuario u = (c != null) ? c : a;
        return u != null ? u.getSiguiendo().size() : 0;
    }

    @Override
    public java.util.List<String> getFollowers(String nickname) {
        java.util.List<String> res = new java.util.ArrayList<>();
        Cliente c = manejadorCliente.obtenerClienteReal(nickname);
        Aerolinea a = manejadorAerolinea.obtenerAerolinea(nickname);
        Usuario u = (c != null) ? c : a;
        if (u != null) for (Usuario s : u.getSeguidores()) res.add(s.getNickname());
        return res;
    }

    @Override
    public java.util.List<String> getFollowing(String nickname) {
        java.util.List<String> res = new java.util.ArrayList<>();
        Cliente c = manejadorCliente.obtenerClienteReal(nickname);
        Aerolinea a = manejadorAerolinea.obtenerAerolinea(nickname);
        Usuario u = (c != null) ? c : a;
        if (u != null) for (Usuario s : u.getSiguiendo()) res.add(s.getNickname());
        return res;
    }

    /*Carga la tabla Follow y reconstruye relaciones en memoria entre clientes y aerolineas.
    private void cargarFollowsDesdeBD() {
        try {
            TypedQuery<Follow> q = entManager.createQuery("SELECT f FROM Follow f", Follow.class);
            List<Follow> follows = q.getResultList();
            // limpiar relaciones existentes
            for (Cliente cl : manejadorCliente.getClientesReales()) cl.rebuildSeguimientoFromNicknames(new java.util.HashMap<String, Usuario>());
            for (Aerolinea ar : manejadorAerolinea.getAerolineas()) ar.rebuildSeguimientoFromNicknames(new java.util.HashMap<String, Usuario>());

            // poblar relaciones
            for (Follow f : follows) {
                String follower = f.getFollower();
                String followed = f.getFollowed();
                Cliente cF = manejadorCliente.obtenerClienteReal(follower);
                Aerolinea aF = manejadorAerolinea.obtenerAerolinea(follower);
                Usuario uf = (cF != null) ? cF : aF;

                Cliente cT = manejadorCliente.obtenerClienteReal(followed);
                Aerolinea aT = manejadorAerolinea.obtenerAerolinea(followed);
                Usuario ut = (cT != null) ? cT : aT;

                if (uf != null && ut != null) {
                    uf.seguirA(ut);
                    if (ut instanceof Cliente) ((Cliente) ut).addSeguidor(uf);
                    else if (ut instanceof Aerolinea) ((Aerolinea) ut).addSeguidor(uf);
                }
            }
        } catch (RuntimeException e) {
            // si falla la carga no detener la inicialización
        }
    }*/

}

