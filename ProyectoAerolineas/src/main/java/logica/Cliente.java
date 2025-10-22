package logica;

import DataTypes.DtCliente;
import DataTypes.DtPaquete;
import DataTypes.DtReserva;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;


/**
 * Representa a un cliente del sistema con sus datos personales y reservas.
 */
@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "usuario_id")
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
    @JoinColumn(name = "cliente_nickname")
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraPaqLogica> comprasPaquetes = new ArrayList<>();

    public Cliente() {
        super();
    }

    /**
     * Crea un cliente con todos los datos requeridos.
     */
    public Cliente(String nickname, String nombre, String apellido,
                   String email, LocalDate fechaNac, String nacionalidad,
                   TipoDoc tipoDoc, String numDoc, String password, String imagenUrl) {
        super(nickname, nombre, email, password, LocalDate.now(), imagenUrl); //// Con contraseña
        if (nickname == null) {
            throw new IllegalArgumentException("El nickname del cliente no puede ser null");
        }
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre del cliente no puede ser null");
        }
        if (apellido == null) {
            throw new IllegalArgumentException("El apellido del cliente no puede ser null");
        }
        if (email == null) {
            throw new IllegalArgumentException("El emali del cliente no puede ser null");
        }
        if (fechaNac == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser null");
        }
        if (nacionalidad == null) {
            throw new IllegalArgumentException("La nacionalidad no puede ser null");
        }
        if (tipoDoc == null) {
            throw new IllegalArgumentException("El tipo de documento no puede ser null");
        }
        if (numDoc == null) {
            throw new IllegalArgumentException("El numero de documento no puede ser null");
        }
        if (password == null) {
            throw new IllegalArgumentException("La contraseña no puede ser null");
        }

        this.apellido = apellido;
        this.fechaNacimiento = fechaNac;
        this.nacionalidad = nacionalidad;
        this.tipoDocumento = tipoDoc;
        this.numeroDocumento = numDoc;
    }

    /** Devuelve un objeto de transferencia con los datos del cliente. */
    public DtCliente getDtCliente() {
        return new DtCliente(
                this.getNickname(),
                this.getNombre(),
                this.getEmail(),
                this.getImagenUrl(),
                this.apellido,
                this.fechaNacimiento,
                this.nacionalidad,
                this.tipoDocumento,
                this.numeroDocumento,
                this.getFechaAlta(),
                this.getDtReservas(),
                this.getDtPaquetesComprados()
        );
    }


    // --- Getters y Setters ---
    /** Devuelve el apellido del cliente. */
    public String getApellido() { return apellido; }
    /** Actualiza el apellido del cliente. */
    public void setApellido(String apellido) { this.apellido = apellido; }

    /** Devuelve la fecha de nacimiento. */
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    /** Actualiza la fecha de nacimiento. */
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    /** Devuelve la nacionalidad. */
    public String getNacionalidad() { return nacionalidad; }
    /** Actualiza la nacionalidad. */
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    /** Devuelve el tipo de documento. */
    public TipoDoc getTipoDocumento() { return tipoDocumento; }
    /** Actualiza el tipo de documento. */
    public void setTipoDocumento(TipoDoc tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    /** Devuelve el número de documento. */
    public String getNumeroDocumento() { return numeroDocumento; }
    /** Actualiza el número de documento. */
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    /** Devuelve la lista de reservas del cliente. */
    public List<Reserva> getReservas() { return reservas; }
    /** Reemplaza la lista de reservas del cliente. */
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }

    /** Devuelve las compras de paquetes realizadas por el cliente. */
    public List<CompraPaqLogica> getComprasPaquetes() { return comprasPaquetes; }
    /** Actualiza las compras de paquetes del cliente. */
    public void setComprasPaquetes(List<CompraPaqLogica> comprasPaquetes) { this.comprasPaquetes = comprasPaquetes; }

    /**
     * Agrega una reserva a la lista del cliente.
     */
    public void agregarReserva(Reserva reserva) {
        if(reserva == null) {
            throw new IllegalArgumentException("La ruta no puede ser null");
        }
        reservas.add(reserva);
    }

    @Override
    public String toString() {
        return this.getNombre();
    }

    /** Devuelve una lista de DTs con las reservas del cliente. */
    public List<DtReserva> getDtReservas() {
        List<DtReserva> dtReservas = new ArrayList<>();
        for (Reserva r : reservas) {
            dtReservas.add(new DtReserva(
                    r.getId(),
                    r.getFecha(),
                    r.getCosto(),
                    r.getTipoAsiento(),
                    r.getCantidadPasajes(),
                    r.getUnidadesEquipajeExtra(),
                    r.getDtPasajeros(),
                    r.getVuelo() != null ? r.getVuelo().getNombre() : "N/A"
            ));
        }
        return dtReservas;
    }

    /** Devuelve una lista de DTs con los paquetes comprados por el cliente. */
    public List<DtPaquete> getDtPaquetesComprados() {
        List<DtPaquete> dtPaquetes = new ArrayList<>();
        for (CompraPaqLogica compra : comprasPaquetes) {
            dtPaquetes.add(new DtPaquete(compra.getPaquete()));
        }
        return dtPaquetes;
    }
}