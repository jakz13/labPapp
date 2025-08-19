

public class ItemPaquete {
    private RutaVuelo rutaVuelo;
    private int cantidad;
    private String tipoAsiento;

    public ItemPaquete(RutaVuelo rutaVuelo, int cantidad, String tipoAsiento) {
        this.rutaVuelo = rutaVuelo;
        this.cantidad = cantidad;
        this.tipoAsiento = tipoAsiento;
    }
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }
    public int getCantidad() { return cantidad; }
    public String getTipoAsiento() { return tipoAsiento; }
}
