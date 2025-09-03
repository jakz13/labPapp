package guiSesion;

import Logica.*;

import javax.swing.*;

public class ConsultaVuelo {
    private JComboBox comboBoxAerolinea;
    private JList listRutaVuelo;
    private JList listVuelo;
    private JTextArea textAreaDatosVuelo;
    private JButton cerrarButton;

    public ConsultaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        // Cargar aerolíneas al iniciar
        cargarAerolineas();

        // Actualizar la lista de rutas cuando se selecciona otra aerolínea
        comboBoxAerolinea.addActionListener(e -> actualizarRutas());

        listRutaVuelo.addListSelectionListener(e -> actualizarVuelos());

        listVuelo.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // evita que se dispare dos veces
                Vuelo seleccionada = (Vuelo) listVuelo.getSelectedValue();
                if (seleccionada != null) {
                    // Mostrar información en el JTextArea
                    String info = "Nombre: " + seleccionada.getNombre() + "\n"
                            + "Nombre de Ruta: " + seleccionada.getNombreRuta() + "\n"
                            + "Duración: " + seleccionada.getDuracion() + "\n"
                            + "Fecha: " + seleccionada.getFecha() + "\n"
                            + "Asientos Ejecutivos: " + seleccionada.getAsientosEjecutivo() + "\n"
                            + "Asientos Turista: " + seleccionada.getAsientosTurista() + "\n"
                            + "Fecha de Alta: " + seleccionada.getFechaAlta() + "\n"
                            + "Disponibilidad: " + seleccionada.getReservas();
                    textAreaDatosVuelo.setText(info);
                } else {
                    textAreaDatosVuelo.setText(""); // limpiar si no hay selección
                }
            }
        });

        // Acción del botón Cerrar
        cerrarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(comboBoxAerolinea);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });
    }



    // Carga aerolíneas disponibles
    private void cargarAerolineas() {
        DefaultComboBoxModel<Aerolinea> modeloAerolinea = new DefaultComboBoxModel<>();
        for (Aerolinea a : ManejadorAerolinea.getInstance().getAerolineas()) {
            modeloAerolinea.addElement(a);
        }
        comboBoxAerolinea.setModel(modeloAerolinea);
        comboBoxAerolinea.setSelectedIndex(-1);
    }

    private void actualizarRutas() {
        Aerolinea seleccionada = (Aerolinea) comboBoxAerolinea.getSelectedItem();
        DefaultListModel<RutaVuelo> modeloRutas = new DefaultListModel<>();
        if (seleccionada != null) {
            for (RutaVuelo r : seleccionada.getRutasVuelo()) {
                modeloRutas.addElement(r);
            }
        }
        listRutaVuelo.setModel(modeloRutas);
    }

    private void actualizarVuelos() {
        Aerolinea seleccionada = (Aerolinea) listRutaVuelo.getSelectedValue();
        DefaultListModel<RutaVuelo> modeloRutas = new DefaultListModel<>();
        if (seleccionada != null) {
            for (RutaVuelo r : seleccionada.getRutasVuelo()) {
                modeloRutas.addElement(r);
            }
        }
        listVuelo.setModel(modeloRutas);
    }
}
