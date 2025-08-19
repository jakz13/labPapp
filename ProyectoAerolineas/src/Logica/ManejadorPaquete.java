// Manejador de paquetes de vuelo

public class ManejadorPaquete {
    private static ManejadorPaquete instancia = null;

    ManejadorPaquete() {
        // Inicialización de la colección de paquetes


    }

    public static ManejadorPaquete getInstance() {
        if (instancia == null) {
            instancia = new ManejadorPaquete();
        }
        return instancia;
    }

    public void comprarPaquete(CompraPaquete compra) {
        // Lógica para procesar la compra de un paquete
    }

    public void comprarVuelo(CompraVuelo compra) {
        // Lógica para procesar la compra de un vuelo
    }

    public void agregarPaquete(Paquete p) {

    }
}
