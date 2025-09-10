package DataTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DtCliente extends DtUsuario {
    private String apellido;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String tipoDocumento;   // lo paso a String para simplificar la transferencia
    private String numeroDocumento;
    private List<Long> reservasIds = new ArrayList<>(); // en DTO solo referencias, no objetos completos
    private List<Long> paquetesCompradosIds = new ArrayList<>();

    public DtCliente() {
    }

    public DtCliente(String nickname, String nombre, String email,
                     String apellido, LocalDate fechaNacimiento,
                     String nacionalidad, String tipoDocumento,
                     String numeroDocumento) {
        super(nickname, nombre, email);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
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

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public List<Long> getReservasIds() {
        return reservasIds;
    }

    public void setReservasIds(List<Long> reservasIds) {
        this.reservasIds = reservasIds;
    }

    public List<Long> getPaquetesCompradosIds() {
        return paquetesCompradosIds;
    }

    public void setPaquetesCompradosIds(List<Long> paquetesCompradosIds) {
        this.paquetesCompradosIds = paquetesCompradosIds;
    }

    @Override
    public String toString() {
        return "DtCliente{" +
                "nickname='" + getNickname() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", reservasIds=" + reservasIds +
                ", paquetesCompradosIds=" + paquetesCompradosIds +
                '}';
    }
}
