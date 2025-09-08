package guiSesion;

import Logica.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class VerUsuarios {
    private JPanel panelConsultaRuta;
    private JButton cerrarButton;
    private JComboBox<String> comboBoxUsuarios;
    private JTextArea textAreaDatosUsuario;
    private JTextArea textAreaDatosAdicionales;
    private JList<Object> listAdicionales;

    private final ISistema sistema;

    public VerUsuarios() {
        sistema = Fabrica.getInstance().getISistema();

        // --- Cargar todos los usuarios ---
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

            if (seleccionado.startsWith("Cliente:")) {
                String nick = seleccionado.substring("Cliente:".length());
                mostrarCliente(nick.trim());
            } else if (seleccionado.startsWith("Aerolinea:")) {
                String nick = seleccionado.substring("Aerolinea:".length());
                mostrarAerolinea(nick.trim());
            }
        });

        // --- Acción al seleccionar adicional ---
        listAdicionales.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Object seleccionado = listAdicionales.getSelectedValue();
                if (seleccionado == null) return;

                if (seleccionado instanceof RutaVuelo) {
                    mostrarDetalleRuta((RutaVuelo) seleccionado);
                } else if (seleccionado instanceof Vuelo) {
                    mostrarDetalleVuelo((Vuelo) seleccionado);
                } else if (seleccionado instanceof Paquete) {
                    mostrarDetallePaquete((Paquete) seleccionado);
                }
            }
        });
        // Listar reservas y paquetes
        DefaultListModel<Object> modelo = new DefaultListModel<>();
        for (Reserva r : c.getReservas().values()) {
            modelo.addElement(r.getVuelo());
        }
        for (Paquete p : c.getPaquetesComprados()) {
            modelo.addElement(p);
        }
        listAdicionales.setModel(modelo);
        textAreaDatosAdicionales.setText("Reservas y Paquetes del cliente");
    }

    // --- Mostrar aerolínea ---
    private void mostrarAerolinea(String nickname) {
        Aerolinea a = sistema.obtenerAerolinea(nickname);
        if (a == null) return;

        textAreaDatosUsuario.setText(
                "Aerolinea: " + a.getNombre() + "\n" +
                        "Email: " + a.getEmail() + "\n" +
                        "Sitio Web: " + a.getSitioWeb() + "\n" +
                        "Descripcion: " + a.getDescripcion() + "\n"
        );


        cerrarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panelConsultaRuta);
            if (topFrame != null) topFrame.dispose();
        });
    }

    // --- Mostrar cliente ---
    private void mostrarCliente(String nickname) {
        Cliente c = sistema.obtenerCliente(nickname);
        if (c == null) return;

        textAreaDatosUsuario.setText(
                "Cliente: " + c.getNombre() + " " + c.getApellido() + "\n" +
                        "Email: " + c.getEmail() + "\n" +
                        "Fecha Nacimiento: " + c.getFechaNacimiento() + "\n" +
                        "Nacionalidad: " + c.getNacionalidad() + "\n"
        );

