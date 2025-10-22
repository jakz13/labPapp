package DataTypes;

import logica.TipoDoc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DtCliente extends DtUsuario {
    private String apellido;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private TipoDoc tipoDocumento;
    private String numeroDocumento;
    private List<DtReserva> reservas = new ArrayList<>();
    private List<DtPaquete> paquetesComprados = new ArrayList<>();

    public DtCliente() {
    }

    public DtCliente(String nickname, String nombre, String email, String imagenUrl,
                     String apellido, LocalDate fechaNacimiento,
                     String nacionalidad, TipoDoc tipoDocumento,
                     String numeroDocumento, LocalDate fechaAlta,List<DtReserva> reservas, List<DtPaquete> paquetesComprados) {
        super(nickname, nombre, email, imagenUrl, fechaAlta);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.reservas = reservas;
        this.paquetesComprados = paquetesComprados;
    }

    // --- Getters y Setters ---
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public TipoDoc getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDoc tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public List<DtReserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<DtReserva> reservas) {
        this.reservas = reservas;
    }

    public List<DtPaquete> getPaquetesComprados() {
        return paquetesComprados;
    }

    public void setPaquetesComprados(List<DtPaquete> paquetesComprados) {
        this.paquetesComprados = paquetesComprados;
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
}