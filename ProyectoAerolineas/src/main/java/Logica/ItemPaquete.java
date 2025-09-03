package Logica;

import jakarta.persistence.*;

@Entity
@Table(name = "item_paquete")
public class ItemPaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paquete_id", nullable = false)
    private Paquete paquete;

    @ManyToOne
    @JoinColumn(name = "ruta_vuelo_id", nullable = false)
    private RutaVuelo rutaVuelo;

    @Column(nullable = false)
    private int cantidad;

    @Column(name = "cant_asientos")
    private int cantAsientos;

    @Enumerated(EnumType.STRING)
    private TipoAsiento tipoAsiento;

    public ItemPaquete() {}

    public ItemPaquete(Paquete paquete, RutaVuelo rutaVuelo, int cantidad, TipoAsiento tipoAsiento) {
        this.paquete = paquete;
        this.rutaVuelo = rutaVuelo;
        this.cantidad = cantidad;
        this.tipoAsiento = tipoAsiento;
        this.cantAsientos = 0;
    }

    public Long getId() { return id; }
    public Paquete getPaquete() { return paquete; }
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }
    public int getCantidad() { return cantidad; }
    public int getCantAsientos() { return cantAsientos; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }

    public void setPaquete(Paquete paquete) { this.paquete = paquete; }
    public void setRutaVuelo(RutaVuelo rutaVuelo) { this.rutaVuelo = rutaVuelo; }
    public void setCantAsientos(int cantAsientos) { this.cantAsientos = cantAsientos; }
    public void setTipoAsiento(TipoAsiento tipoAsiento) { this.tipoAsiento = tipoAsiento; }

    public void incrementarCantidad(int cantidad) {
        this.cantAsientos += cantidad;
    }
}

