

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
            System.out.println("8. Consulta de Paquetes de Ruta de Vuelo");
            System.out.println("9. Alta Ruta de Vuelo");
            System.out.println("10. Alta Ciudad");
            System.out.println("11. Agregar Ruta de Vuelo a Paquete");
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
                    /*System.out.print("Fecha de alta (YYYY-MM-DD): ");
                    String fechaStr = sc.nextLine();
                    LocalDate fecha = LocalDate.parse(fechaStr);*/
                    System.out.print("Descuento (%): ");
                    int descuento = sc.nextInt();
                    System.out.print("Periodo de validez (días): ");
                    int periodo = sc.nextInt();
                    sc.nextLine();

                    sistema.altaPaquete(nombre, desc, costo, LocalDate.now(), descuento, periodo);
                    System.out.println("Paquete dado de alta con éxito.");
                }
                // CONSULTA DE PAQUETE DE RUTAS DE VUELO
                case 8 -> {
                    List<Paquete> paquetes = sistema.listarPaquetes();
                    if (paquetes.isEmpty()) {
                        System.out.println("No hay paquetes registrados.");
                        break;
                    }
                    mostrarPaquetes(paquetes);
                    System.out.print("Ingrese el nombre del paquete que desea consultar: ");
                    String nomPaquete = sc.nextLine();


                    Paquete paquete = null;
                    for (Paquete p : paquetes) {
                        if (p.getNombre().equalsIgnoreCase(nomPaquete)) {
                            paquete = p;
                            break;
                        }
                    }
                    if (paquete == null) {
                        System.out.println("No se encontró el paquete.");
                        break;
                    }

                    // Mostrar información general del paquete
                    System.out.println("=== Detalles del Paquete ===");
                    System.out.println("Nombre: " + paquete.getNombre());
                    System.out.println("Descripción: " + paquete.getDescripcion());
                    System.out.println("Costo: $" + paquete.getCosto());
                    System.out.println("Descuento: " + (paquete.getDescuentoPorc() > 0 ? paquete.getDescuentoPorc() + "%" : "No tiene"));
                    System.out.println("Periodo de validez: " + (paquete.getPeriodoValidezDias() > 0 ? paquete.getPeriodoValidezDias() + " días" : "Sin periodo"));
                    System.out.println("Rutas incluidas:");
                    List<ItemPaquete> items = paquete.getItemPaquetes();
                    if (items.isEmpty()) {
                        System.out.println(" No hay rutas de vuelo agregadas a este paquete.");
                    } else {
                        for (ItemPaquete item : items) {
                            System.out.println("  Ruta: " + item.getRutaVuelo().getNombre() + " | Cantidad de asientos: " + item.getCantAsientos() +
                                    " | Tipo de asiento: " + item.getTipoAsiento());
                        }

                        System.out.print("Desea consultar una ruta específica de este paquete? (s/n): ");
                        String resp = sc.nextLine();
                        if (resp.equalsIgnoreCase("s")) {
                            System.out.print("Ingrese el nombre de la ruta: ");
                            String nomRuta = sc.nextLine();

                            RutaVuelo rutaSeleccionada = null;
                            for (ItemPaquete item : items) {
                                if (item.getRutaVuelo().getNombre().equalsIgnoreCase(nomRuta)) {
                                    rutaSeleccionada = item.getRutaVuelo();
                                    break;
                                }
                            }
                            if (rutaSeleccionada != null) {
                                System.out.println("=== Detalle de la Ruta ===");
                                System.out.println("Nombre: " + rutaSeleccionada.getNombre());
                                System.out.println("Descripción: " + rutaSeleccionada.getDescripcion());
                                System.out.println("Origen: " + rutaSeleccionada.getCiudadOrigen());
                                System.out.println("Destino: " + rutaSeleccionada.getCiudadDestino());
                                System.out.println("Hora: " + rutaSeleccionada.getHora());
                                System.out.println("Costo Turista: $" + rutaSeleccionada.getCostoTurista());
                                System.out.println("Costo Ejecutivo: $" + rutaSeleccionada.getCostoEjecutivo());
                                System.out.println("Costo equipaje extra: $" + rutaSeleccionada.getCostoEquipajeExtra());
                            } else {
                                System.out.println("No se encontró la ruta en el paquete.");
                            }
                        }
                    }
                }
                // En el case 9 del menú
                case 9 -> {  // Alta de Ruta de Vuelo
                    List<Aerolinea> aerolineas = sistema.listarAerolineas();
                    System.out.println("=== Aerolíneas ===");
                    for (Aerolinea a : aerolineas) {
                        System.out.println(a.getNickname() + " - " + a.getNombre());
                    }
                    System.out.print("Ingrese el nickname de la aerolínea: ");
                    String nombreAerolinea = sc.nextLine();
                    Aerolinea aerolinea = sistema.obtenerAerolinea(nombreAerolinea);
                    if (aerolinea == null) {
                        System.out.println("Aerolínea no encontrada");
                        break;
                    }
                    System.out.print("Nombre de la ruta (único): ");
                    String nombreRuta = sc.nextLine();

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
                    System.out.print("Fecha de alta (YYYY-MM-DD): ");
                    String fechaStr = sc.nextLine();
                    LocalDate fechaAlta = LocalDate.parse(fechaStr);
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

                    // Pedir categorías (ejemplo: separadas por coma)
                    System.out.print("Categorías (separadas por coma): ");
                    String categoriasStr = sc.nextLine();
                    String[] categorias = categoriasStr.split(",");

                    sistema.altaRutaVuelo(
                            nombreRuta,
                            descripcion,
                            nombreAerolinea,
                            ciudadOrigen,
                            ciudadDestino,
                            hora,
                            fechaAlta,
                            costoTurista,
                            costoEjecutivo,
                            costoEquipaje,
                            categorias
                    );
                    System.out.println("Ruta de vuelo dada de alta con éxito.");
                }
                case 10 -> {
                    System.out.print("Nombre de la ciudad: ");
                    String nombreCiudad = sc.nextLine();
                    System.out.print("País: ");
                    String pais = sc.nextLine();
                    sistema.altaCiudad(nombreCiudad, pais);
                    System.out.println("Ciudad agregada correctamente.");
                }
                // AGREGAR RUTA DE VUELO A UN PAQUETE
                case 11 -> {
                    List<Paquete> disponibles = sistema.listarPaquetesDisp();
                    mostrarPaquetes(disponibles);
                    System.out.print("Seleccione el paquete al que desea agregar una ruta: ");
                    String nomPaquete = sc.nextLine();
                    List<Aerolinea> aerolineas = sistema.listarAerolineas();
                    System.out.println("=== Aerolíneas ===");
                    for (Aerolinea a : aerolineas) {
                        System.out.println(a.getNickname() + " - " + a.getNombre());
                    }
                    System.out.print("Ingrese el nickname de la aerolínea que desea agregar una ruta: ");
                    String nombreAerolinea = sc.nextLine();
                    // Listar rutas de vuelo de la aerolínea seleccionada
                    List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nombreAerolinea);
                    System.out.println("=== Rutas de Vuelo de dicha Aerolinea ===");
                    for (RutaVuelo r : rutas) {
                        System.out.println(r.getNombre());
                    }
                    System.out.print("Ingrese el nombre de la ruta que desea agregar: ");
                    String nombreRuta = sc.nextLine();
                    System.out.print("Ingrese la cantidad de asientos de la ruta " + nombreRuta + " que desee agregar al paquete " + nomPaquete + ": ");
                    int cantAsi = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Ingrese el tipo de asiento (TURISTA/EJECUTIVO) de la ruta " + nombreRuta + " que desea agregar al paquete " + nomPaquete + ": ");
                    String tipoStr = sc.nextLine().toUpperCase();
                    TipoAsiento tipoA = TipoAsiento.valueOf(tipoStr);
                    sistema.altaRutaPaquete(nomPaquete, nombreRuta, cantAsi, tipoA);
                }

            }


        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        } while (opcion != 0);


        System.out.println("¡Hasta luego!");
    }


    public static void mostrarPaquetes(List<Paquete> paquetes) {
        System.out.println("=== Paquetes ===");
        for (Paquete p : paquetes) {
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Descripción: " + p.getDescripcion());
            System.out.println("Costo: $" + p.getCosto());
            System.out.println("Fecha de alta: " + p.getFechaAlta());
            System.out.println("Descuento: " + (p.getDescuentoPorc() > 0 ? p.getDescuentoPorc() + "%" : "No tiene."));
            System.out.println("Periodo de validez: " + (p.getPeriodoValidezDias() > 0 ? p.getPeriodoValidezDias() + " días" : "Sin periodo."));
            System.out.println("-------------------------");
        }
    }
}

