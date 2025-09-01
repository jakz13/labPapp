public class ItemPaquete {
    private RutaVuelo rutaVuelo;
    private int cantAsientos;
    private TipoAsiento tipoAsiento;

    public ItemPaquete(RutaVuelo rutaVuelo, int cantidad, TipoAsiento tipoAsiento) {
        this.rutaVuelo = rutaVuelo;
        this.cantAsientos= cantidad;
        this.tipoAsiento = tipoAsiento;
    }
    public RutaVuelo getRutaVuelo() { return rutaVuelo; }
    public int getCantAsientos() { return cantAsientos; }
    public TipoAsiento getTipoAsiento() { return tipoAsiento; }

    public void incrementarCantidad(int cantidad) {
        this.cantAsientos += cantidad;
    }
}
