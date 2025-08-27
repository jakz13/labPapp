
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


public class Sistema implements ISistema {

    private ManejadorCliente manejadorCliente;
    private ManejadorAerolinea manejadorAerolinea;
    private ManejadorCiudad manejadorCiudad;
    private ManejadorRutaVuelo manejadorRutaVuelo;
    private ManejadorVuelo manejadorVuelo;
    private ManejadorPaquete manejadorPaquete;
    private static int idReservaCounter = 0; // Contador para generar IDs únicos de reserva

    public Sistema() {
        this.manejadorCliente = ManejadorCliente.getInstance();
        this.manejadorPaquete = ManejadorPaquete.getInstance();
        this.manejadorAerolinea = ManejadorAerolinea.getInstance();
        this.manejadorRutaVuelo = ManejadorRutaVuelo.getInstance();
        this.manejadorVuelo = ManejadorVuelo.getInstance();
        this.manejadorCiudad = ManejadorCiudad.getInstance();
    }

    public void cargarDatosEjemplo() {
        // Alta de aerolínea
        String nickAerolinea = "flyuy";
        String nombreAerolinea = "Fly Uruguay";
        String descripcionAerolinea = "Aerolínea uruguaya de calidad";
        String correoAerolinea = "info@flyuy.com";
        String sitioweb = "www.flyuy.com";
        altaAerolinea(nickAerolinea, nombreAerolinea, descripcionAerolinea, correoAerolinea, sitioweb);

        // Alta de ruta de vuelo asociada
        String nombreRuta = "UY-MVD-BUE";
        String descripcionRuta = "Ruta entre Montevideo y Buenos Aires";
        String hora = "08:00";
        LocalDate fechaAlta = LocalDate.now();
        double costoTurista = 120.0;
        double costoEjecutivo = 250.0;
        double costoEquipajeExtra = 30.0;
        String ciudadOrigen = "Montevideo";
        String ciudadDestino = "Buenos Aires";
        String[] categorias = { "Turista", "Ejecutivo" };

        altaRutaVuelo(
                nombreRuta,
                descripcionRuta,
                nickAerolinea,
                ciudadOrigen,
                ciudadDestino,
                hora,
                fechaAlta,
                costoTurista,
                costoEjecutivo,
                costoEquipajeExtra,
                categorias);
    }

    // --- USUARIOS ---
    @Override
    public void altaCliente(String nickname, String nombre, String apellido, String correo) {
        if (manejadorAerolinea.obtenerAerolinea(nickname) == null
                && manejadorCliente.obtenerCliente(nickname) == null) {
            Cliente c = new Cliente(nickname, nombre, apellido, correo);
            manejadorCliente.agregarCliente(c);
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
    public void altaAerolinea(String nickname, String nombre, String descripcion, String correo, String sitioweb) {
        if (manejadorAerolinea.obtenerAerolinea(nickname) == null
                && manejadorCliente.obtenerCliente(nickname) == null) {
            Aerolinea a = new Aerolinea(nickname, nombre, descripcion, correo, sitioweb);
            manejadorAerolinea.agregarAerolinea(a);
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
        manejadorCiudad.agregarCiudad(c);
    }

    @Override
    public void altaRutaVuelo(String nombre, String codigo, String aerolinea, String origen, String destino,
            String tipo, LocalDate fecha, double costoTurista, double costoEjecutivo, double otroCosto,
            String[] servicios) {
        Aerolinea aero = manejadorAerolinea.obtenerAerolinea(aerolinea);
        if (aero != null) {
            RutaVuelo r = new RutaVuelo(nombre, codigo, aero, origen, destino, tipo, fecha, costoTurista,
                    costoEjecutivo, otroCosto, servicios);
            manejadorRutaVuelo.agregarRutaVuelo(r);
            manejadorAerolinea.agregarRutaVueloAAerolinea(aerolinea, r);
        } else {
            throw new IllegalArgumentException("No existe Aerolinea con ese nickname");
        }
    }

    public List<RutaVuelo> listarRutasPorAerolinea(String nombreAerolinea) {
        return manejadorAerolinea.obtenerRutaVueloDeAerolinea(nombreAerolinea);
    }

    // --- VUELO ---
    // ProyectoAerolineas/src/Logica/Sistema.java

    public String altaVueloAux(String nombreAerolinea, String nombreRuta, String nombreVuelo, String fecha,
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
        Vuelo vuelo = new Vuelo(nombreVuelo, nombreRuta, fecha, duracion, asientosTurista, asientosEjecutivo);
        manejadorVuelo.agregarVuelo(vuelo);
        manejadorRutaVuelo.agregarVueloARuta(nombreRuta, vuelo);
        return "Vuelo dado de alta correctamente.";
    }

    @Override
    public boolean altaVuelo(String nombreVuelo, String nombreRuta, String fecha, int duracion, int asientosTurista,
            int asientosEjecutivo) {
        if (manejadorVuelo.getVuelo(nombreVuelo) != null) {
            return false;
        }
        Vuelo vuelo = new Vuelo(nombreVuelo, nombreRuta, fecha, duracion, asientosTurista, asientosEjecutivo);
        manejadorVuelo.agregarVuelo(vuelo);
        manejadorRutaVuelo.agregarVueloARuta(nombreRuta, vuelo);
        return true;
    }

    public List<Vuelo> listarVuelosPorRuta(String nombreRuta) {
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        if (ruta == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(ruta.getVuelos().values());
    }

    public Vuelo verInfoVuelo(String nombreVuelo) {
        Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);
        if (vuelo == null) {
            throw new IllegalArgumentException("Vuelo no encontrado");
        }
        return vuelo; // Asumiendo que Vuelo tiene métodos para obtener reservas y datos
    }

    public String crearYRegistrarReserva(String nicknameCliente, String nombreVuelo, LocalDate fechaReserva,
            double costo,
            TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra, List<Pasajero> pasajeros) {
        try {
            // Generar un ID único para la reserva
            String idReserva = "RES" + (++idReservaCounter);
            Reserva reserva = new Reserva(idReserva, costo, tipoAsiento, cantidadPasajes, unidadesEquipajeExtra,
                    pasajeros);
            registrarReservaVuelo(nicknameCliente, nombreVuelo, reserva);
            return "✅ Reserva registrada con éxito.";
        } catch (Exception e) {
            return "Error al registrar la reserva: " + e.getMessage();
        }
    }

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
        manejadorCliente.agregarReserva(reserva, nicknameCliente, idReserva);
    }

    // --- PAQUETES ---
    @Override
    public void altaPaquete(String nombre, String descripcion, double costo, LocalDate fechaAlta, int descuentoPorc,
            int periodoValidezDias) {
        Paquete p = new Paquete(nombre, descripcion, costo, fechaAlta, descuentoPorc, periodoValidezDias);
        manejadorPaquete.agregarPaquete(p);
    }

    @Override
    public List<Paquete> listarPaquetes() {
        return manejadorPaquete.getPaquetes();
    }

}
