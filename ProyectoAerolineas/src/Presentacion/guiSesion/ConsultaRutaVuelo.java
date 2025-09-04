package guiSesion;

import Logica.*;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ConsultaRutaVuelo {
    private JPanel panel1;
    private JPanel panelConsultaRuta;
    private JComboBox<Aerolinea> comboBoxAerolinea;
    private JList<RutaVuelo> listRutas;
    private JList<Vuelo> listVuelosAsociados;
    private JTextArea textAreaDatosVuelo;
    private JButton cerrarButton;

    public ConsultaRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        DefaultComboBoxModel<Aerolinea> modeloAerolinea = new DefaultComboBoxModel<>();
        for (Aerolinea a : sistema.listarAerolineas()) {
            modeloAerolinea.addElement(a);
        }
        comboBoxAerolinea.setModel(modeloAerolinea);
        comboBoxAerolinea.setSelectedIndex(-1);

        comboBoxAerolinea.addActionListener(e -> {
            Aerolinea seleccionada = (Aerolinea) comboBoxAerolinea.getSelectedItem();
            DefaultListModel<RutaVuelo> modeloRutas = new DefaultListModel<>();
            if (seleccionada != null) {
                for (RutaVuelo r : seleccionada.getRutasVuelo()) {
                    modeloRutas.addElement(r);
                }
            }
            listRutas.setModel(modeloRutas);
            textAreaDatosVuelo.setText("");
        });


        listRutas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                RutaVuelo seleccionada = listRutas.getSelectedValue();
                DefaultListModel<Vuelo> modeloVuelos = new DefaultListModel<>();
                if (seleccionada != null) {

                    StringBuilder infoRuta = new StringBuilder();
                    infoRuta.append("Nombre: ").append(seleccionada.getNombre()).append("\n")
                            .append("Origen: ").append(seleccionada.getCiudadOrigen()).append("\n")
                            .append("Destino: ").append(seleccionada.getCiudadDestino()).append("\n")
                            .append("Duración Estimada: ").append(seleccionada.getHora()).append(" horas\n")
                            .append("Vuelos Asociados: ").append(seleccionada.getVuelos().size()).append("\n");

                    textAreaDatosVuelo.setText(infoRuta.toString());

                    for (Vuelo v : seleccionada.getVuelos().values()) {
                        modeloVuelos.addElement(v);
                    }
                }
                listVuelosAsociados.setModel(modeloVuelos);
            }
        });

        listVuelosAsociados.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Vuelo vuelo = listVuelosAsociados.getSelectedValue();
                if (vuelo != null) {
                    StringBuilder infoVuelo = new StringBuilder();
                    infoVuelo.append("Nombre: ").append(vuelo.getNombre()).append("\n")
                            .append("Fecha: ").append(vuelo.getFecha()).append("\n")
                            .append("Duración: ").append(vuelo.getDuracion()).append(" días\n")
                            .append("Asientos Ejecutivos: ").append(vuelo.getAsientosEjecutivo()).append("\n")
                            .append("Asientos Turista: ").append(vuelo.getAsientosTurista()).append("\n")
                            .append("Fecha de Alta: ").append(vuelo.getFechaAlta()).append("\n")
                            .append("Reservas:\n");

                    Map<String, Reserva> reservas = vuelo.getReservas();
                    if (reservas.isEmpty()) {
                        infoVuelo.append("  Ninguna reserva\n");
                    } else {
                        for (Map.Entry<String, Reserva> entry : reservas.entrySet()) {
                            Reserva r = entry.getValue();
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
