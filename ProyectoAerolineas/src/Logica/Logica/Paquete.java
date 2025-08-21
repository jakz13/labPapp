package Logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paquete {
    private String nombre;
    private String descripcion;
    private double costo;
    private LocalDate fechaAlta;
    private int descuentoPorc;
    private int periodoValidezDias;
    private List<ItemPaquete> ItemPaquetes;

    public Paquete(String nombre, String descripcion, double costo, LocalDate fechaAlta, int descuentoPorc, int periodoValidezDias){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.fechaAlta = LocalDate.now();
        this.descuentoPorc = descuentoPorc;
        this.periodoValidezDias = periodoValidezDias;
        this.ItemPaquetes = new ArrayList<>();
    }

    public String getNombre(){ return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getCosto() { return costo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public int getDescuentoPorc() { return descuentoPorc; }
    public int getPeriodoValidezDias() { return periodoValidezDias; }
    public List<ItemPaquete> getItemPaquetes() { return ItemPaquetes; }

    public String getDescuento() {
        if (descuentoPorc > 0) {
            return "Descuento: " + descuentoPorc + "%";
        } else {
            return "Sin descuento";
        }
    }

    public String getPeriodoValidez() {
        if (periodoValidezDias > 0) {
            return "Periodo de validez: " + periodoValidezDias + " días";
        } else {
            return "Sin periodo de validez";
        }
    }
}
