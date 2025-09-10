package Logica;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cliente extends Usuario {
    private String apellido;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private TipoDoc tipoDocumento;
    private String numeroDocumento;
    private Map<String, Reserva> reservas;
    private List<Paquete> paquetesComprados;


    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setTipoDocumento(TipoDoc tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public void setReservas(Map<String, Reserva> reservas) {
        this.reservas = reservas;
    }

    public void setPaquetesComprados(List<Paquete> paquetesComprados) {
        this.paquetesComprados = paquetesComprados;
    }

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


    public String getApellido() { return apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public TipoDoc getTipoDocumento() { return tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public Map<String, Reserva> getReservas() { return reservas; }
    public List< Paquete > getPaquetesComprados() { return paquetesComprados; }
    public void agregarReserva(String idReserva, Reserva reserva) {
        reservas.put(idReserva, reserva);
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
}
