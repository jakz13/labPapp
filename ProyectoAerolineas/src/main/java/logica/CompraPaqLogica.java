package logica;

//import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;



/**
 * Representa la compra de un paquete realizada por un cliente.
 * Contiene información sobre fechas, validez y costo.
 */
@Entity
@Table(name = "compras_paquetes")
public class CompraPaqLogica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompraPaq;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paquete_id")
    private Paquete paquete;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVenc;

    private double costo;

    public CompraPaqLogica() { }

    /**
     * Crea una compra de paquete con validez y costo.
     */
    public CompraPaqLogica(Cliente cliente, Paquete paquete, LocalDate fechaCompra, int validezDias, double costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }
        this.cliente = cliente;
        this.paquete = paquete;
        this.fechaCompra = fechaCompra;
        this.fechaVenc = fechaCompra.plusDays(validezDias);
        this.costo = costo;
    }

    // --- Getters ---
    /** Devuelve el id de la compra. */
    public Long getId() { return idCompraPaq; }
    /** Devuelve el cliente que realizó la compra. */
    public Cliente getCliente() { return cliente; }
    /** Devuelve el paquete comprado. */
    public Paquete getPaquete() { return paquete; }
    /** Devuelve la fecha de compra. */
    public LocalDate getFechaCompra() { return fechaCompra; }
    /** Devuelve la fecha de vencimiento de la compra. */
    public LocalDate getFechaVenc() { return fechaVenc; }
    /** Devuelve el costo pagado. */
    public double getCosto() { return costo; }

    // --- Setters ---
    /** Actualiza el cliente de la compra. */
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    /** Actualiza el paquete de la compra. */
    public void setPaquete(Paquete paquete) { this.paquete = paquete; }
    /** Actualiza la fecha de compra. */
    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }
    /** Actualiza la fecha de vencimiento. */
    public void setFechaVenc(LocalDate fechaVenc) { this.fechaVenc = fechaVenc; }
    /** Actualiza el costo (no negativo). */
    public void setCosto(double costo) {
        if (costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
        }
        this.costo = costo;
    }
}
