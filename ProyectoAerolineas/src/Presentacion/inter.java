

import java.util.List;
import java.util.Scanner;

public class inter {

    public static void main(String[] args) {

        ISistema sistema = Fabrica.getInstance().getISistema();
        Scanner sc = new Scanner(System.in);

        int opcion = 0;
        do {
            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Alta Cliente");
            System.out.println("2. Alta Aerolinea");
            System.out.println("3. Listar Clientes");
            System.out.println("4. Listar Aerolineas");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> {
                    System.out.print("Nickname: ");
                    String nick = sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = sc.nextLine();
                    System.out.print("Correo: ");
                    String correo = sc.nextLine();
                    sistema.altaCliente(nick, nombre, apellido, correo);
                }
                case 2 -> {
                    System.out.print("Nickname: ");
                    String nick = sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Descripcion: ");
                    String desc = sc.nextLine();
                    System.out.print("Correo: ");
                    String correo = sc.nextLine();
                    sistema.altaAerolinea(nick, nombre, desc, correo);
                }
                case 3 -> {
                    List<Cliente> clientes = sistema.listarClientes();
                    System.out.println("=== Clientes ===");
                    for (Cliente c : clientes) {
                        System.out.println(c.getNickname() + " - " + c.getNombre() + " " + c.getApellido());
                    }
                }
                case 4 -> {
                    List<Aerolinea> aerolineas = sistema.listarAerolineas();
                    System.out.println("=== Aerolíneas ===");
                    for (Aerolinea a : aerolineas) {
                        System.out.println(a.getNickname() + " - " + a.getNombre());
                    }
                }
            }

        } while (opcion != 0);

        System.out.println("¡Hasta luego!");
    }
}
