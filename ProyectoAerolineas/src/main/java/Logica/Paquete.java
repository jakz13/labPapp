package Logica;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paquetes")
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private double costo;
    private LocalDate fechaAlta;
    private int descuentoPorc;
    private int periodoValidezDias;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paquete_id")
    private List<ItemPaquete> itemPaquetes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paquete_id")
    private List<CompraPaqLogica> compras = new ArrayList<>();

    public Paquete() {} // constructor vacío para JPA

    public Paquete(String nombre, String descripcion, int descuentoPorc, int periodoValidezDias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = 0.0;
        this.fechaAlta = LocalDate.now();
        this.descuentoPorc = descuentoPorc;
        this.periodoValidezDias = periodoValidezDias;
    }

    // ======= Getters y Setters =======
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public int getDescuentoPorc() { return descuentoPorc; }
    public int getPeriodoValidezDias() { return periodoValidezDias; }

    public List<ItemPaquete> getItemPaquetes() { return itemPaquetes; }
    public List<CompraPaqLogica> getCompras() { return compras; }

    public String getDescuento() {
        return descuentoPorc > 0 ? "Descuento: " + descuentoPorc + "%" : "Sin descuento";
    }

    public String getPeriodoValidez() {
        return periodoValidezDias > 0 ? "Periodo de validez: " + periodoValidezDias + " días" : "Sin periodo de validez";
    }

    public boolean estaComprado() {
        return !compras.isEmpty();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
