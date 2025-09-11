package guiSesion;


import Logica.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;


public class VerUsuarios {
    private JPanel panelConsulta;
    private JComboBox<String> comboBoxUsuarios;
    private JTextArea textAreaDatosUsuario;
    private JPanel panelBotonesDinamicos;
    private JButton cerrarButton;
    private JPanel JPanelDinamico;


    private final ISistema sistema;


    public VerUsuarios() {
        sistema = Fabrica.getInstance().getISistema();


        // --- Inicializar componentes ---
        JPanelDinamico.setLayout(new FlowLayout());



        // --- Layout general ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(comboBoxUsuarios, BorderLayout.NORTH);
        topPanel.add(new JScrollPane(textAreaDatosUsuario), BorderLayout.CENTER);


        panelConsulta.add(topPanel, BorderLayout.NORTH);
        panelConsulta.add(JPanelDinamico, BorderLayout.CENTER);
        panelConsulta.add(cerrarButton, BorderLayout.SOUTH);


        // --- Cargar usuarios ---
        DefaultComboBoxModel<String> modeloUsuarios = new DefaultComboBoxModel<>();
        for (Cliente c : sistema.listarClientes()) {
            modeloUsuarios.addElement("Cliente:" + c.getNickname());
        }
        for (Aerolinea a : sistema.listarAerolineas()) {
            modeloUsuarios.addElement("Aerolinea:" + a.getNickname());
        }
        comboBoxUsuarios.setModel(modeloUsuarios);
        comboBoxUsuarios.setSelectedIndex(-1);


        // --- Acción al seleccionar usuario ---
        comboBoxUsuarios.addActionListener(e -> {
            String seleccionado = (String) comboBoxUsuarios.getSelectedItem();
            if (seleccionado == null) return;


            JPanelDinamico.removeAll();
            if (seleccionado.startsWith("Cliente:")) {
                String nick = seleccionado.substring("Cliente:".length()).trim();
                mostrarCliente(nick);
                generarBotonesCliente(nick);
            } else if (seleccionado.startsWith("Aerolinea:")) {
                String nick = seleccionado.substring("Aerolinea:".length()).trim();
                mostrarAerolinea(nick);
                generarBotonesAerolinea(nick);
            }
            JPanelDinamico.revalidate();
            JPanelDinamico.repaint();
        });


        // --- Botón cerrar ---
        cerrarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panelConsulta);
            if (topFrame != null) topFrame.dispose();
        });
    }


    // --- Mostrar cliente ---
    private void mostrarCliente(String nickname) {
        Cliente c = sistema.obtenerCliente(nickname);
        if (c != null) {
            textAreaDatosUsuario.setText(
                    "Cliente: " + c.getNombre() + " " + c.getApellido() + "\n" +
                            "Email: " + c.getEmail() + "\n" +
                            "Fecha Nacimiento: " + c.getFechaNacimiento() + "\n" +
                            "Nacionalidad: " + c.getNacionalidad() + "\n"
            );
        }
    }


    // --- Mostrar aerolínea ---
    private void mostrarAerolinea(String nickname) {
        Aerolinea a = sistema.obtenerAerolinea(nickname);
        if (a != null) {
            textAreaDatosUsuario.setText(
                    "Aerolinea: " + a.getNombre() + "\n" +
                            "Email: " + a.getEmail() + "\n" +
                            "Sitio Web: " + a.getSitioWeb() + "\n" +
                            "Descripcion: " + a.getDescripcion() + "\n"
            );
        }
    }


    // --- Botones dinámicos para cliente ---
    private void generarBotonesCliente(String nickname) {
        JButton btnReservas = new JButton("Ver Reservas");
        btnReservas.addActionListener(e -> {
            Cliente c = sistema.obtenerCliente(nickname);
            mostrarListaInteractiva("Reservas del cliente", c.getReservas().values().toArray());
        });


        JButton btnPaquetes = new JButton("Ver Paquetes");
        btnPaquetes.addActionListener(e -> {
            Cliente c = sistema.obtenerCliente(nickname);
            mostrarListaInteractiva("Paquetes del cliente", c.getPaquetesComprados().toArray());
        });


        JPanelDinamico.add(btnReservas);
        JPanelDinamico.add(btnPaquetes);
    }

    private void generarBotonesAerolinea(String nickname) {
        JButton btnRutas = new JButton("Ver Rutas de Vuelo");
        btnRutas.addActionListener(e -> {
            List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nickname);
            mostrarListaInteractiva("Rutas de la aerolínea", rutas.toArray());
        });
        JPanelDinamico.add(btnRutas);
    }

    private void mostrarListaInteractiva(String titulo, Object[] elementos) {
        if (elementos == null || elementos.length == 0) {
            JOptionPane.showMessageDialog(panelConsulta, "No hay elementos para mostrar.");
            return;
        }

        JList<Object> lista = new JList<>(elementos);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setPreferredSize(new Dimension(400, 150));

        JTextArea areaDetalle = new JTextArea(8, 40);
        areaDetalle.setEditable(false);
        JScrollPane scrollDetalle = new JScrollPane(areaDetalle);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(scrollLista, BorderLayout.NORTH);
        panel.add(scrollDetalle, BorderLayout.CENTER);

        lista.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !lista.isSelectionEmpty()) {
                Object seleccionado = lista.getSelectedValue();
                String detalle = obtenerDetalle(seleccionado);
                areaDetalle.setText(detalle);
            }
        });

        JOptionPane.showMessageDialog(panelConsulta, panel, titulo, JOptionPane.PLAIN_MESSAGE);
    }


    // --- Obtener detalle como String (para poner en el textArea) ---
    private String obtenerDetalle(Object obj) {
        StringBuilder detalle = new StringBuilder();

        if (obj instanceof RutaVuelo r) {
            detalle.append("Ruta: ").append(r.getNombre()).append("\n")
                    .append("Origen: ").append(r.getCiudadOrigen()).append("\n")
                    .append("Destino: ").append(r.getCiudadDestino()).append("\n")
                    .append("Duración: ").append(r.getHora()).append(" horas\n")
                    .append("Vuelos Asociados: ").append(r.getVuelos().size()).append("\n\n");

            detalle.append("=== Vuelos ===\n");
            for (Vuelo v : r.getVuelos().values()) {
                detalle.append("Vuelo: ").append(v.getNombre()).append("\n")
                        .append("Fecha: ").append(v.getFecha()).append("\n")
                        .append("Duración: ").append(v.getDuracion()).append(" días\n")
                        .append("Asientos Ejecutivos: ").append(v.getAsientosEjecutivo()).append("\n")
                        .append("Asientos Turista: ").append(v.getAsientosTurista()).append("\n")
                        .append("Reservas: ").append(v.getReservas().size()).append("\n")
                        .append("-------------------------\n");
            }
        }
        if (obj instanceof Reserva r) {
            detalle.append("Reserva ID: ").append(r.getId()).append("\n")
                    .append("Costo: $").append(r.getCosto()).append("\n")
                    .append("Tipo Asiento: ").append(r.getTipoAsiento()).append("\n")
                    .append("Cantidad Pasajes: ").append(r.getCantidadPasajes()).append("\n")
                    .append("Cantidad Equipaje extra: ").append(r.getUnidadesEquipajeExtra()).append("\n");
        }
        if (obj instanceof Paquete p) {
            detalle.append("Paquete: ").append(p.getNombre()).append("\n")
                    .append("Descripción: ").append(p.getDescripcion()).append("\n")
                    .append("Costo: ").append(p.getCosto()).append("\n")
                    .append("Descuento: ").append(p.getDescuentoPorc()).append("%\n")
                    .append("Periodo de validez: ").append(p.getPeriodoValidezDias()).append(" días\n")
                    .append("Fecha de alta: ").append(p.getFechaAlta()).append("\n")
                    .append("Rutas incluidas: ").append(p.getItemPaquetes().size()).append("\n");

            for (ItemPaquete item : p.getItemPaquetes()) {
                detalle.append("   - Ruta: ").append(item.getRutaVuelo().getNombre())
                        .append(" | Tipo Asiento: ").append(item.getTipoAsiento())
                        .append(" | Cantidad: ").append(item.getCantAsientos())
                        .append("\n");
            }
        }

        return detalle.toString();
    }



    public Container getPanelConsulta() {
        return panelConsulta;
    }
}
