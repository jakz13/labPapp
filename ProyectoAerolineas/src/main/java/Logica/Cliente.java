package Logica;

import DataTypes.DtPaquete;
import DataTypes.DtReserva;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "usuario_id") // referencia a la PK de Usuario
public class Cliente extends Usuario {

    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String nacionalidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDoc tipoDocumento;

    @Column(name = "numero_documento", unique = true)
    private String numeroDocumento;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cliente_nickname") // FK en tabla Reserva
    private List<Reserva> reservas = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "cliente_paquete",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "paquete_id")
    )
    private List<Paquete> paquetesComprados = new ArrayList<>();

    public Cliente() {
    }

    public Cliente(String nickname, String nombre, String apellido,
                   String email, LocalDate fechaNac, String nacionalidad,
                   TipoDoc tipoDoc, String numDoc) {
        super(nickname, nombre, email);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNac;
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipoDoc;
        this.numeroDocumento = numDoc;
    }

    // --- Getters y Setters ---
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public TipoDoc getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDoc tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }

    public List<Paquete> getPaquetesComprados() { return paquetesComprados; }
    public void setPaquetesComprados(List<Paquete> paquetesComprados) { this.paquetesComprados = paquetesComprados; }

    public void agregarReserva(Reserva reserva) {
        reservas.add(reserva);
    }
    @Override
    public String toString() {
        return this.getNombre();
    }

    public List<DtReserva> getDtReservas() {
        List<DtReserva> dtReservas = new ArrayList<>();
        for (Reserva r : reservas) {
            dtReservas.add(new DtReserva(r.getId(), r.getFecha(), r.getCosto(), r.getTipoAsiento(), r.getCantidadPasajes(), r.getUnidadesEquipajeExtra(), r.getDtPasajeros(), r.getVuelo() != null ? r.getVuelo().getNombre() : "N/A"));
        }
        return dtReservas;
    }

    public List<DtPaquete> getDtPaquetesComprados() {
        List<DtPaquete> dtPaquetes = new ArrayList<>();
        for (Paquete p : paquetesComprados) {
            dtPaquetes.add(new DtPaquete(p));
        }
        return dtPaquetes;
    }
}
