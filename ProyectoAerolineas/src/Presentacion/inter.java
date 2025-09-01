import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class inter {

    public static void main(String[] args) {

        Fabrica fabrica = Fabrica.getInstance();
        ISistema sistema = fabrica.getISistema();
        Scanner sc = new Scanner(System.in);

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
                System.out.println("11. Reserva Vuelo");
                System.out.println("12. Modificar Cliente");
                System.out.println("13. Modificar Aerolinea");
                System.out.println("14. Consulta Rutas de Vuelo");
                System.out.println("15. Agregar Ruta de Vuelo a Paquete");
                System.out.println("0. Salir");
                System.out.print("Opción: ");
                opcion = sc.nextInt();
                sc.nextLine(); // limpiar buffer

                switch (opcion) {
                    // Alta Cliente
                    case 1 -> {
                        System.out.print("Nickname: ");
                        String nick = sc.nextLine();
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Apellido: ");
                        String apellido = sc.nextLine();
                        System.out.print("Correo: ");
                        String correo = sc.nextLine();
                        System.out.print("Fecha de Nacimiento (YYYY-MM-DD): ");
                        String fechaStr = sc.nextLine();
                        LocalDate fechaNac = LocalDate.parse(fechaStr);
                        System.out.print("Nacionalidad: ");
                        String nacionalidad = sc.nextLine();
                        System.out.print("Tipo de Documento: ");
                        String tipoDoc = sc.nextLine();
                        System.out.print("Número de Documento: ");
                        String numDoc = sc.nextLine();
                        sistema.altaCliente(nick, nombre, apellido, correo, fechaNac, nacionalidad, tipoDoc, numDoc);
                    }
                    // Alta Aerolinea
                    case 2 -> {
                        System.out.print("Nickname: ");
                        String nick = sc.nextLine();
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Descripcion: ");
                        String desc = sc.nextLine();
                        System.out.print("Correo: ");
                        String correo = sc.nextLine();
                        System.out.print("Sitio web: ");
                        String sitioweb = sc.nextLine();
                        sistema.altaAerolinea(nick, nombre, correo, desc, sitioweb);
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

                        String resultado = sistema.altaVueloAux(nombreAerolinea, nombreRuta, nombreVuelo, fecha,
                                duracion, asientosTurista, asientosEjecutivo);
                        System.out.println(resultado);
                    }
                    // CONSULTA VUELO
                    case 6 -> {
                        List<Aerolinea> aerolineas = sistema.listarAerolineas();
                        if (aerolineas.isEmpty()) {
                            System.out.println("No hay aerolíneas registradas.");
                            break;
                        }

                        System.out.println("=== Aerolíneas ===");
                        for (Aerolinea a : aerolineas) {
                            System.out.println(a.getNickname() + " - " + a.getNombre());
                        }
                        System.out.print("Ingrese el nickname de la aerolínea: ");
                        String nicknameAerolinea = sc.nextLine();

                        List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nicknameAerolinea);
                        if (rutas.isEmpty()) {
                            System.out.println("No hay rutas de vuelo registradas para esta aerolínea.");
                            break;
                        }

                        System.out.println("=== Rutas de Vuelo ===");
                        for (RutaVuelo r : rutas) {
                            System.out.println(r.getNombre());
                        }
                        System.out.print("Ingrese el nombre de la ruta: ");
                        String nombreRuta = sc.nextLine();

                        RutaVuelo ruta = sistema.obtenerRuta(nombreRuta); // necesitas exponer esto en Sistema
                        if (ruta == null) {
                            System.out.println("Ruta de vuelo no encontrada.");
                            break;
                        }

                        // Mostrar datos de la ruta
                        System.out.println("=== Datos de la Ruta de Vuelo ===");
                        System.out.println("Nombre: " + ruta.getNombre());
                        System.out.println("Descripción: " + ruta.getDescripcion());
                        System.out.println("Aerolínea: " + ruta.getAerolinea().getNombre());
                        System.out.println("Ciudad Origen: " + ruta.getCiudadOrigen());
                        System.out.println("Ciudad Destino: " + ruta.getCiudadDestino());
                        System.out.println("Hora: " + ruta.getHora());
                        System.out.println("Fecha de alta: " + ruta.getFechaAlta());
                        System.out.println("Costo Turista: " + ruta.getCostoTurista());
                        System.out.println("Costo Ejecutivo: " + ruta.getCostoEjecutivo());
                        System.out.println("Costo Equipaje Extra: " + ruta.getCostoEquipajeExtra());
                        System.out.println("Categorías: " + String.join(", ", ruta.getCategorias()));
                        System.out.println("=================================");

                        // Listar vuelos de la ruta
                        List<Vuelo> vuelos = sistema.listarVuelosPorRuta(nombreRuta);
                        if (vuelos.isEmpty()) {
                            System.out.println("No hay vuelos registrados para esta ruta.");
                            break;
                        }

                        System.out.println("=== Vuelos ===");
                        for (Vuelo v : vuelos) {
                            System.out.println(v.getNombre());
                        }
                        System.out.print("Ingrese el nombre del vuelo (o Enter para no consultar): ");
                        String nombreVuelo = sc.nextLine();

                        if (!nombreVuelo.isEmpty()) {
                            try {
                                Vuelo vuelo = sistema.verInfoVuelo(nombreVuelo);
                                System.out.println("=== Datos del vuelo ===");
                                System.out.println("Nombre: " + vuelo.getNombre());
                                System.out.println("Fecha: " + vuelo.getFecha());
                                System.out.println("Duración: " + vuelo.getDuracion());
                                System.out.println("Asientos turista: " + vuelo.getAsientosTurista());
                                System.out.println("Asientos ejecutivo: " + vuelo.getAsientosEjecutivo());
                                System.out.println("Reservas:");
                                // Aquí podrías iterar reservas si las manejás
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
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
                    // Alta de Ruta de Vuelo
                    case 9 -> {
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
                            if (resp.equalsIgnoreCase("s"))
                                continue;
                            else
                                break;
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
                        sc.nextLine(); // limpiar buffer

                        System.out.print("Ciudad origen: ");
                        String ciudadOrigen = sc.nextLine();
                        System.out.print("Ciudad destino: ");
                        String ciudadDestino = sc.nextLine();

                        // Pedir categorías (ejemplo: separadas por coma)
                        System.out.print("Categorías (separadas por coma): ");
                        String categoriasStr = sc.nextLine();
                        String[] categorias = categoriasStr.split(",");

                        // Ahora pasamos el objeto Aerolinea en lugar del nickname
                        sistema.altaRutaVuelo(
                                nombreRuta,
                                descripcion,
                                aerolinea,   // <-- objeto, no String
                                ciudadOrigen,
                                ciudadDestino,
                                hora,
                                fechaAlta,
                                costoTurista,
                                costoEjecutivo,
                                costoEquipaje,
                                categorias);

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
                    case 11 -> {
                        boolean reservaExitosa = false;
                        while (!reservaExitosa) {
                            // Listar aerolíneas
                            List<Aerolinea> aerolineas = sistema.listarAerolineas();
                            System.out.println("=== Aerolíneas ===");
                            for (Aerolinea a : aerolineas) {
                                System.out.println(a.getNickname() + " - " + a.getNombre());
                            }
                            System.out.print("Ingrese el nickname de la aerolínea: ");
                            String nombreAerolinea = sc.nextLine();

                            // Listar rutas de vuelo
                            List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nombreAerolinea);
                            if (rutas.isEmpty()) {
                                System.out.println("No hay rutas para esta aerolínea.");
                                break;
                            }
                            System.out.println("=== Rutas de Vuelo ===");
                            for (RutaVuelo r : rutas) {
                                System.out.println(r.getNombre());
                            }
                            System.out.print("Ingrese el nombre de la ruta: ");
                            String nombreRuta = sc.nextLine();

                            // Listar vuelos
                            List<Vuelo> vuelos = sistema.listarVuelosPorRuta(nombreRuta);
                            if (vuelos.isEmpty()) {
                                System.out.println("No hay vuelos para esta ruta.");
                                break;
                            }
                            System.out.println("=== Vuelos ===");
                            for (Vuelo v : vuelos) {
                                System.out.println(v.getNombre());
                            }
                            System.out.print("Ingrese el nombre del vuelo: ");
                            String nombreVuelo = sc.nextLine();

                            // Mostrar datos del vuelo
                            try {
                                Vuelo vuelo = sistema.verInfoVuelo(nombreVuelo);
                                System.out.println("Datos del vuelo:");
                                System.out.println("Nombre: " + vuelo.getNombre());
                                System.out.println("Fecha: " + vuelo.getFecha());
                                System.out.println("Duración: " + vuelo.getDuracion());
                                System.out.println("Asientos turista: " + vuelo.getAsientosTurista());
                                System.out.println("Asientos ejecutivo: " + vuelo.getAsientosEjecutivo());
                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                                break;
                            }

                            // Listar clientes
                            List<Cliente> clientes = sistema.listarClientes();
                            if (clientes.isEmpty()) {
                                System.out.println("No hay clientes registrados.");
                                break;
                            }
                            System.out.println("=== Clientes ===");
                            for (Cliente c : clientes) {
                                System.out.println(c.getNickname() + " - " + c.getNombre() + " " + c.getApellido());
                            }
                            System.out.print("Ingrese el nickname del cliente: ");
                            String nicknameCliente = sc.nextLine();

                            // Tipo de asiento
                            System.out.print("Tipo de asiento (TURISTA/EJECUTIVO): ");
                            String tipoAsientoStr = sc.nextLine().toUpperCase();
                            TipoAsiento tipoAsiento = tipoAsientoStr.equals("EJECUTIVO") ? TipoAsiento.EJECUTIVO
                                    : TipoAsiento.TURISTA;

                            // Cantidad de pasajes
                            System.out.print("Cantidad de pasajes: ");
                            int cantidadPasajes = sc.nextInt();
                            sc.nextLine();

                            // Unidades de equipaje extra
                            System.out.print("Unidades de equipaje extra: ");
                            int unidadesEquipajeExtra = sc.nextInt();
                            sc.nextLine();

                            // Nombres y apellidos de los pasajeros
                            List<Pasajero> pasajeros = new ArrayList<>();
                            for (int i = 1; i <= cantidadPasajes; i++) {
                                System.out.print("Nombre del pasajero " + i + ": ");
                                String nombrePasajero = sc.nextLine();
                                System.out.print("Apellido del pasajero " + i + ": ");
                                String apellidoPasajero = sc.nextLine();
                                pasajeros.add(new Pasajero(nombrePasajero, apellidoPasajero));
                            }

                            // Fecha de la reserva
                            System.out.print("Fecha de la reserva (YYYY-MM-DD): ");
                            String fechaReservaStr = sc.nextLine();
                            LocalDate fechaReserva = LocalDate.parse(fechaReservaStr);

                            // Calcular costo (ajusta según tu lógica)
                            double costo = 0;
                            RutaVuelo ruta = rutas.stream().filter(r -> r.getNombre().equals(nombreRuta)).findFirst()
                                    .orElse(null);
                            if (ruta != null) {
                                if (tipoAsiento == TipoAsiento.TURISTA) {
                                    costo = ruta.getCostoTurista() * cantidadPasajes
                                            + ruta.getCostoEquipajeExtra() * unidadesEquipajeExtra;
                                } else {
                                    costo = ruta.getCostoEjecutivo() * cantidadPasajes
                                            + ruta.getCostoEquipajeExtra() * unidadesEquipajeExtra;
                                }
                            }

                            // Registrar reserva usando la función auxiliar
                            String resultado = sistema.crearYRegistrarReserva(
                                    nicknameCliente,
                                    nombreVuelo,
                                    fechaReserva,
                                    costo,
                                    tipoAsiento,
                                    cantidadPasajes,
                                    unidadesEquipajeExtra,
                                    pasajeros);
                            System.out.println(resultado);

                            // Si ya existe una reserva, ofrecer opciones
                            if (resultado.toLowerCase().contains("ya tiene una reserva")) {
                                System.out.println("¿Qué desea hacer?");
                                System.out.println("1. Cambiar aerolínea");
                                System.out.println("2. Cambiar ruta de vuelo");
                                System.out.println("3. Cambiar vuelo");
                                System.out.println("4. Cambiar cliente");
                                System.out.println("5. Cancelar");
                                System.out.print("Opción: ");
                                int opcionCambio = sc.nextInt();
                                sc.nextLine();
                                switch (opcionCambio) {
                                    case 1:
                                        continue; // Volver a elegir aerolínea
                                    case 2:
                                        continue; // Volver a elegir ruta
                                    case 3:
                                        continue; // Volver a elegir vuelo
                                    case 4:
                                        continue; // Volver a elegir cliente
                                    case 5:
                                        System.out.println("Reserva cancelada.");
                                        break;
                                    default:
                                        System.out.println("Opción inválida. Reserva cancelada.");
                                        break;
                                }
                                if (opcionCambio == 5)
                                    break;
                            } else {
                                reservaExitosa = true;
                            }
                        }
                    }
                    case 12 -> {
                        List<Cliente> clientes = sistema.listarClientes();
                        if (clientes.isEmpty()) {
                            System.out.println("No hay clientes registrados.");
                            break;
                        }

                        System.out.println("=== Clientes ===");
                        for (Cliente c : clientes) {
                            System.out.println(c.getNickname() + " - " + c.getNombre() + " " + c.getApellido());
                        }

                        System.out.print("Ingrese el nickname del cliente a modificar: ");
                        String nickname = sc.nextLine();

                        Cliente clienteOriginal = clientes.stream()
                                .filter(c -> c.getNickname().equals(nickname))
                                .findFirst()
                                .orElse(null);

                        if (clienteOriginal == null) {
                            System.out.println("Cliente no encontrado.");
                            break;
                        }

                        // Mostrar los datos actuales del cliente
                        System.out.println("=== Datos actuales del cliente ===");
                        System.out.println("Nickname: " + clienteOriginal.getNickname());
                        System.out.println("Nombre: " + clienteOriginal.getNombre());
                        System.out.println("Apellido: " + clienteOriginal.getApellido());
                        System.out.println("Email: " + clienteOriginal.getEmail());
                        System.out.println("Fecha de nacimiento: " + clienteOriginal.getFechaNacimiento());
                        System.out.println("Nacionalidad: " + clienteOriginal.getNacionalidad());
                        System.out.println("Tipo de documento: " + clienteOriginal.getTipoDocumento());
                        System.out.println("Número de documento: " + clienteOriginal.getNumeroDocumento());
                        System.out.println("=================================");

                        // Pedir nuevos datos (o vacío para no modificar)
                        System.out.print("Nuevo nombre (dejar vacío para no modificar): ");
                        String nuevoNombre = sc.nextLine();
                        System.out.print("Nuevo apellido (dejar vacío para no modificar): ");
                        String nuevoApellido = sc.nextLine();
                        System.out.print("Nueva nacionalidad (dejar vacío para no modificar): ");
                        String nuevaNacionalidad = sc.nextLine();
                        System.out.print("Nuevo tipo de documento (dejar vacío para no modificar): ");
                        String nuevoTipoDoc = sc.nextLine();
                        System.out.print("Nuevo número de documento (dejar vacío para no modificar): ");
                        String nuevoNumDoc = sc.nextLine();

                        LocalDate nuevaFechaNac = null;
                        System.out.print("Nueva fecha de nacimiento (YYYY-MM-DD, dejar vacío para no modificar): ");
                        String nuevaFechaStr = sc.nextLine();
                        if (!nuevaFechaStr.isEmpty()) {
                            try {
                                nuevaFechaNac = LocalDate.parse(nuevaFechaStr);
                            } catch (Exception e) {
                                System.out.println("Formato de fecha inválido. No se modificará la fecha.");
                            }
                        }

                        // Crear cliente temporal con los nuevos datos
                        Cliente clienteTemporal = new Cliente(
                                nickname,
                                nuevoNombre.isEmpty() ? null : nuevoNombre,
                                nuevoApellido.isEmpty() ? null : nuevoApellido,
                                null, // email no se modifica
                                nuevaFechaNac,
                                nuevaNacionalidad.isEmpty() ? null : nuevaNacionalidad,
                                nuevoTipoDoc.isEmpty() ? null : nuevoTipoDoc,
                                nuevoNumDoc.isEmpty() ? null : nuevoNumDoc
                        );

                        sistema.modificarDatosDeCliente(clienteTemporal);

                        System.out.println("Datos del cliente modificados correctamente.");
                    }
                    case 13 -> {
                        List<Aerolinea> aerolineas = sistema.listarAerolineas();
                        if (aerolineas.isEmpty()) {
                            System.out.println("No hay aerolíneas registradas.");
                            break;
                        }

                        System.out.println("=== Aerolíneas ===");
                        for (Aerolinea a : aerolineas) {
                            System.out.println(a.getNickname() + " - " + a.getNombre());
                        }

                        System.out.print("Ingrese el nickname de la aerolínea a modificar: ");
                        String nickname = sc.nextLine();

                        Aerolinea aerolineaOriginal = aerolineas.stream()
                                .filter(a -> a.getNickname().equals(nickname))
                                .findFirst()
                                .orElse(null);

                        if (aerolineaOriginal == null) {
                            System.out.println("Aerolínea no encontrada.");
                            break;
                        }

                        // Mostrar datos actuales
                        System.out.println("=== Datos actuales de la aerolínea ===");
                        System.out.println("Nickname: " + aerolineaOriginal.getNickname());
                        System.out.println("Nombre: " + aerolineaOriginal.getNombre());
                        System.out.println("Email: " + aerolineaOriginal.getEmail());
                        System.out.println("Descripción: " + aerolineaOriginal.getDescripcion());
                        System.out.println("Sitio web: " + aerolineaOriginal.getSitioWeb());
                        System.out.println("======================================");

                        // Pedir nuevos valores
                        System.out.print("Nuevo nombre (dejar vacío para no modificar): ");
                        String nuevoNombre = sc.nextLine();
                        System.out.print("Nueva descripción (dejar vacío para no modificar): ");
                        String nuevaDescripcion = sc.nextLine();
                        System.out.print("Nuevo sitio web (dejar vacío para no modificar): ");
                        String nuevoSitioWeb = sc.nextLine();

                        // Crear aerolínea temporal solo con los datos ingresados
                        Aerolinea aerolineaTemporal = new Aerolinea(
                                nickname,
                                nuevoNombre.isEmpty() ? null : nuevoNombre,
                                null, // email no se modifica
                                nuevaDescripcion.isEmpty() ? null : nuevaDescripcion,
                                nuevoSitioWeb.isEmpty() ? null : nuevoSitioWeb
                        );

                        sistema.modificarDatosAerolinea(aerolineaTemporal);

                        System.out.println("Datos de la aerolínea modificados correctamente.");
                    }

                    // CONSULTA RUTA DE VUELO
                    case 14 -> {
                        List<Aerolinea> aerolineas = sistema.listarAerolineas();
                        if (aerolineas.isEmpty()) {
                            System.out.println("No hay aerolíneas registradas.");
                            break;
                        }

                        System.out.println("=== Aerolíneas ===");
                        for (Aerolinea a : aerolineas) {
                            System.out.println(a.getNickname() + " - " + a.getNombre());
                        }
                        System.out.print("Ingrese el nickname de la aerolínea: ");
                        String nicknameAerolinea = sc.nextLine();

                        List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nicknameAerolinea);
                        if (rutas.isEmpty()) {
                            System.out.println("No hay rutas de vuelo registradas para esta aerolínea.");
                            break;
                        }

                        System.out.println("=== Rutas de Vuelo ===");
                        for (RutaVuelo r : rutas) {
                            System.out.println(r.getNombre());
                        }
                        System.out.print("Ingrese el nombre de la ruta: ");
                        String nombreRuta = sc.nextLine();

                        RutaVuelo ruta = sistema.obtenerRuta(nombreRuta); // debe existir este método en Sistema
                        if (ruta == null) {
                            System.out.println("Ruta de vuelo no encontrada.");
                            break;
                        }

                        // Mostrar datos de la ruta
                        System.out.println("=== Datos de la Ruta de Vuelo ===");
                        System.out.println("Nombre: " + ruta.getNombre());
                        System.out.println("Descripción: " + ruta.getDescripcion());
                        System.out.println("Aerolínea: " + ruta.getAerolinea().getNombre());
                        System.out.println("Ciudad Origen: " + ruta.getCiudadOrigen());
                        System.out.println("Ciudad Destino: " + ruta.getCiudadDestino());
                        System.out.println("Hora: " + ruta.getHora());
                        System.out.println("Fecha de alta: " + ruta.getFechaAlta());
                        System.out.println("Costo Turista: " + ruta.getCostoTurista());
                        System.out.println("Costo Ejecutivo: " + ruta.getCostoEjecutivo());
                        System.out.println("Costo Equipaje Extra: " + ruta.getCostoEquipajeExtra());
                        System.out.println("Categorías: " + String.join(", ", ruta.getCategorias()));
                        System.out.println("=================================");

                        // Mostrar vuelos asociados
                        List<Vuelo> vuelos = sistema.listarVuelosPorRuta(nombreRuta);
                        if (vuelos.isEmpty()) {
                            System.out.println("No hay vuelos registrados para esta ruta.");
                        } else {
                            System.out.println("=== Vuelos asociados ===");
                            for (Vuelo v : vuelos) {
                                System.out.println("- " + v.getNombre() + " (" + v.getFecha() + ")");
                            }
                        }
                    }
                    // AGREGAR RUTA DE VUELO A UN PAQUETE
                    case 15 -> {
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
                continue;
            }
        } while (opcion != 0);

        System.out.println("¡Hasta luego!");
    }

    // Funcion mostrarTodosLosPaquetes para acortar casos de uso
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
