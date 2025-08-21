package Logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente extends Usuario {
    private String apellido;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String tipoDocumento;
    private String numeroDocumento;
    private List<Reserva> reservas;
    private List<Paquete> paquetesComprados;

    public Cliente(String nickname, String nombre, String email, String apellido, LocalDate fechaNac, String nacionalidad, String tipoDoc, String numDoc) {
        super(nickname, nombre, email);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNac;
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipoDoc;
        this.numeroDocumento = numDoc;
        this.reservas = new ArrayList<>();
        this.paquetesComprados = new ArrayList<>();
    }

    public Cliente(String nickname, String nombre, String apellido, String correo) {
        super(nickname, nombre, correo);
        this.apellido = apellido;
        this.fechaNacimiento = LocalDate.now(); // Default to today if not provided
        this.nacionalidad = "Desconocida"; // Default value
        this.tipoDocumento = "Desconocido"; // Default value
        this.numeroDocumento = "00000000"; // Default value
        this.reservas = new ArrayList<>();
        this.paquetesComprados = new ArrayList<>();
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public List<Object> getPaquetesComprados() {
        return Collections.singletonList(paquetesComprados);
    }
}
