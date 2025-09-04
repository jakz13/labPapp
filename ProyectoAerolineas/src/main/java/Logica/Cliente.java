package Logica;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "clientes")
public class Cliente extends Usuario {

    @Column(nullable = false)
    private String apellido;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String nacionalidad;

    @Enumerated(EnumType.STRING)  // si TipoDoc es un enum
    private TipoDoc tipoDocumento;

    @Column(name = "numero_documento", unique = true)
    private String numeroDocumento;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<String, Reserva> reservas = new HashMap<>();

    @ManyToMany
    @JoinTable(
            name = "cliente_paquete",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "paquete_id")
    )
    private List<Paquete> paquetesComprados = new ArrayList<>();

    public Cliente() {}

    public Cliente(String nickname, String nombre, String email, String apellido, LocalDate fechaNac,
                   String nacionalidad, TipoDoc tipoDoc, String numDoc) {
        super(nickname, nombre, email);
        this.apellido = apellido;
        this.fechaNacimiento = fechaNac;
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipoDoc;
        this.numeroDocumento = numDoc;
    }


    public String getApellido() { return apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getNacionalidad() { return nacionalidad; }
    public TipoDoc getTipoDocumento() { return tipoDocumento; }
    public String getNumeroDocumento() { return numeroDocumento; }
    public Map<String, Reserva> getReservas() { return reservas; }
    public List<Paquete> getPaquetesComprados() { return paquetesComprados; }
    public void agregarReserva(String idReserva, Reserva reserva) {
        reservas.put(idReserva, reserva);
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
    public void setFechaNacimiento(LocalDate fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}
    public void setPaquetesComprados(List<Paquete> paquetesComprados) {this.paquetesComprados = paquetesComprados;}
}
