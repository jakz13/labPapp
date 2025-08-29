import java.time.LocalDate;



public class CompraPaqLogica {
    private Cliente cliente;
    private Paquete paquete;
    private LocalDate fechaCompra;
    private LocalDate fechaVenc;
    private double costo;

    public CompraPaqLogica(Cliente cliente, Paquete paquete, LocalDate fechaCompra, int validezDias, double costo) {
        this.cliente = cliente;
        this.paquete = paquete;
        this.fechaCompra = fechaCompra;
        this.fechaVenc = fechaCompra.plusDays(validezDias);
        this.costo = costo;
    }

    public Cliente getCliente() { return cliente; }
    public Paquete getPaquete() { return paquete; }
    public LocalDate getFechaCompra() { return fechaCompra; }
    public LocalDate getFechaVenc() { return fechaVenc; }
    public double getCosto() { return costo; }

}

