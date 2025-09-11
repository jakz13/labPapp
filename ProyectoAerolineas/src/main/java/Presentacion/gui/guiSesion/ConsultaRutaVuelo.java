package Presentacion.gui.guiSesion;

import DataTypes.DtAerolinea;
import DataTypes.DtReserva;
import DataTypes.DtRutaVuelo;
import DataTypes.DtVuelo;
import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.util.Map;

public class ConsultaRutaVuelo {
    private JPanel panel1;
    private JPanel panelConsultaRuta;
    private JComboBox<DtAerolinea> comboBoxAerolinea;
    private JList<DtRutaVuelo> listRutas;
    private JList<DtVuelo> listVuelosAsociados;
    private JTextArea textAreaDatosRuta;
    private JButton cerrarButton;
    private JTextArea textAreaDatosVuelo;

    public ConsultaRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        DefaultComboBoxModel<DtAerolinea> modeloAerolinea = new DefaultComboBoxModel<>();
        for (DtAerolinea a : sistema.listarAerolineas()) {
            modeloAerolinea.addElement(a);
        }
        comboBoxAerolinea.setModel(modeloAerolinea);
        comboBoxAerolinea.setSelectedIndex(-1);

        comboBoxAerolinea.addActionListener(e -> {
            DtAerolinea seleccionada = (DtAerolinea) comboBoxAerolinea.getSelectedItem();
            DefaultListModel<DtRutaVuelo> modeloRutas = new DefaultListModel<>();
            if (seleccionada != null) {
                for (DtRutaVuelo r : seleccionada.getRutas()) {
                    modeloRutas.addElement(r);
                }
            }
            listRutas.setModel(modeloRutas);
            textAreaDatosRuta.setText("");
        });

        listRutas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                DtRutaVuelo seleccionada = listRutas.getSelectedValue();
                DefaultListModel<DtVuelo> modeloVuelos = new DefaultListModel<>();
                if (seleccionada != null) {

                    StringBuilder infoRuta = new StringBuilder();
                    infoRuta.append("Nombre: ").append(seleccionada.getNombre()).append("\n")
                            .append("Origen: ").append(seleccionada.getCiudadOrigen()).append("\n")
                            .append("Destino: ").append(seleccionada.getCiudadDestino()).append("\n")
                            .append("Duración Estimada: ").append(seleccionada.getHora()).append(" Horas\n")
                            .append("Vuelos Asociados: ").append(seleccionada.getVuelos().size()).append("\n");

                    textAreaDatosRuta.setText(infoRuta.toString());

                    for (DtVuelo v : seleccionada.getVuelos()) {
                        modeloVuelos.addElement(v);
                    }
                }
                listVuelosAsociados.setModel(modeloVuelos);
            }
        });

        listVuelosAsociados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                DtVuelo vuelo = listVuelosAsociados.getSelectedValue();
                if (vuelo != null) {
                    StringBuilder infoVuelo = new StringBuilder();
                    infoVuelo.append("Nombre: ").append(vuelo.getNombre()).append("\n")
                            .append("Fecha: ").append(vuelo.getFecha()).append("\n")
                            .append("Duración: ").append(vuelo.getDuracion()).append(" Hora\n")
                            .append("Asientos Ejecutivos: ").append(vuelo.getAsientosEjecutivo()).append("\n")
                            .append("Asientos Turista: ").append(vuelo.getAsientosTurista()).append("\n")
                            .append("Fecha de Alta: ").append(vuelo.getFechaAlta()).append("\n")
                            .append("Reservas:\n");

                    Map<String, DtReserva> reservas = (Map<String, DtReserva>) vuelo.getReservas();

                    if (reservas.isEmpty()) {
                        infoVuelo.append("  Ninguna reserva\n");
                    } else {
                        for (Map.Entry<String, DtReserva> entry : reservas.entrySet()) {
                            DtReserva r = entry.getValue();
                            infoVuelo.append("  ID: ").append(r.getId())
                                    .append(", Costo: ").append(r.getCosto())
                                    .append(", Tipo: ").append(r.getTipoAsiento())
                                    .append(", Pasajes: ").append(r.getCantidadPasajes())
                                    .append("\n");
                        }
                    }

                    textAreaDatosVuelo.setText(infoVuelo.toString());
                }
            }
        });

        cerrarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panelConsultaRuta);
            if (topFrame != null) topFrame.dispose();
        });
    }

    public JPanel getPanelConsultaRuta() {
        return panelConsultaRuta;
    }
}
