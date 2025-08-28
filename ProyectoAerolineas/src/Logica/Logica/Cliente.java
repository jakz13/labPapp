package Logica;

import java.time.LocalDate;
import java.util.*;

public class Cliente extends Usuario {
    private String apellido;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private TipoDoc tipoDocumento;
    private String numeroDocumento;
    private Map<String, Reserva> reservas;
    private List<Paquete> paquetesComprados;

    public Cliente(String nickname, String nombre, String email, String apellido, LocalDate fechaNac, String nacionalidad,TipoDoc tipoDoc, String numDoc) {
        super(nickname, nombre, email);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNac;
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipoDoc;
        this.numeroDocumento = numDoc;
        this.reservas = new HashMap<>();
        this.paquetesComprados = new java.util.ArrayList<>();
    }
    /*
    public Cliente(String nickname, String nombre, String apellido, String correo) {
        super(nickname, nombre, correo);
        this.apellido = apellido;
        this.fechaNacimiento = LocalDate.now(); // Default to today if not provided
        this.nacionalidad = "Desconocida"; // Default value
        this.tipoDocumento = ; // Default value
        this.numeroDocumento = "00000000"; // Default value
        this.reservas = new HashMap<>();
        this.paquetesComprados = new ArrayList<>();
    }
*/

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public TipoDoc getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public Map<String, Reserva>getReservas() {
        return reservas;
    }

    public List<Object> getPaquetesComprados() {
        return Collections.singletonList(paquetesComprados);
    }

    public void agregarReserva(String idReserva, Reserva reserva) {
        reservas.put(idReserva, reserva);
    }
}
