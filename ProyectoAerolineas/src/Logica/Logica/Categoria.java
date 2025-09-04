package Logica;


public class Categoria {

    private String nombre; // clave primaria


    public Categoria() {}


    public Categoria(String nombre) {
        this.nombre = nombre;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
