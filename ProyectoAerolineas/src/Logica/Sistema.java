

import java.util.ArrayList;
import java.util.List;

public class Sistema implements ISistema {

    private ManejadorCliente manejadorCliente;
    private ManejadorAerolinea manejadorAerolinea;
    private ManejadorCiudad manejadorCiudad;
    private ManejadorRutaVuelo manejadorRutaVuelo;
    private ManejadorVuelo manejadorVuelo;
    private ManejadorPaquete manejadorPaquete;

    public Sistema() {
        this.manejadorCliente = new ManejadorCliente();
        this.manejadorAerolinea = new ManejadorAerolinea();
        this.manejadorCiudad = new ManejadorCiudad();
        this.manejadorRutaVuelo = new ManejadorRutaVuelo();
        this.manejadorVuelo = new ManejadorVuelo();
        this.manejadorPaquete = new ManejadorPaquete();
    }

    // --- USUARIOS ---
    @Override
    public void altaCliente(String nickname, String nombre, String apellido, String correo) {
        Cliente c = new Cliente(nickname, nombre, apellido, correo);
        manejadorCliente.agregarCliente(c);
    }

    @Override
    public void altaAerolinea(String nickname, String nombre, String descripcion, String correo) {
        Aerolinea a = new Aerolinea(nickname, nombre, descripcion, correo);
        manejadorAerolinea.agregarAerolinea(a);
    }

    @Override
    public List<Cliente> listarClientes() {
        return manejadorCliente.getClientes();
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

    // --- RUTAS DE VUELO ---
    @Override
    public void altaRutaVuelo(String nombre, String aerolinea, String origen, String destino, double costoTurista, double costoEjecutivo) {
        RutaVuelo r = new RutaVuelo(nombre, aerolinea, origen, destino, costoTurista, costoEjecutivo);
        manejadorRutaVuelo.agregarRuta(r);
    }

    @Override
    public void altaVuelo(String nombreRuta, String nombreVuelo, String fecha, int asientosTurista, int asientosEjecutivo) {
        Vuelo v = new Vuelo(nombreVuelo, nombreRuta, fecha, asientosTurista, asientosEjecutivo);
        manejadorVuelo.agregarVuelo(v);
    }

    // --- PAQUETES ---
    @Override
    public void crearPaquete(String nombre, String descripcion, int validezDias, double descuento) {
        Paquete p = new Paquete(nombre, descripcion, validezDias, descuento);
        manejadorPaquete.agregarPaquete(p);
    }
}
