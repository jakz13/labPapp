package Presentacion.gui.guiSesion;

import Logica.Fabrica;
import Logica.ISistema;
import DataTypes.DtAerolinea;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class NuevaRutaVuelo {
    private String aerolineaSeleccionada;
    private JPanel PanelAltaRuta;
    private JTextField NombreRuta;
    private JTextArea descripcion;
    private JTextField descripcionCortaField; // Nuevo campo para descripción corta
    private JTextField CostoTurista;
    private JTextField CostoEjecutivo;
    private JTextField Equipaje;
    private JButton crear;
    private JButton cancelarButton;
    private JList<String> ListaAereolinea;
    private JComboBox<String> comboBoxCiudadOrigen;
    private JComboBox<String> comboBoxDestino;
    private JComboBox<String> comboBoxCategoria;
    private JScrollPane scrollPane;

    public NuevaRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        // --- Aerolíneas ---
        DefaultListModel<String> modeloAerolineas = new DefaultListModel<>();
        for (DtAerolinea a : sistema.listarAerolineas()) {
            String item = a.getNickname() + " (" + a.getNombre() + ")";
            modeloAerolineas.addElement(item);
        }
        ListaAereolinea.setModel(modeloAerolineas);

        ListaAereolinea.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccionado = ListaAereolinea.getSelectedValue();
                if (seleccionado != null) {
                    aerolineaSeleccionada = seleccionado.split(" ")[0]; // nickname
                }
            }
        });

        // --- Ciudades ---
        DefaultComboBoxModel<String> modeloOrigen = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> modeloDestino = new DefaultComboBoxModel<>();
        for (var c : sistema.listarCiudades()) {
            modeloOrigen.addElement(c.getNombre());
            modeloDestino.addElement(c.getNombre());
        }
        comboBoxCiudadOrigen.setModel(modeloOrigen);
        comboBoxDestino.setModel(modeloDestino);

        // --- Categorías ---
        DefaultComboBoxModel<String> modeloCategorias = new DefaultComboBoxModel<>();
        for (var cat : sistema.listarCategorias()) {
            modeloCategorias.addElement(cat.getNombre());
        }
        comboBoxCategoria.setModel(modeloCategorias);

        // --- Crear ruta ---
        crear.addActionListener(e -> {
            String nombreRuta = NombreRuta.getText().trim();
            String Descripcion = descripcion.getText().trim();
            String descripcionCorta = descripcionCortaField.getText().trim(); // Nueva descripción corta
            String origen = (String) comboBoxCiudadOrigen.getSelectedItem();
            String destino = (String) comboBoxDestino.getSelectedItem();
            String categoriaSel = (String) comboBoxCategoria.getSelectedItem();
            String[] categorias = categoriaSel != null ? new String[]{categoriaSel} : new String[0];

            String hora = "00:00";
            double costoTurista, costoEjecutivo, equipaje;

            try {
                costoTurista = Double.parseDouble(CostoTurista.getText().trim());
                costoEjecutivo = Double.parseDouble(CostoEjecutivo.getText().trim());
                equipaje = Double.parseDouble(Equipaje.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Costos inválidos. Deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate fecha = LocalDate.now();

            if (nombreRuta.isEmpty() || descripcionCorta.isEmpty() || aerolineaSeleccionada == null ||
                    origen == null || destino == null || categorias.length == 0) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar todos los campos, incluyendo la descripción corta.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Llamar al método actualizado con descripción corta
                sistema.altaRutaVuelo(nombreRuta, Descripcion, descripcionCorta,
                        sistema.obtenerAerolinea(aerolineaSeleccionada),
                        origen, destino, hora, fecha,
                        costoTurista, costoEjecutivo, equipaje, categorias);

                JOptionPane.showMessageDialog(null,
                        "Ruta creada correctamente. Estado: INGRESADA - Esperando aprobación del administrador.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // Limpiar campos después de crear
                limpiarCampos();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null,
                        ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Cancelar ---
        cancelarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(PanelAltaRuta);
            if (topFrame != null) topFrame.dispose();
        });
    }

    private void limpiarCampos() {
        NombreRuta.setText("");
        descripcion.setText("");
        descripcionCortaField.setText("");
        CostoTurista.setText("");
        CostoEjecutivo.setText("");
        Equipaje.setText("");
        ListaAereolinea.clearSelection();
        comboBoxCiudadOrigen.setSelectedIndex(0);
        comboBoxDestino.setSelectedIndex(0);
        comboBoxCategoria.setSelectedIndex(0);
    }

    public Container getPanelAltaRuta() {
        return PanelAltaRuta;
    }

    // Método para crear los componentes de la GUI (necesario para el diseñador de GUI)
    private void createUIComponents() {
        // Inicialización de componentes si es necesario
    }
}