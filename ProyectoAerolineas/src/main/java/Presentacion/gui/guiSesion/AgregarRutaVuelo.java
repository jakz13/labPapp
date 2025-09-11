package Presentacion.gui.guiSesion;

import DataTypes.DtAerolinea;
import DataTypes.DtRutaVuelo;
import DataTypes.DtPaquete;
import Logica.Fabrica;
import Logica.ISistema;
import Logica.TipoAsiento;

import javax.swing.*;
import java.awt.*;

public class AgregarRutaVuelo {
    private JComboBox<DtPaquete> campoPaquete;
    private JComboBox<DtAerolinea> campoAerolinea;
    private JList<DtRutaVuelo> campoRutaVuelo;
    private JComboBox<TipoAsiento> campoTipoAsiento;
    private JTextField campoCantidadAsiento;
    private JButton añadirButton;
    private JButton cancelarButton;
    private JPanel JPanelAgregar;

    public AgregarRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        // Cargar los paquetes, aerolíneas y tipos de asiento al iniciar
        cargarPaquetes();
        cargarAerolineas();
        cargarTipoAsiento();

        // Actualizar la lista de rutas cuando se selecciona otra aerolínea
        campoAerolinea.addActionListener(e -> actualizarRutas());

        // Acción del botón Añadir
        añadirButton.addActionListener(e -> {
            DtPaquete paquete = (DtPaquete) campoPaquete.getSelectedItem();
            DtAerolinea aerolinea = (DtAerolinea) campoAerolinea.getSelectedItem();
            DtRutaVuelo rutaVuelo = (DtRutaVuelo) campoRutaVuelo.getSelectedValue();
            TipoAsiento tipoAsiento = (TipoAsiento) campoTipoAsiento.getSelectedItem();
            String cantidadAsientoStr = campoCantidadAsiento.getText().trim();

            // Validar campos
            if (paquete == null || aerolinea == null || rutaVuelo == null ||
                    tipoAsiento == null || cantidadAsientoStr.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar todos los campos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cantidadAsiento;
            try {
                cantidadAsiento = Integer.parseInt(cantidadAsientoStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Cantidad de Asientos debe ser un número entero válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                sistema.altaRutaPaquete(paquete.getNombre(), rutaVuelo.getNombre(), cantidadAsiento, tipoAsiento);
                JOptionPane.showMessageDialog(null,
                        "Ruta de vuelo agregada correctamente al paquete.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error al agregar la ruta: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del botón Cancelar
        cancelarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(campoPaquete);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });
    }

    // Carga paquetes disponibles
    private void cargarPaquetes() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultComboBoxModel<DtPaquete> modeloPaquetes = new DefaultComboBoxModel<>();

        for (DtPaquete p : sistema.getPaquetesDisp()) {
            modeloPaquetes.addElement(p);
        }

        campoPaquete.setModel(modeloPaquetes);
        campoPaquete.setSelectedIndex(-1);

        // Listener opcional para mostrar detalle en otro componente
        campoPaquete.addActionListener(e -> {
            DtPaquete seleccionado = (DtPaquete) campoPaquete.getSelectedItem();
            if (seleccionado != null) {
                // Mostrar detalles en un TextArea, Label o popup
                System.out.println(seleccionado.toString());
            }
        });
    }


    private void cargarAerolineas() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultComboBoxModel<DtAerolinea> modeloAerolinea = new DefaultComboBoxModel<>();
        for (DtAerolinea a : sistema.getAerolineas()) {
            modeloAerolinea.addElement(a);
        }
        campoAerolinea.setModel(modeloAerolinea);
        campoAerolinea.setSelectedIndex(-1);
    }

    private void actualizarRutas() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DtAerolinea seleccionada = (DtAerolinea) campoAerolinea.getSelectedItem();
        DefaultListModel<DtRutaVuelo> modeloRutas = new DefaultListModel<>();
        if (seleccionada != null) {
            for (DtRutaVuelo r : sistema.listarRutasPorAerolinea(seleccionada.getNombre())) {
                modeloRutas.addElement(r);
            }
        }
        campoRutaVuelo.setModel(modeloRutas);
    }

    private void cargarTipoAsiento() {
        DefaultComboBoxModel<TipoAsiento> modeloAsientos = new DefaultComboBoxModel<>();
        for (TipoAsiento t : TipoAsiento.values()) {
            modeloAsientos.addElement(t);
        }
        campoTipoAsiento.setModel(modeloAsientos);
        campoTipoAsiento.setSelectedIndex(-1);
    }

    public Container getPanelDeSesion() {
        return JPanelAgregar;
    }
}
