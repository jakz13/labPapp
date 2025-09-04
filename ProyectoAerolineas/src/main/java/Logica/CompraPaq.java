package Logica;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "compra_paquete")
public class CompraPaq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "paquete_id", nullable = false)
    private Paquete paquete;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDate fechaCompra;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVenc;

    @Column(nullable = false)
    private double costo;

    public CompraPaq() {}

    public CompraPaq(Cliente cliente, Paquete paquete, LocalDate fechaCompra, int validezDias, double costo) {
        this.cliente = cliente;
        this.paquete = paquete;
        this.fechaCompra = fechaCompra;
        this.fechaVenc = fechaCompra.plusDays(validezDias);
        this.costo = costo;
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Paquete getPaquete() { return paquete; }
    public LocalDate getFechaCompra() { return fechaCompra; }
    public LocalDate getFechaVenc() { return fechaVenc; }
    public double getCosto() { return costo; }

    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setPaquete(Paquete paquete) { this.paquete = paquete; }
    public void setFechaCompra(LocalDate fechaCompra) { this.fechaCompra = fechaCompra; }
    public void setValidezDias(int validezDias) { this.fechaVenc = fechaCompra.plusDays(validezDias); }
    public void setCosto(double costo) { this.costo = costo; }
}


