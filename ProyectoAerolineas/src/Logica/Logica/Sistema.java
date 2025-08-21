package Logica;

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
        altaAerolinea(nickAerolinea, nombreAerolinea, descripcionAerolinea, correoAerolinea);

        // Alta de ruta de vuelo asociada
        String nombreRuta = "UY-MVD-BUE";
        String ciudadOrigen = "Montevideo";
        String ciudadDestino = "Buenos Aires";
        double costoTurista = 120.0;
        double costoEjecutivo = 250.0;
        altaRutaVuelo(nombreRuta, nickAerolinea, ciudadOrigen, ciudadDestino, costoTurista, costoEjecutivo);
    }

    // --- USUARIOS ---
    @Override
    public void altaCliente(String nickname, String nombre, String apellido, String correo) {
        if (manejadorAerolinea.obtenerAerolinea(nickname)==null && manejadorCliente.obtenerCliente(nickname)==null) {
            Cliente c = new Cliente(nickname, nombre, apellido, correo);
            manejadorCliente.agregarCliente(c);
        }else  {
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
            throw new IllegalArgumentException("Logica.Cliente no encontrado");
        }
    }


    @Override
    public void altaAerolinea(String nickname, String nombre, String descripcion, String correo) {
        if (manejadorAerolinea.obtenerAerolinea(nickname)==null && manejadorCliente.obtenerCliente(nickname)==null) {
            Aerolinea a = new Aerolinea(nickname, nombre, descripcion, correo);
            manejadorAerolinea.agregarAerolinea(a);
        }else  {
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
    public void altaRutaVuelo(String nombre, String aerolinea, String origen, String destino, double costoTurista, double costoEjecutivo) {
        Aerolinea aero = manejadorAerolinea.obtenerAerolinea(aerolinea);
        if (aero != null) {
            RutaVuelo r = new RutaVuelo(nombre, aero, origen, destino, costoTurista, costoEjecutivo);
            manejadorRutaVuelo.agregarRutaVuelo(r);
            manejadorAerolinea.agregarRutaVueloAAerolinea(aerolinea, r);
        } else {
            throw new IllegalArgumentException("No existe Logica.Aerolinea con ese nickname");
        }
    }

    public List<RutaVuelo> listarRutasPorAerolinea(String nombreAerolinea) {
        return manejadorAerolinea.obtenerRutaVueloDeAerolinea(nombreAerolinea);
    }

    // --- VUELO ---
    // ProyectoAerolineas/src/Logica/Logica.Sistema.java

    public String altaVueloAux(String nombreAerolinea, String nombreRuta, String nombreVuelo, String fecha, int duracion, int asientosTurista, int asientosEjecutivo) {
        Aerolinea aerolinea = manejadorAerolinea.obtenerAerolinea(nombreAerolinea);
        if (aerolinea == null) {
            return "La aerolínea no existe.";
        }
        RutaVuelo ruta = manejadorRutaVuelo.getRuta(nombreRuta);
        if (ruta == null || !ruta.getAerolinea().equals(nombreAerolinea)) {
            return "La ruta no existe para la aerolínea seleccionada.";
        }
        if (manejadorVuelo.getVuelo(nombreVuelo) != null) {
            return "Ya existe un vuelo con ese nombre.";
        }
        Vuelo vuelo = new Vuelo(nombreVuelo, nombreRuta, fecha, duracion, asientosTurista, asientosEjecutivo);
        manejadorVuelo.agregarVuelo(vuelo);
        manejadorRutaVuelo.agregarVueloARuta(nombreRuta, vuelo);
        return "Logica.Vuelo dado de alta correctamente.";
    }

    @Override
    public boolean altaVuelo(String nombreVuelo, String nombreRuta, String fecha, int duracion, int asientosTurista, int asientosEjecutivo) {
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
            throw new IllegalArgumentException("Logica.Vuelo no encontrado");
        }
        return vuelo; // Asumiendo que Logica.Vuelo tiene métodos para obtener reservas y datos
    }

    // --- PAQUETES ---
    @Override
    public void altaPaquete(String nombre, String descripcion, double costo, LocalDate fechaAlta, int descuentoPorc, int periodoValidezDias) {
        Paquete p = new Paquete(nombre, descripcion, costo, fechaAlta, descuentoPorc, periodoValidezDias);
        manejadorPaquete.agregarPaquete(p);
    }

    @Override
    public List<Paquete> listarPaquetes() {
        return manejadorPaquete.getPaquetes();
    }

}
