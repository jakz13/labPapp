
package Logica;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;


public class Sistema implements ISistema {

    private EntityManagerFactory emf;
    private EntityManager em;

    private ManejadorCliente manejadorCliente;
    private ManejadorAerolinea manejadorAerolinea;
    private ManejadorCiudad manejadorCiudad;
    private ManejadorRutaVuelo manejadorRutaVuelo;
    private ManejadorVuelo manejadorVuelo;
    private ManejadorPaquete manejadorPaquete;
    private static int idReservaCounter = 0; // Contador para generar IDs únicos de reserva

    public Sistema() {
        this.emf = Persistence.createEntityManagerFactory("LAB_PA");
        this.em = emf.createEntityManager();
        this.manejadorCliente = ManejadorCliente.getInstance();
        this.manejadorPaquete = ManejadorPaquete.getInstance();
        this.manejadorAerolinea = ManejadorAerolinea.getInstance();
        this.manejadorRutaVuelo = ManejadorRutaVuelo.getInstance();
        this.manejadorVuelo = ManejadorVuelo.getInstance();
        this.manejadorCiudad = ManejadorCiudad.getInstance();
    }
    @Override
    public void cargarDesdeBd() {
        manejadorCliente.cargarClientesDesdeBD(em);
        manejadorAerolinea.cargarAerolineasDesdeBD(em);
        manejadorCiudad.cargarCiudadesDesdeBD(em);
        manejadorRutaVuelo.cargarRutasDesdeBD(em);
        manejadorVuelo.cargarVuelosDesdeBD(em);
        manejadorPaquete.cargarPaquetesDesdeBD(em);
    }

    // --- USUARIOS ---
    @Override
    public void altaCliente(String nickname, String nombre, String apellido, String correo, LocalDate fechaNac,
                            String nacionalidad, TipoDoc tipoDoc, String numDoc) {
        if (manejadorAerolinea.obtenerAerolinea(nickname) == null
                && manejadorCliente.obtenerCliente(nickname) == null) {
            Cliente c = new Cliente(nickname, nombre, correo, apellido, fechaNac, nacionalidad, tipoDoc, numDoc);
            manejadorCliente.agregarCliente(c, em);
        } else {
            throw new IllegalArgumentException("Ya existe con ese nickname");
        }
    }


    @Override
    public List<Cliente> listarClientes() {
        return manejadorCliente.getClientes();
    }

