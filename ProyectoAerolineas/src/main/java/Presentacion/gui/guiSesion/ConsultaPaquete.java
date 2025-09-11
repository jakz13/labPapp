package Presentacion.gui.guiSesion;

import DataTypes.*;
import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;

public class ConsultaPaquete {
    private JPanel PanelConsultaPaquete;
    private JComboBox<DtPaquete> comboboxPaquetes;
    private JTextPane InfoPaquetes;
    private JComboBox<DtRutaVuelo> comboBoxRutaVuelo;
    private JTextArea textAreaInfoRuta;
    private JButton cerrarButton;

    private ISistema sistema;

    public ConsultaPaquete() {
        sistema = Fabrica.getInstance().getISistema();

        // cargar paquetes al iniciar
        cargarPaquetes();

        // al seleccionar un paquete mostrar su información
        comboboxPaquetes.addActionListener(e -> actualizarInfoPaquete());

        // al seleccionar una ruta dentro del paquete mostrar su info detallada
        comboBoxRutaVuelo.addActionListener(e -> actualizarInfoRuta());

        // botón cerrar
        cerrarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(PanelConsultaPaquete);
            if (topFrame != null) {
                topFrame.dispose();
            }
        });
    }

    private void cargarPaquetes() {
        DefaultComboBoxModel<DtPaquete> modeloPaquetes = new DefaultComboBoxModel<>();
        for (DtPaquete p : sistema.listarPaquetes()) {
            modeloPaquetes.addElement(p);
        }
        comboboxPaquetes.setModel(modeloPaquetes);
        comboboxPaquetes.setSelectedIndex(-1);
    }

    private void actualizarInfoPaquete() {
        DtPaquete seleccionado = (DtPaquete) comboboxPaquetes.getSelectedItem();
        if (seleccionado != null) {
            StringBuilder info = new StringBuilder();
            info.append("Nombre: ").append(seleccionado.getNombre()).append("\n")
                    .append("Descripción: ").append(seleccionado.getDescripcion()).append("\n")
                    .append("Costo base: ").append(seleccionado.getCosto()).append("\n")
                    .append("Descuento: ").append(seleccionado.getDescuentoPorc()).append("%\n")
                    .append("Validez: ").append(seleccionado.getPeriodoValidezDias()).append(" días\n");

            info.append("Rutas incluidas:\n");

            DefaultComboBoxModel<DtRutaVuelo> modeloRutas = new DefaultComboBoxModel<>();

            for (DtItemPaquete item : seleccionado.getItems()) {
                DtRutaVuelo r = item.getRutaVuelo();
                if (r != null) {
                    modeloRutas.addElement(r);
                    info.append("  - ").append(r.getNombre())
                            .append(" (").append(item.getTipoAsiento())
                            .append(", Asientos: ").append(item.getCantAsientos())
                            .append(")\n");
                }
            }

            comboBoxRutaVuelo.setModel(modeloRutas);
            InfoPaquetes.setText(info.toString());
        } else {
            InfoPaquetes.setText("");
            comboBoxRutaVuelo.setModel(new DefaultComboBoxModel<>());
        }
        textAreaInfoRuta.setText(""); // limpiar info de ruta
    }

    private void actualizarInfoRuta() {
        DtRutaVuelo seleccionada = (DtRutaVuelo) comboBoxRutaVuelo.getSelectedItem();
        if (seleccionada != null) {
            StringBuilder info = new StringBuilder();
            info.append("Nombre Ruta: ").append(seleccionada.getNombre()).append("\n")
                    .append("Origen: ").append(seleccionada.getCiudadOrigen()).append("\n")
                    .append("Destino: ").append(seleccionada.getCiudadDestino()).append("\n")
                    .append("Duración estimada: ").append(seleccionada.getHora()).append("\n")
                    .append("Fecha de Alta: ").append(seleccionada.getFechaAlta()).append("\n")
                    .append("Costo Turista: ").append(seleccionada.getCostoTurista()).append("\n")
                    .append("Costo Ejecutivo: ").append(seleccionada.getCostoEjecutivo()).append("\n");

            textAreaInfoRuta.setText(info.toString());
        } else {
            textAreaInfoRuta.setText("");
        }
    }

    public Container getPanelConsultaPaquete() {
        return PanelConsultaPaquete;
    }
}
