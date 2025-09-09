package Logica;

import jakarta.persistence.*;

@Entity
@Table(name = "items_paquete")
public class ItemPaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paquete_id")
    private Paquete paquete;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ruta_vuelo_id")
    private RutaVuelo rutaVuelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_asiento", nullable = false)
    private TipoAsiento tipoAsiento;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    public ItemPaquete() {}

    public ItemPaquete(Paquete paquete, RutaVuelo rutaVuelo, int cantidad, TipoAsiento tipoAsiento) {
        this.paquete = paquete;
        this.rutaVuelo = rutaVuelo;
        this.cantidad = cantidad;
        this.tipoAsiento = tipoAsiento;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public Paquete getPaquete() { return paquete; }
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }
    public int getCantidad() { return cantidad; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }

    // --- Setters ---
    public void setPaquete(Paquete paquete) { this.paquete = paquete; }
    public void setRutaVuelo(RutaVuelo rutaVuelo) { this.rutaVuelo = rutaVuelo; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setTipoAsiento(TipoAsiento tipoAsiento) { this.tipoAsiento = tipoAsiento; }
    public int getCantAsientos() {
        return cantidad;
    }
    // --- Lógica de negocio ---
    public void incrementarCantidad(int cantidad) {
        this.cantidad += cantidad;
    }
}
