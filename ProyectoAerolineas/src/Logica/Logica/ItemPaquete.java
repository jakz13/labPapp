package Logica;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ItemPaquete {
    //private final int cantidad;
    private RutaVuelo rutaVuelo;
    private int cantAsientos;
    private TipoAsiento tipoAsiento;

    public ItemPaquete(RutaVuelo rutaVuelo, int cantAsientos, TipoAsiento tipoAsiento) {
        this.rutaVuelo = rutaVuelo;
        this.cantAsientos = cantAsientos;
        this.tipoAsiento = tipoAsiento;
    }


    public RutaVuelo getRutaVuelo() {
        return rutaVuelo;
    }

    public int getCantAsientos() {
        return cantAsientos;
    }

    public TipoAsiento getTipoAsiento() {
        return tipoAsiento;
    }


    public void incrementarCantidad(int cantidad) {this.cantAsientos += cantidad;}

    public double calcularCostoItem() {
        if (rutaVuelo == null) {
            throw new IllegalStateException("Ruta de vuelo no definida para este item");
        }

        double costoBase;
        if (tipoAsiento == TipoAsiento.TURISTA) {
            costoBase = rutaVuelo.getCostoTurista(); // asumimos que RutaVuelo tiene este método
        } else {
            costoBase = rutaVuelo.getCostoEjecutivo(); // para otro tipo de asiento
        }

        // Costo total del ítem multiplicando por la cantidad de pasajes
        return costoBase * cantAsientos;
    }

}
