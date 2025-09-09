package Presentacion.gui.guiSesion;

import Logica.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VerUsuarios {
    private JPanel panelConsulta;
    private JComboBox<String> comboBoxUsuarios;
    private JTextArea textAreaDatosUsuario;
    private JPanel panelBotonesDinamicos;
    private JButton cerrarButton;

    private final ISistema sistema;

    public VerUsuarios() {
        sistema = Fabrica.getInstance().getISistema();

        // --- Inicializar componentes ---
        panelConsulta = new JPanel(new BorderLayout());
        comboBoxUsuarios = new JComboBox<>();
        textAreaDatosUsuario = new JTextArea(10, 40);
        textAreaDatosUsuario.setEditable(false);
        cerrarButton = new JButton("Cerrar");
        panelBotonesDinamicos = new JPanel(new FlowLayout());

        // --- Layout general ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(comboBoxUsuarios, BorderLayout.NORTH);
        topPanel.add(new JScrollPane(textAreaDatosUsuario), BorderLayout.CENTER);

        panelConsulta.add(topPanel, BorderLayout.NORTH);
        panelConsulta.add(panelBotonesDinamicos, BorderLayout.CENTER);
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

            panelBotonesDinamicos.removeAll();
            if (seleccionado.startsWith("Cliente:")) {
                String nick = seleccionado.substring("Cliente:".length()).trim();
                mostrarCliente(nick);
                generarBotonesCliente(nick);
            } else if (seleccionado.startsWith("Aerolinea:")) {
                String nick = seleccionado.substring("Aerolinea:".length()).trim();
                mostrarAerolinea(nick);
                generarBotonesAerolinea(nick);
            }
            panelBotonesDinamicos.revalidate();
            panelBotonesDinamicos.repaint();
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
            mostrarListaInteractiva("Reservas del cliente", c.getReservas().toArray());
        });

        JButton btnPaquetes = new JButton("Ver Paquetes");
        btnPaquetes.addActionListener(e -> {
            Cliente c = sistema.obtenerCliente(nickname);
            mostrarListaInteractiva("Paquetes del cliente", c.getPaquetesComprados().toArray());
        });

        panelBotonesDinamicos.add(btnReservas);
        panelBotonesDinamicos.add(btnPaquetes);
    }

    // --- Botones dinámicos para aerolínea ---
    private void generarBotonesAerolinea(String nickname) {
        JButton btnRutas = new JButton("Ver Rutas de Vuelo");
        btnRutas.addActionListener(e -> {
            List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nickname);
            mostrarListaInteractiva("Rutas de la aerolínea", rutas.toArray());
        });

        panelBotonesDinamicos.add(btnRutas);
    }

    // --- Mostrar lista y permitir ver detalles ---
    private void mostrarListaInteractiva(String titulo, Object[] elementos) {
        if (elementos == null || elementos.length == 0) {
            JOptionPane.showMessageDialog(panelConsulta, "No hay elementos para mostrar.");
            return;
        }

        JList<Object> lista = new JList<>(elementos);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setPreferredSize(new Dimension(400, 200));

        int opcion = JOptionPane.showConfirmDialog(
                panelConsulta, scroll, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE
        );

        if (opcion == JOptionPane.OK_OPTION && !lista.isSelectionEmpty()) {
            Object seleccionado = lista.getSelectedValue();
            mostrarDetalle(seleccionado);
        }
    }

    // --- Mostrar detalle según tipo ---
    private void mostrarDetalle(Object obj) {
        StringBuilder detalle = new StringBuilder();

        if (obj instanceof RutaVuelo r) {
            detalle.append("Ruta: ").append(r.getNombre()).append("\n")
                    .append("Origen: ").append(r.getCiudadOrigen()).append("\n")
                    .append("Destino: ").append(r.getCiudadDestino()).append("\n")
                    .append("Duración: ").append(r.getHora()).append(" horas\n")
                    .append("Vuelos Asociados: ").append(r.getVuelos().size()).append("\n");

            // Permitir ver vuelos de la ruta
            mostrarListaInteractiva("Vuelos de la ruta", r.getVuelos().toArray());
            return;
        }
        if (obj instanceof Vuelo v) {
            detalle.append("Vuelo: ").append(v.getNombre()).append("\n")
                    .append("Fecha: ").append(v.getFecha()).append("\n")
                    .append("Duración: ").append(v.getDuracion()).append(" días\n")
                    .append("Asientos Ejecutivos: ").append(v.getAsientosEjecutivo()).append("\n")
                    .append("Asientos Turista: ").append(v.getAsientosTurista()).append("\n")
                    .append("Reservas: ").append(v.getReservas().size()).append("\n");
        }
        if (obj instanceof Reserva r) {
            detalle.append("Reserva ID: ").append(r.getId()).append("\n")
                    .append("Costo: ").append(r.getCosto()).append("\n")
                    .append("Tipo Asiento: ").append(r.getTipoAsiento()).append("\n")
                    .append("Cantidad Pasajes: ").append(r.getCantidadPasajes()).append("\n");
        }
        if (obj instanceof Paquete p) {
            detalle.append("Paquete: ").append(p.getNombre()).append("\n")
                    .append("Descripción: ").append(p.getDescripcion()).append("\n")
                    .append("Costo: ").append(p.getCosto()).append("\n")
                    .append("Descuento: ").append(p.getDescuentoPorc()).append("%\n")
                    .append("Periodo de validez: ").append(p.getPeriodoValidezDias()).append(" días\n")
                    .append("Fecha de alta: ").append(p.getFechaAlta()).append("\n")
                    .append("Rutas incluidas: ").append(p.getItemPaquetes().size()).append("\n");

            // Mostrar detalle de las rutas incluidas
            for (ItemPaquete item : p.getItemPaquetes()) {
                detalle.append("   - Ruta: ").append(item.getRutaVuelo().getNombre())
                        .append(" | Tipo Asiento: ").append(item.getTipoAsiento())
                        .append(" | Cantidad: ").append(item.getCantAsientos())
                        .append("\n");
            }
        }

        JOptionPane.showMessageDialog(panelConsulta, detalle.toString(), "Detalle", JOptionPane.INFORMATION_MESSAGE);
    }

    public Container getPanelConsulta() {
        return panelConsulta;
    }
}
