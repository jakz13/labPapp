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
    private List<ItemPaquete> ItemP;
    private List<CompraPaqLogica> Compras;

    public Paquete(String nombre, String descripcion, double costo, LocalDate fechaAlta, int descuentoPorc, int periodoValidezDias){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        //this.fechaAlta = LocalDate.now();
        this.fechaAlta = fechaAlta;
        this.descuentoPorc = descuentoPorc;
        this.periodoValidezDias = periodoValidezDias;
        this.ItemP = new ArrayList<>();
        this.Compras = new ArrayList<>();
    }

    public String getNombre(){ return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getCosto() { return costo; }
    public LocalDate getFechaAlta() { return fechaAlta; }
    public int getDescuentoPorc() { return descuentoPorc; }
    public int getPeriodoValidezDias() { return periodoValidezDias; }
    public List<ItemPaquete> getItemPaquetes() { return ItemP; }
    public List<CompraPaqLogica> getCompras() { return Compras; }

    public boolean estaComprado() {
        return !Compras.isEmpty();
    }

    public void registrarCompra(CompraPaqLogica compra) {
        this.Compras.add(compra);
    }
}
