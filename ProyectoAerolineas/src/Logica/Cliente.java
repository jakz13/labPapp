import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cliente extends Usuario {
    private String apellido;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private String tipoDocumento;
    private String numeroDocumento;
    private Map<String, Reserva> reservas;
    private List<Paquete> paquetesComprados;

    public Cliente(String nickname, String nombre, String email, String apellido, LocalDate fechaNac, String nacionalidad, String tipoDoc, String numDoc) {
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
    public String getTipoDocumento() { return tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public Map<String, Reserva> getReservas() { return reservas; }
    public List<Object> getPaquetesComprados() { return Collections.singletonList(paquetesComprados); }

    public void agregarReserva(String idReserva, Reserva reserva) {
        reservas.put(idReserva, reserva);
    }
}
