package logica;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;


/**
 * Representa un elemento dentro de un paquete (ruta, cantidad y tipo de asiento).
 * Permite calcular su costo y ajustar la cantidad de asientos.
 */
@Entity
@Table(name = "item_paquete")
public class ItemPaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemPaquete;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ruta_vuelo_id", nullable = false)
    private RutaVuelo rutaVuelo;

    @Column(nullable = false)
    private int cantAsientos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAsiento tipoAsiento;

    public ItemPaquete() {
    }

    /** Crea un item con ruta, cantidad y tipo de asiento. */
    public ItemPaquete(RutaVuelo rutaVuelo, int cantAsientos, TipoAsiento tipoAsiento) {
        if (rutaVuelo == null) {
            throw new IllegalArgumentException("La ruta de vuelo del item paquete no puede ser null");
        }
        if (cantAsientos <= 0) {
            throw new IllegalArgumentException("La cantidad de asientos del item paquete debe ser mayor a cero");
        }
        if (tipoAsiento == null) {
            throw new IllegalArgumentException("El tipo de asiento del item paquete no puede ser null");
        }
        this.rutaVuelo = rutaVuelo;
        this.cantAsientos = cantAsientos;
        this.tipoAsiento = tipoAsiento;
    }

    /** Devuelve la ruta asociada al item. */
    public RutaVuelo getRutaVuelo() {
        return rutaVuelo;
    }

    /** Devuelve la cantidad de asientos del item. */
    public int getCantAsientos() {
        return cantAsientos;
    }

    /** Devuelve el tipo de asiento del item. */
    public TipoAsiento getTipoAsiento() {
        return tipoAsiento;
    }

    /** Incrementa la cantidad de asientos del item. */
    public void incrementarCantidad(int cantidad) {
        this.cantAsientos += cantidad;
    }

    /** Calcula y devuelve el costo total de este item según el tipo de asiento. */
    public double calcularCostoItem() {
        if (rutaVuelo == null) {
            throw new IllegalStateException("Ruta de vuelo no definida para este item");
        }

        double costoBase;
        if (tipoAsiento == TipoAsiento.TURISTA) {
            costoBase = rutaVuelo.getCostoTurista();
        } else {
            costoBase = rutaVuelo.getCostoEjecutivo();
        }

        return costoBase * cantAsientos;
    }
}
