

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class inter {

    public static void main(String[] args) {

        Fabrica fabrica = Fabrica.getInstance();
        ISistema sistema = fabrica.getISistema();
        Scanner sc = new Scanner(System.in);
        sistema.cargarDatosEjemplo();

        int opcion = 0;
        do {

        try {
            System.out.println("===== MENÚ PRINCIPAL =====");
            System.out.println("1. Alta Cliente");
            System.out.println("2. Alta Aerolinea");
            System.out.println("3. Listar Clientes");
            System.out.println("4. Listar Aerolineas");
            System.out.println("5. Alta Vuelo");
            System.out.println("6. Consulta Vuelo");
            System.out.println("7. Alta Paquete");
            System.out.println("8. Listar Paquetes");
            System.out.println("9. Alta Ruta de Vuelo");
            System.out.println("10. Alta Ciudad");
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
                // Listar CLIENTES
                case 3 -> {
                    List<Cliente> clientes = sistema.listarClientes();
                    System.out.println("=== Clientes ===");
                    for (Cliente c : clientes) {
                        System.out.println(c.getNickname() + " - " + c.getNombre() + " " + c.getApellido());
                    }
                }
                // Listar AEROLINEAS
                case 4 -> {
                    List<Aerolinea> aerolineas = sistema.listarAerolineas();
                    System.out.println("=== Aerolíneas ===");
                    for (Aerolinea a : aerolineas) {
                        System.out.println(a.getNickname() + " - " + a.getNombre());
                    }
                }
                // ALTA VUELO
                case 5 -> {
                    List<Aerolinea> aerolineas = sistema.listarAerolineas();
                    System.out.println("=== Aerolíneas ===");
                    for (Aerolinea a : aerolineas) {
                        System.out.println(a.getNickname() + " - " + a.getNombre());
                    }
                    System.out.print("Ingrese el nickname de la aerolínea: ");
                    String nombreAerolinea = sc.nextLine();

                    // Listar rutas de vuelo de la aerolínea seleccionada
                    List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nombreAerolinea);


                    System.out.println("=== Rutas de Vuelo ===");
                    for (RutaVuelo r : rutas) {
                        System.out.println(r.getNombre());
                    }
                    System.out.print("Ingrese el nombre de la ruta: ");
                    String nombreRuta = sc.nextLine();

                    // Pedir datos del vuelo
                    System.out.print("Nombre del vuelo: ");
                    String nombreVuelo = sc.nextLine();
                    System.out.print("Fecha (YYYY-MM-DD): ");
                    String fecha = sc.nextLine();
                    System.out.print("Duración (minutos): ");
                    int duracion = sc.nextInt();
                    System.out.print("Cantidad máxima de asientos turista: ");
                    int asientosTurista = sc.nextInt();
                    System.out.print("Cantidad máxima de asientos ejecutivo: ");
                    int asientosEjecutivo = sc.nextInt();
                    sc.nextLine();

                    String resultado = sistema.altaVueloAux(nombreAerolinea, nombreRuta, nombreVuelo, fecha, duracion, asientosTurista, asientosEjecutivo);
                    System.out.println(resultado);
                }
                // CONSULTA VUELO
                case 6 -> {
                    List<Aerolinea> aerolineas = sistema.listarAerolineas();
                    System.out.println("=== Aerolíneas ===");
                    for (Aerolinea a : aerolineas) {
                        System.out.println(a.getNickname() + " - " + a.getNombre());
                    }
                    System.out.print("Ingrese el nickname de la aerolínea: ");
                    String nombreAerolinea = sc.nextLine();

                    // Listar rutas de vuelo de la aerolínea seleccionada
                    List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nombreAerolinea);
                    System.out.println("=== Rutas de Vuelo ===");
                    for (RutaVuelo r : rutas) {
                        System.out.println(r.getNombre());
                    }
                    System.out.print("Ingrese el nombre de la ruta: ");
                    String nombreRuta = sc.nextLine();

                    // Listar vuelos de la ruta seleccionada
                    List<Vuelo> vuelos = sistema.listarVuelosPorRuta(nombreRuta);
                    System.out.println("=== Vuelos ===");
                    for (Vuelo v : vuelos) {
                        System.out.println(v.getNombre());
                    }
                    System.out.print("Ingrese el nombre del vuelo: ");
                    String nombreVuelo = sc.nextLine();

                    // Mostrar información del vuelo
                    try {
                        Vuelo vuelo = sistema.verInfoVuelo(nombreVuelo);
                        System.out.println("Datos del vuelo:");
                        System.out.println("Nombre: " + vuelo.getNombre());
                        System.out.println("Fecha: " + vuelo.getFecha());
                        System.out.println("Duración: " + vuelo.getDuracion());
                        System.out.println("Asientos turista: " + vuelo.getAsientosTurista());
                        System.out.println("Asientos ejecutivo: " + vuelo.getAsientosEjecutivo());
                        System.out.println("Reservas:");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                // ALTA PAQUETE
                case 7 -> {
                    System.out.print("Nombre del paquete: ");
                    String nombre = sc.nextLine();
                    System.out.print("Descripción: ");
                    String desc = sc.nextLine();
                    System.out.print("Costo: ");
                    double costo = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Fecha de alta (YYYY-MM-DD): ");
                    String fechaStr = sc.nextLine();
                    LocalDate fecha = LocalDate.parse(fechaStr);
                    System.out.print("Descuento (%): ");
                    int descuento = sc.nextInt();
                    System.out.print("Periodo de validez (días): ");
                    int periodo = sc.nextInt();
                    sc.nextLine();

                    sistema.altaPaquete(nombre, desc, costo, fecha, descuento, periodo);
                    System.out.println("Paquete dado de alta con éxito.");
                }
                // Listar PAQUETES
                case 8 -> {
                    List<Paquete> paquetes = sistema.listarPaquetes();
                    System.out.println("=== Paquetes ===");
                    for (Paquete p : paquetes) {
                        System.out.println("Nombre: " + p.getNombre());
                        System.out.println("Descripción: " + p.getDescripcion());
                        System.out.println("Costo: " + p.getCosto());
                        System.out.println("Fecha de alta: " + p.getFechaAlta());
                        System.out.println("Descuento: " + p.getDescuento() + "%");
                        System.out.println("Periodo de validez: " + p.getPeriodoValidez() + " días");
                        System.out.println("-------------------------");
                    }
                }
                case 9 -> {  // Alta de Ruta de Vuelo
                    List<Aerolinea> aerolineas = sistema.listarAerolineas();
                    System.out.println("=== Aerolíneas ===");
                    for (Aerolinea a : aerolineas) {
                        System.out.println(a.getNickname() + " - " + a.getNombre());
                    }
                    System.out.print("Ingrese el nickname de la aerolínea: ");
                    String nombreAerolinea = sc.nextLine();
                    // Verificar si la aerolínea existe
                    Aerolinea aerolinea = sistema.obtenerAerolinea(nombreAerolinea);
                    if (aerolinea == null) {
                        System.out.println("Aerolínea no encontrada");
                    }
                    System.out.print("Nombre de la ruta (único): ");
                    String nombreRuta = sc.nextLine();

                    // Verificar si ya existe la ruta
                    List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nombreAerolinea);
                    boolean existe = rutas.stream().anyMatch(r -> r.getNombre().equals(nombreRuta));
                    if (existe) {
                        System.out.println("Ya existe una ruta con ese nombre. ¿Desea reingresar los datos? (s/n)");
                        String resp = sc.nextLine();
                        if (resp.equalsIgnoreCase("s")) continue;
                        else break;
                    }

                    System.out.print("Descripción: ");
                    String descripcion = sc.nextLine();
                    System.out.print("Hora (HH:mm): ");
                    String hora = sc.nextLine();
                    System.out.print("Costo turista: ");
                    double costoTurista = sc.nextDouble();
                    System.out.print("Costo ejecutivo: ");
                    double costoEjecutivo = sc.nextDouble();
                    System.out.print("Costo unidad equipaje extra: ");
                    double costoEquipaje = sc.nextDouble();
                    sc.nextLine();



                    System.out.print("Ciudad origen: ");
                    String ciudadOrigen = sc.nextLine();
                    System.out.print("Ciudad destino: ");
                    String ciudadDestino = sc.nextLine();

                    // Aquí podrías listar y seleccionar categorías si tienes esa funcionalidad

                    // Alta de la ruta
                    sistema.altaRutaVuelo(nombreRuta, nombreAerolinea, ciudadOrigen, ciudadDestino, costoTurista, costoEjecutivo);
                    System.out.println("✅ Ruta de vuelo dada de alta con éxito.");
                }
                case 10 -> {
                    System.out.print("Nombre de la ciudad: ");
                    String nombreCiudad = sc.nextLine();
                    System.out.print("País: ");
                    String pais = sc.nextLine();
                    sistema.altaCiudad(nombreCiudad, pais);
                    System.out.println("Ciudad agregada correctamente.");
                }
            }


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            continue;
        }
        } while (opcion != 0);


        System.out.println("¡Hasta luego!");
    }
}

