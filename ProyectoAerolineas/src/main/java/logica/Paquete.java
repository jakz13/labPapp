package logica;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un paquete de rutas que puede ser vendido a clientes.
 * Contiene ítems, descuento y lógica para calcular su costo.
 */
@Entity
@Table(name = "paquetes")
public class Paquete {

    /** Nombre del paquete usado como clave primaria. */
    @Id
    private String nombre; // usamos el nombre como PK

    /** Descripción detallada del paquete. */
    @Column(nullable = false)
    private String descripcion;

    /** Costo actual calculado del paquete. */
    private double costo;

    /** Fecha en la que se dio de alta el paquete. */
    private LocalDate fechaAlta;

    /** Porcentaje de descuento aplicado al paquete (0-100). */
    private int descuentoPorc;

    /** Periodo de validez del paquete en días. */
    private int periodoValidezDias;

    /** Lista de ítems (rutas) incluidos en el paquete. */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "paquete_id") // FK en item_paquete
    private List<ItemPaquete> itemPaquetes = new ArrayList<>();

    /** Compras realizadas de este paquete. */
    @OneToMany(mappedBy = "paquete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraPaqLogica> compras = new ArrayList<>();

    public Paquete() {
    }

    /**
     * Crea un paquete con datos básicos.
     * @param nombre identificador del paquete
     * @param descripcion texto descriptivo
     * @param descuentoPorc porcentaje de descuento
     * @param periodoValidezDias días de validez
     */
    public Paquete(String nombre, String descripcion, int descuentoPorc, int periodoValidezDias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = 0.0;
        this.fechaAlta = LocalDate.now();
        this.descuentoPorc = descuentoPorc;
        this.periodoValidezDias = periodoValidezDias;
    }

    /** Establece el costo manualmente. */
    public void setCosto(double costo) { this.costo = costo; }

    /** Devuelve el nombre del paquete. */
    public String getNombre(){ return nombre; }
    /** Devuelve la descripción del paquete. */
    public String getDescripcion() { return descripcion; }
    /** Calcula el costo total aplicando descuentos según los items. */
    public double getCosto() {
        double costoTotal = 0;
        for (ItemPaquete item : this.getItemPaquetes()) {
            double costoRuta = item.getTipoAsiento() == TipoAsiento.TURISTA
                    ? item.getRutaVuelo().getCostoTurista()
                    : item.getRutaVuelo().getCostoEjecutivo();
            costoTotal += costoRuta * item.getCantAsientos();
        }
        return costoTotal * (1 - this.getDescuentoPorc() / 100.0);
    }

    /** Devuelve la fecha de alta del paquete. */
    public LocalDate getFechaAlta() { return fechaAlta; }
    /** Devuelve el porcentaje de descuento. */
    public int getDescuentoPorc() { return descuentoPorc; }
    /** Devuelve el periodo de validez en días. */
    public int getPeriodoValidezDias() { return periodoValidezDias; }
    /** Devuelve los ítems incluidos en el paquete. */
    public List<ItemPaquete> getItemPaquetes() { return itemPaquetes; }
    /** Devuelve las compras asociadas a este paquete. */
    public List<CompraPaqLogica> getCompras() { return compras; }

    /** Devuelve una representación corta del descuento. */
    public String getDescuento() {
        if (descuentoPorc > 0) {
            return "Descuento: " + descuentoPorc + "%";
        } else {
            return "Sin descuento";
        }
    }

    /** Devuelve una representación corta del periodo de validez. */
    public String getPeriodoValidez() {
        if (periodoValidezDias > 0) {
            return "Periodo de validez: " + periodoValidezDias + " días";
        } else {
            return "Sin periodo de validez";
        }
    }

    /** Indica si el paquete ya fue comprado alguna vez. */
    public boolean estaComprado() {
        return !compras.isEmpty();
    }

    /** Recalcula y devuelve el costo del paquete según sus items y descuento. */
    public double calcularCostoReservaPaquete() {
        double costoTotal = 0.0;

        for (ItemPaquete item : itemPaquetes) {
            costoTotal += item.calcularCostoItem();
        }

        if (descuentoPorc > 0) {
            double descuento = costoTotal * (descuentoPorc / 100.0);
            costoTotal -= descuento;
        }

        this.costo = costoTotal;
        return costoTotal;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
