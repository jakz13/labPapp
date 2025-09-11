package DataTypes;

public class DtItemPaquete {
    private DtRutaVuelo rutaVueloId; // referencia a la ruta de vuelo
    private int cantAsientos;
    private String tipoAsiento; // en texto para facilitar transporte

    public DtItemPaquete() {
    }

    public DtItemPaquete(Long id,DtRutaVuelo rutaVueloId, int cantAsientos, String tipoAsiento) {
        this.rutaVueloId = rutaVueloId;
        this.cantAsientos = cantAsientos;
        this.tipoAsiento = tipoAsiento;
    }

    // --- Getters y Setters ---


    public DtRutaVuelo getRutaVuelo() {
        return rutaVueloId;
    }

    public void setRutaVueloId(DtRutaVuelo rutaVueloId) {
        this.rutaVueloId = rutaVueloId;
    }

    public int getCantAsientos() {
        return cantAsientos;
    }

    public void setCantAsientos(int cantAsientos) {
        this.cantAsientos = cantAsientos;
    }

    public String getTipoAsiento() {
        return tipoAsiento;
    }

    public void setTipoAsiento(String tipoAsiento) {
        this.tipoAsiento = tipoAsiento;
    }

    @Override
    public String toString() {
        return "DtItemPaquete{" +
                ", rutaVueloId=" + rutaVueloId +
                ", cantAsientos=" + cantAsientos +
                ", tipoAsiento='" + tipoAsiento + '\'' +
                '}';
    }
}
