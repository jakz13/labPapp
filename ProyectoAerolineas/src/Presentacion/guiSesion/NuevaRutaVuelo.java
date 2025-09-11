package guiSesion;

import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class NuevaRutaVuelo {
    private String aerolineaSeleccionada;
    private JPanel PanelAltaRuta;
    private JTextField NombreRuta;
    private JTextArea descripcion;
    private JTextField CostoTurista;
    private JTextField CostoEjecutivo;
    private JTextField Equipaje;
    private JButton crear;
    private JButton cancelarButton;
    private JList ListaAereolinea;
    private JComboBox<String> comboBoxCiudadOrigen;
    private JComboBox<String> comboBoxDestino;
    private JComboBox<String> comboBoxCategoria;

    public NuevaRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        // --- Aerolíneas ---
        DefaultListModel<String> modeloAerolineas = new DefaultListModel<>();
        for (var a : sistema.listarAerolineas()) {
            String item = a.getNickname() + " (" + a.getNombre() + ")";
            modeloAerolineas.addElement(item);
        }
        ListaAereolinea.setModel(modeloAerolineas);

        ListaAereolinea.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccionado = (String) ListaAereolinea.getSelectedValue();
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

            if (nombreRuta.isEmpty() || aerolineaSeleccionada == null || origen == null || destino == null || categorias.length == 0) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar todos los campos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                sistema.altaRutaVuelo(nombreRuta, Descripcion, sistema.obtenerAerolinea(aerolineaSeleccionada),
                        origen, destino, hora, fecha,
                        costoTurista, costoEjecutivo, equipaje, categorias);

                JOptionPane.showMessageDialog(null,
                        "Ruta creada correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

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

    public Container getPanelAltaRuta() {
        return PanelAltaRuta;
    }
}
