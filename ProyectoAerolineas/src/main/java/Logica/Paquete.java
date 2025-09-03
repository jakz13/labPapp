package Logica;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paquetes")
public class Paquete {

    @Id
    @Column(nullable = false, length = 100)
    private String nombre; // ahora es la PK

    private String descripcion;

    @Column(nullable = false)
    private double costo;

    @Column(name = "fecha_alta")
    private LocalDate fechaAlta;

    private int descuentoPorc;

    private int periodoValidezDias;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "paquete_nombre") // FK en ItemPaquete
    private List<ItemPaquete> itemPaquetes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "paquete_nombre") // FK en CompraPaqLogica
    private List<CompraPaqLogica> compras = new ArrayList<>();

    public Paquete() {
        this.fechaAlta = LocalDate.now();
    }

    public Paquete(String nombre, String descripcion, double costo, int descuentoPorc, int periodoValidezDias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.fechaAlta = LocalDate.now();
        this.descuentoPorc = descuentoPorc;
        this.periodoValidezDias = periodoValidezDias;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getCosto() { return costo; }
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

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCosto(double costo) { this.costo = costo; }
    public void setDescuentoPorc(int descuentoPorc) { this.descuentoPorc = descuentoPorc; }
    public void setPeriodoValidezDias(int periodoValidezDias) { this.periodoValidezDias = periodoValidezDias; }
    public void setItemPaquetes(List<ItemPaquete> itemPaquetes) { this.itemPaquetes = itemPaquetes; }
    public void setCompras(List<CompraPaqLogica> compras) { this.compras = compras; }
}

