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
    private List<CompraPaqLogica> Compras;

    public Paquete(String nombre, String descripcion, /*LocalDate fechaAlta,*/ int descuentoPorc, int periodoValidezDias){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = 0.0;
        this.fechaAlta = LocalDate.now();
        this.descuentoPorc = descuentoPorc;
        this.periodoValidezDias = periodoValidezDias;
        this.ItemPaquetes = new ArrayList<>();
        this.Compras = new ArrayList<>();
    }

    public void setCosto(double costo) { this.costo = costo; }

    public String getNombre(){ return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getCosto() { return costo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public int getDescuentoPorc() { return descuentoPorc; }
    public int getPeriodoValidezDias() { return periodoValidezDias; }
    public List<ItemPaquete> getItemPaquetes() { return ItemPaquetes; }
    public List<CompraPaqLogica> getCompras() { return Compras; }

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
    public boolean estaComprado() {
        return !Compras.isEmpty();
    }

    public double calcularCostoReservaPaquete() {
        double costoTotal = 0.0;

        // Sumar el costo de cada item del paquete
        for (ItemPaquete item : ItemPaquetes) {
            costoTotal += item.calcularCostoItem();
        }

        // Aplicar descuento del paquete si corresponde
        if (descuentoPorc > 0) {
            double descuento = costoTotal * (descuentoPorc / 100.0);
            costoTotal -= descuento;
        }

        // Aquí podrías sumar costos extras si los hubiera
        // costoTotal += calcularCostosExtras();

        this.costo = costoTotal; // actualizar atributo de la clase

        return costoTotal;
    }



    @Override
    public String toString() {
        return this.nombre;
    }

}
