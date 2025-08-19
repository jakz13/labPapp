public class Paquete {
    private String nombre;
    private String descripcion;
    private double costo;

    public Paquete(String nombre, String descripcion, double costo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
    }
    public Paquete(String nombre, String descripcion, double costo) {
        this(nombre, descripcion, costo, 0.0);
    }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getCosto() { return costo; }
}
