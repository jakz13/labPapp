package guiSesion;

import Logica.*;

import javax.swing.*;
import java.awt.*;

public class AgregarRutaVuelo {
    private JComboBox<Paquete> campoPaquete;
    private JComboBox<Aerolinea> campoAerolinea;
    private JList<RutaVuelo> campoRutaVuelo;
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
            Paquete paquete = (Paquete) campoPaquete.getSelectedItem();
            Aerolinea aerolinea = (Aerolinea) campoAerolinea.getSelectedItem();
            RutaVuelo rutaVuelo = (RutaVuelo) campoRutaVuelo.getSelectedValue();
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


            // Llamada a la lógica para agregar la ruta al paquete
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
        DefaultComboBoxModel<Paquete> modeloPaquetes = new DefaultComboBoxModel<>();
        for (Paquete p : ManejadorPaquete.getInstance().getPaquetesDisp()) {
            modeloPaquetes.addElement(p);
        }
        campoPaquete.setModel(modeloPaquetes);
        campoPaquete.setSelectedIndex(-1);
    }

    // Carga aerolíneas disponibles
    private void cargarAerolineas() {
        DefaultComboBoxModel<Aerolinea> modeloAerolinea = new DefaultComboBoxModel<>();
        for (Aerolinea a : ManejadorAerolinea.getInstance().getAerolineas()) {
            modeloAerolinea.addElement(a);
        }
        campoAerolinea.setModel(modeloAerolinea);
        campoAerolinea.setSelectedIndex(-1);
    }

    // Actualiza la lista de rutas según la aerolínea seleccionada
    private void actualizarRutas() {
        Aerolinea seleccionada = (Aerolinea) campoAerolinea.getSelectedItem();
        DefaultListModel<RutaVuelo> modeloRutas = new DefaultListModel<>();
        if (seleccionada != null) {
            for (RutaVuelo r : seleccionada.getRutasVuelo()) {
                modeloRutas.addElement(r);
            }
        }
        campoRutaVuelo.setModel(modeloRutas);
    }

    // Carga los tipos de asiento del enum
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
