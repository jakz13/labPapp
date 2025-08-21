package Logica;

public class Ciudad {
    private String nombre;
    private String pais;
    private String aeropuerto;
    private String descripcion;
    private String sitioWeb;
    private String fechaAlta;

    public Ciudad(String nombre, String pais, String aeropuerto, String descripcion, String sitioWeb, String fechaAlta) {
        this.nombre = nombre;
        this.pais = pais;
        this.aeropuerto = aeropuerto;
        this.descripcion = descripcion;
        this.sitioWeb = sitioWeb;
        this.fechaAlta = fechaAlta;
    }

    public Ciudad(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
        this.aeropuerto = "";
        this.descripcion = "";
        this.sitioWeb = "";
        this.fechaAlta = "";
    }

    public String getNombre() { return nombre; }
    public String getPais() { return pais; }
    public String getAeropuerto() { return aeropuerto; }
    public String getDescripcion() { return descripcion; }
    public String getSitioWeb() { return sitioWeb; }
    public String getFechaAlta() { return fechaAlta; }
}
