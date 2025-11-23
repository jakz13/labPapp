package ServiciosWeb;

public class Publicador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        WebServices p = new WebServices();
        String portArg = null;
        if (args != null && args.length > 0 && args[0] != null && !args[0].isEmpty()) {
            portArg = args[0];
        }
        // Si portArg es null, WebServices.publicar() usará la propiedad -Dws.port o la variable WS_PORT
        if (portArg != null) {
            p.publicar(portArg);
        } else {
            p.publicar();
        }
    }

}
