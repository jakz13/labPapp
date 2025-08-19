public class Fabrica {
    private static Fabrica instancia = null;

    private Fabrica() {
    }

    public static Fabrica getInstance() {
        if (instancia == null) {
            instancia = new Fabrica();
        }

        return instancia;
    }


}
