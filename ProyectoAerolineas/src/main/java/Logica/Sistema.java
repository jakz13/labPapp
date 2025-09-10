package Logica;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import DataTypes.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


public class Sistema implements ISistema {

    private EntityManagerFactory emf;
    private EntityManager em;

    private ManejadorCliente manejadorCliente;
    private ManejadorAerolinea manejadorAerolinea;
    private ManejadorCiudad manejadorCiudad;
    private ManejadorRutaVuelo manejadorRutaVuelo;
    private ManejadorVuelo manejadorVuelo;
    private ManejadorPaquete manejadorPaquete;
    private ManejadorCategoria manejadorCategoria;
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
        this.manejadorCategoria = ManejadorCategoria.getInstance();
    }

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
    public void altaCliente(String nickname, String nombre, String apellido, String correo, LocalDate fechaNac, String nacionalidad, TipoDoc tipoDoc, String numDoc) {
        if (manejadorAerolinea.obtenerAerolinea(nickname) == null
                && manejadorCliente.obtenerCliente(nickname) == null) {
            Cliente c = new Cliente(nickname, nombre, apellido, correo, fechaNac, nacionalidad, tipoDoc, numDoc);
            manejadorCliente.agregarCliente(c,em);
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
        Ciudad c = new Ciudad(nombre, pais);
        manejadorCiudad.agregarCiudad(c,em);
    }

    @Override
    public void altaRutaVuelo(String nombre, String descripcion, DtAerolinea aerolinea, String ciudadOrigen, String ciudadDestino, String hora, LocalDate fechaAlta, double costoTurista, double costoEjecutivo, double costoEquipajeExtra, String[] categorias) {

        Aerolinea aero = manejadorAerolinea.obtenerAerolinea(aerolinea.getNickname());
        if (aero != null) {
            List<String> categoria = new ArrayList<>();
            for (String cat : categorias) {
                Categoria c = manejadorCategoria.buscarCategorias(cat);
                if (c != null) {
                    categoria.add(cat);
                }
            }
            RutaVuelo r = new RutaVuelo(nombre, descripcion, aero, ciudadOrigen, ciudadDestino,
                    hora, fechaAlta, costoTurista, costoEjecutivo,
                    costoEquipajeExtra, categoria);

            manejadorRutaVuelo.agregarRutaVuelo(r,em);
            manejadorAerolinea.agregarRutaVueloAAerolinea(aero.getNickname(), r,em);

        } else {
            throw new IllegalArgumentException("No existe Aerolinea con ese nickname");
        }
    }

    @Override
    public RutaVuelo obtenerRuta(String nombreRuta) {
        return manejadorRutaVuelo.getRuta(nombreRuta);
    }


    public List<DtRutaVuelo> listarRutasPorAerolinea(String nombreAerolinea) {
        return manejadorAerolinea.obtenerRutaVueloDeAerolinea(nombreAerolinea);
    }

    @Override
    public void altaVuelo(String nombreVuelo, String nombreAereolinea, String nombreRuta, LocalDate fecha, int duracion, int asientosTurista,
                          int asientosEjecutivo, LocalDate fechaAlta) {
        if (manejadorVuelo.getVuelo(nombreVuelo) != null) {
            return;
        }
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        Vuelo vuelo = new Vuelo(nombreVuelo, nombreAereolinea, ruta, fecha, duracion, asientosTurista, asientosEjecutivo, fechaAlta);
        manejadorVuelo.agregarVuelo(vuelo,em);
        manejadorRutaVuelo.agregarVueloARuta(nombreRuta, vuelo,em);
    }

    public Vuelo obtenerVuelo(String nombreVuelo) {
        return manejadorVuelo.getVuelo(nombreVuelo);
    }

    @Override
    public List<Vuelo> listarVuelosPorRuta(String nombreRuta) {
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        if (ruta == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(ruta.getVuelos());
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
    public void crearYRegistrarReserva(String nicknameCliente, String nombreVuelo, LocalDate fechaReserva, double costo,
                                       TipoAsiento tipoAsiento, int cantidadPasajes, int unidadesEquipajeExtra, List<Pasajero> pasajeros) {
        try {
            // Generar un ID único para la reserva
            Vuelo vuelo = manejadorVuelo.getVuelo(nombreVuelo);
            String idReserva = "RES" + (++idReservaCounter);
            Reserva reserva = new Reserva(idReserva, costo, tipoAsiento, cantidadPasajes, unidadesEquipajeExtra,
                    pasajeros, vuelo);
            registrarReservaVuelo(nicknameCliente, nombreVuelo, reserva);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al registrar la reserva: ");
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
        vuelo.agregarReserva(idReserva,reserva);
        manejadorCliente.agregarReserva(reserva, nicknameCliente, idReserva);
    }

    // --- PAQUETES ---
    @Override
    public void altaPaquete(String nombre, String descripcion, int descuentoPorc, int periodoValidezDias) {
        Paquete p = new Paquete(nombre, descripcion, /*fechaAlta,*/ descuentoPorc, periodoValidezDias);
        manejadorPaquete.agregarPaquete(p,em);
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
        Paquete p = manejadorPaquete.obtenerPaquete(nombrePaquete);
        if (p == null) {
            throw new IllegalArgumentException("Paquete no encontrado");
        }
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nomRuta);
        /*

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

*/
        if (ruta == null) {
            System.out.println("No se encontró la ruta con ese nombre.");
            return;
        }


        try {
            manejadorPaquete.agregarRutaPaquete(p, ruta, cantidadAsientos, tipoAsiento,em);
            System.out.println("Ruta agregada al paquete correctamente.");
        } catch (IllegalStateException e) {
            System.out.println("ERROR.");
        }
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
        Paquete p = manejadorPaquete.obtenerPaquete(nomPaquete);
        if (p == null) {
            throw new IllegalArgumentException("Paquete no encontrado");
        }

        Cliente c = manejadorCliente.obtenerCliente(nomCliente);
        if (c == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        for (CompraPaqLogica cp : p.getCompras()) {
            if (cp.getCliente().equals(c)) {
                throw new IllegalArgumentException("Error, el cliente " + c.getNombre() + " ya compró el paquete " + p.getNombre() + ". Elija otro o cancele.");
            }
        }

        try {
            manejadorPaquete.compraPaquete(p, c, validezDias, fechaC, costo,em);
            System.out.println("Paquete comprado correctamente.");
        } catch (IllegalStateException e) {
            System.out.println("ERROR.");
        }
    }


    @Override
    public void altaCategoria(String nomCat){
        Categoria cat = manejadorCategoria.buscarCategorias(nomCat);
        if (cat != null) {
            throw new IllegalArgumentException("Error, la categoria ya existe.");
            //Q aca de la opcion de elegir otro paquete o cancelar. no se donde va esta opcion
        }

        try {
            Categoria c = new Categoria(nomCat);
            manejadorCategoria.agregarCategoria(c,em);
            System.out.println("Paquete comprado correctamente.");
        }catch (IllegalStateException e){
            System.out.println("ERROR.");
        }
    }

    // --- DATOS ADICIONALES DE USUARIO ---
    public List<Object> obtenerDatosAdicionalesUsuario(String nickname) {
        List<Object> adicionales = new ArrayList<>();
        Cliente cliente = manejadorCliente.obtenerCliente(nickname);
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


}
