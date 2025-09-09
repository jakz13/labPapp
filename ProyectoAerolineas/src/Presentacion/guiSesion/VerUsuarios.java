package guiSesion;

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

            panelBotonesDinamicos.removeAll(); // limpiar botones anteriores
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

    // --- Generar botones dinámicos para cliente ---
    private void generarBotonesCliente(String nickname) {
        JButton btnReservas = new JButton("Ver Reservas");
        btnReservas.addActionListener(e -> {
            List<Object> datos = sistema.obtenerDatosAdicionalesUsuario(nickname);
            mostrarListaEnDialog("Reservas del cliente", datos);
        });

        JButton btnPaquetes = new JButton("Ver Paquetes");
        btnPaquetes.addActionListener(e -> {
            List<Object> datos = sistema.obtenerDatosAdicionalesUsuario(nickname);
            mostrarListaEnDialog("Paquetes del cliente", datos);
        });

        panelBotonesDinamicos.add(btnReservas);
        panelBotonesDinamicos.add(btnPaquetes);
    }

    // --- Generar botones dinámicos para aerolínea ---
    private void generarBotonesAerolinea(String nickname) {
        JButton btnRutas = new JButton("Ver Rutas de Vuelo");
        btnRutas.addActionListener(e -> {
            List<RutaVuelo> rutas = sistema.listarRutasPorAerolinea(nickname);
            mostrarListaEnDialog("Rutas de vuelo de la aerolínea", rutas);
        });

        panelBotonesDinamicos.add(btnRutas);
    }

    // --- Mostrar resultados en un diálogo con JList ---
    private void mostrarListaEnDialog(String titulo, List<?> elementos) {
        DefaultListModel<Object> modelo = new DefaultListModel<>();
        for (Object obj : elementos) {
            modelo.addElement(obj);
        }
        JList<Object> lista = new JList<>(modelo);
        JOptionPane.showMessageDialog(panelConsulta, new JScrollPane(lista), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public Container getPanelConsulta() {
        return panelConsulta;
    }
}
