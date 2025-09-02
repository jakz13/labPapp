package Logica;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ItemPaquete {
    private final int cantidad;
    private RutaVuelo rutaVuelo;
    private int cantAsientos;
    private String tipoAsiento;

    public ItemPaquete(RutaVuelo rutaVuelo, int cantidad, String tipoAsiento) {
        this.rutaVuelo = rutaVuelo;
        this.cantidad = cantidad;
        this.tipoAsiento = tipoAsiento;
    }



    public RutaVuelo getRutaVuelo() {
        return rutaVuelo;
    }

    public int getCantAsientos() {
        return cantAsientos;
    }

    public String getTipoAsiento() {
        return tipoAsiento;
    }

    public int getCantidad() {
        return cantidad;
    }
}