    @Override
    public Cliente verInfoCliente(String nickname) {
        Cliente cliente = manejadorCliente.obtenerCliente(nickname);
        if (cliente != null) {
            return cliente;
        } else {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    }
    @Override
    public Cliente obtenerCliente(String nickname) {
        Cliente cliente = manejadorCliente.obtenerCliente(nickname);
        return cliente;
    }

    @Override
    public void altaAerolinea(String nickname, String nombre, String descripcion, String email, String sitioWeb) {
        if (manejadorAerolinea.obtenerAerolinea(nickname) == null
                && manejadorCliente.obtenerCliente(nickname) == null) {
            Aerolinea a = new Aerolinea(nickname, nombre, email, descripcion, sitioWeb);
            manejadorAerolinea.agregarAerolinea(a,em);
        } else {
            throw new IllegalArgumentException("Ya existe con ese nickname");
        }
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
    public Aerolinea obtenerAerolinea(String nickname) {
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nickname);
        if (aerolinea == null) {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }
        return aerolinea;
    }

    @Override
    public List<Aerolinea> listarAerolineas() {
        return manejadorAerolinea.getAerolineas();
    }

    // --- CIUDADES ---
    @Override
    public void altaCiudad(String nombre, String pais) {
        Ciudad c = new Ciudad(nombre, pais);
        manejadorCiudad.agregarCiudad(c,em);
    }

    @Override
    public void altaRutaVuelo(String nombre, String descripcion, Aerolinea aerolinea, String ciudadOrigen, String ciudadDestino, String hora, LocalDate fechaAlta, double costoTurista, double costoEjecutivo, double costoEquipajeExtra, String[] categorias) {

        Aerolinea aero = manejadorAerolinea.obtenerAerolinea(aerolinea.getNickname());
        if (aero != null) {
            RutaVuelo r = new RutaVuelo(nombre, descripcion, aero, ciudadOrigen, ciudadDestino,
                    hora, fechaAlta, costoTurista, costoEjecutivo,
                    costoEquipajeExtra, categorias);

            manejadorRutaVuelo.agregarRutaVuelo(r,em);
            manejadorAerolinea.agregarRutaVueloAAerolinea(aero.getNickname(), r);

        } else {
            throw new IllegalArgumentException("No existe Aerolinea con ese nickname");
        }
    }

    @Override
    public RutaVuelo obtenerRuta(String nombreRuta) {
        return manejadorRutaVuelo.getRuta(nombreRuta);
    }


    public List<RutaVuelo> listarRutasPorAerolinea(String nombreAerolinea) {
        return manejadorAerolinea.obtenerRutaVueloDeAerolinea(nombreAerolinea);
    }

    // --- VUELO ---
    // ProyectoAerolineas/src/Logica/Sistema.java

    /*
    public String altaVueloAux(String nombreAerolinea,String nombreAereolinea, String nombreRuta, String nombreVuelo, LocalDate fecha,
                               int duracion, int asientosTurista, int asientosEjecutivo) {
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nombreAerolinea);
        if (aerolinea == null) {
            return "La aerolínea no existe.";
        }
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        if (ruta == null || !ruta.getAerolinea().getNickname().equals(nombreAerolinea)) {
            return "La ruta no existe para la aerolínea seleccionada.";
        }
        if (manejadorVuelo.getVuelo(nombreVuelo) != null) {
            return "Ya existe un vuelo con ese nombre.";
        }
        Vuelo vuelo = new Vuelo(nombreVuelo, nombreAereolinea, nombreRuta, fecha, duracion, asientosTurista, asientosEjecutivo);
        manejadorVuelo.agregarVuelo(vuelo);
        manejadorRutaVuelo.agregarVueloARuta(nombreRuta, vuelo);
    }
    */

    @Override
    public void altaVuelo(String nombreVuelo, String nombreAereolinea, String nombreRuta, LocalDate fecha, int duracion, int asientosTurista,
                          int asientosEjecutivo, LocalDate fechaAlta) {
        if (manejadorVuelo.getVuelo(nombreVuelo) != null) {
            return;
        }
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        Vuelo vuelo = new Vuelo(nombreVuelo, ruta, fecha, duracion, asientosTurista, asientosEjecutivo, fechaAlta);
        manejadorVuelo.agregarVuelo(vuelo,em);
        manejadorRutaVuelo.agregarVueloARuta(nombreRuta, vuelo,em);
    }


    @Override
    public List<Vuelo> listarVuelosPorRuta(String nombreRuta) {
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        if (ruta == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(ruta.getVuelos().values());
    }

    @Override
    public Vuelo verInfoVuelo(String nombreVuelo) {
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("Vuelo no encontrado");
        }
        return vuelo; // Asumiendo que Vuelo tiene métodos para obtener reservas y datos
    }

    // --- RESERVA ---
    @Override
    public String crearYRegistrarReserva(String nicknameCliente, String nombreVuelo, LocalDate fechaReserva,
                                         double costo,
                                         TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra, List<Pasajero> pasajeros) {
        try {
            // Generar un ID único para la reserva
            String idReserva = "RES" + (++idReservaCounter);
            Reserva reserva = new Reserva(idReserva, costo, tipoAsiento, cantidadPasajes, unidadesEquipajeExtra,
                    pasajeros);
            registrarReservaVuelo(nicknameCliente, nombreVuelo, reserva);
            return "Reserva registrada con éxito.";
        } catch (Exception e) {
            return "Error al registrar la reserva: " + e.getMessage();
        }
    }

    @Override
    public void registrarReservaVuelo(String nicknameCliente, String nombreVuelo, Reserva reserva) {
        // Verificar existencia de cliente y vuelo
        Cliente cliente = manejadorCliente.obtenerCliente(nicknameCliente);
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        if (vuelo == null) {
            throw new IllegalArgumentException("Vuelo no encontrado");
        }

        // Verificar si ya existe una reserva de este cliente para este vuelo
        if (manejadorVuelo.tieneReservaDeCliente(nicknameCliente, vuelo)) {
            throw new IllegalArgumentException("El cliente ya tiene una reserva para este vuelo");
        }
        // Generar un ID único para la reserva
        String idReserva = "RES" + (++idReservaCounter);
        // Registrar la reserva en el vuelo y en el cliente
        vuelo.agregarReserva(nicknameCliente, reserva);
        manejadorCliente.agregarReserva(reserva, nicknameCliente, idReserva,em);
    }

    // --- PAQUETES ---
    @Override
    public void altaPaquete(String nombre, String descripcion, double costo, /*LocalDate fechaAlta,*/ int descuentoPorc,
                            int periodoValidezDias) {
        Paquete p = new Paquete(nombre, descripcion, costo, /*fechaAlta,*/ descuentoPorc, periodoValidezDias);
        manejadorPaquete.crearPaquete(p,em);
    }

    @Override
    public List<Paquete> listarPaquetes() {
        return manejadorPaquete.getPaquetes();
    }

    @Override
    public void modificarDatosDeCliente(String nickname, String nombre, String apellido, String nacionalidad, LocalDate fechaNacimiento, TipoDoc tipoDoc, String numeroDocumento) {
        Cliente cliente = manejadorCliente.obtenerCliente(nickname);
        if (cliente != null) {
            cliente.setApellido(apellido);
            cliente.setNombre(nombre);
            cliente.setNacionalidad(nacionalidad);
            cliente.setFechaNacimiento(fechaNacimiento);
            cliente.setTipoDocumento(tipoDoc);
            cliente.setNumeroDocumento(numeroDocumento);
            manejadorCliente.modificarDatosCliente(cliente,em);
        } else {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
    }



    @Override
    public void modificarDatosAerolinea(String nickname, String Descripcion ,String URL) {
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nickname);
        if (aerolinea != null) {
            aerolinea.setDescripcion(Descripcion);
            aerolinea.setSitioWeb(URL);
            manejadorAerolinea.modificarDatosAerolinea(aerolinea,em);
        } else {
            throw new IllegalArgumentException("Aerolínea no encontrada");
        }
    }

    public List<Paquete> listarPaquetesDisp() {
        return manejadorPaquete.getPaquetesDisp();
    }

    @Override
    public void altaRutaPaquete(String nombrePaquete, String nomRuta, int cantidadAsientos, TipoAsiento tipoAsiento) {
        Paquete p = manejadorPaquete.buscarPaquete(nombrePaquete,em);
        if (p == null) {
            throw new IllegalArgumentException("Paquete no encontrado");
        }


        RutaVuelo ruta = null;
        for (Aerolinea a : listarAerolineas()) {
            for (RutaVuelo r : listarRutasPorAerolinea(a.getNickname())) {
                if (r.getNombre().equals(nomRuta)) {
                    ruta = r;
                    break;
                }
            }
            if (ruta != null) break;
        }


        if (ruta == null) {
            System.out.println("No se encontró la ruta con ese nombre.");
            return;
        }


        try {
            manejadorPaquete.agregarRutaPaquete(p, ruta, cantidadAsientos, tipoAsiento);
            System.out.println("Ruta agregada al paquete correctamente.");
        } catch (IllegalStateException e) {
            System.out.println("ERROR.");
        }
    }



}



