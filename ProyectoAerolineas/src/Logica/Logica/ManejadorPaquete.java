package Logica;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Manejador de paquetes de vuelo
public class ManejadorPaquete {
    // Métodos de manejo
    private Map <String, Paquete> paquetes;
    private static ManejadorPaquete instancia = null;

    private ManejadorPaquete() {
        // Inicialización de la colección de paquetes
        paquetes = new HashMap<String, Paquete>();
    }

    public static ManejadorPaquete getInstance() {
        if (instancia == null) {
            instancia = new ManejadorPaquete();
        }
        return instancia;
    }

    public void crearPaquete(Paquete p) {
        String nombre = p.getNombre();
        if (this.obtenerPaquete(nombre) == null) {
            paquetes.put(nombre, p);
        } else {
            throw new IllegalArgumentException("El paquete con el nombre " + nombre + " ya existe.");
        }
    }

    public Paquete obtenerPaquete(String nombre) {
        return ((Paquete) paquetes.get(nombre));
    }

/*
    public void comprarPaquete(CompraPaquete compra) {
        // Lógica para procesar la compra de un paquete
    }

    public void comprarVuelo(CompraVuelo compra) {
        // Lógica para procesar la compra de un vuelo
    }
*/
    public Paquete buscarPaquete(String nombre) {return ((Paquete) paquetes.get(nombre));}

    public void agregarRutaPaquete(Paquete p, RutaVuelo ruta, int cantidadAsientos, TipoAsiento tipoAsiento) {
        ItemPaquete existente = null;
        for (ItemPaquete item : p.getItemPaquetes()){
            if (item.getRutaVuelo().equals(ruta) && item.getTipoAsiento() == tipoAsiento) {
                existente = item;
                break;
            }
        }
        if (existente != null) {
            existente.incrementarCantidad(cantidadAsientos);
        } else {
            ItemPaquete nuevo = new ItemPaquete(ruta, cantidadAsientos, tipoAsiento);
            p.getItemPaquetes().add(nuevo);
        }
    }

    public void agregarPaquete(Paquete p) {

    }

    public List<Paquete> getPaquetes() {
        return new ArrayList<Paquete>(paquetes.values());
    }

    public List<Paquete> getPaquetesDisp() {
        List<Paquete> disponibles = new ArrayList<>();
        for (Paquete p : paquetes.values()) {
            if (!p.estaComprado()) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }

}

