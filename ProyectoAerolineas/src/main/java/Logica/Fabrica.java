package Logica;

public class Fabrica {
    private static Fabrica instancia = null;
    private ISistema sistema;

    private Fabrica() {
        sistema = new Sistema(); // Asumiendo que Logica.Sistema implementa Logica.ISistema
    }

    public static Fabrica getInstance() {
        if (instancia == null) {
            instancia = new Fabrica();
        }
        return instancia;
    }

    public ISistema getISistema() {
        return sistema;
    }
}
