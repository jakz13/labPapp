package Presentacion.gui.guiSesion;

import logica.ISistema;
import logica.Fabrica;
import DataTypes.*;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AltaVuelo {
    private JPanel panelDeVuelo;
    private JTextField campoAereolinea;
    private JTextField campoRuta;
    private JButton crearButton;
    private JButton cancelarButton;
    private JList listaAereolineas;
    private JList listaRutasDeVuelo;
    private JTextField nomVuelo;
    private JComboBox dia;
    private JComboBox ano;
    private JComboBox mes;
    private JTextField duracion;
    private JTextField cantTuristas;
    private JTextField cantEjecutivo;

    public AltaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultListModel<String> modeloAerolineas = new DefaultListModel<>();
        for (DtAerolinea a : sistema.listarAerolineas()) {
            String item = a.getNickname() + " (" + a.getNombre() + ")";
            modeloAerolineas.addElement(item);
        }
        listaAereolineas.setModel(modeloAerolineas);

        listaAereolineas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String item = (String) listaAereolineas.getSelectedValue();
                if (item != null) {
                    String nickname = item.split(" ")[0];
                    String nombre = item.substring(item.indexOf("(") + 1, item.indexOf(")"));

                    campoAereolinea.setText(nombre);

                    List<DtRutaVuelo> rutas = sistema.listarRutasPorAerolinea(nickname);
                    DefaultListModel<String> modeloRutas = new DefaultListModel<>();
                    for (DtRutaVuelo r : rutas) {
                        modeloRutas.addElement(r.getNombre());
                    }
                    listaRutasDeVuelo.setModel(modeloRutas);
                }
            }
        });
        listaRutasDeVuelo.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String rutaSeleccionada = (String) listaRutasDeVuelo.getSelectedValue();
                if (rutaSeleccionada != null) {
                    campoRuta.setText(rutaSeleccionada);
                }
            }
        });

        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aereolinea = (String) listaAereolineas.getSelectedValue();
                int dur = Integer.parseInt(duracion.getText());
                String Vuelo = nomVuelo.getText();
                int turista = Integer.parseInt(cantTuristas.getText());
                int ejecutivo = Integer.parseInt(cantEjecutivo.getText());
                String Ruta = (String) listaRutasDeVuelo.getSelectedValue();
                String diaStr = (String) dia.getSelectedItem();
                String mesStr = (String) mes.getSelectedItem();
                String anioStr = (String) ano.getSelectedItem();

                LocalDate fechaAlta = LocalDate.now();

                if (Vuelo.isEmpty() || turista <= 0 || dur <= 0 || ejecutivo <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "Debe completar todos los campos.",
                            "Error de salame", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (diaStr.equals("Día") || mesStr.equals("Mes") || anioStr.equals("Año")) {
                    JOptionPane.showMessageDialog(null,
                            "Debe seleccionar una fecha válida.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int dia = Integer.parseInt(diaStr);
                int mes = Integer.parseInt(mesStr);
                int anio = Integer.parseInt(anioStr);

                LocalDate fecha = LocalDate.of(anio, mes, dia);

                try {
                    sistema.altaVuelo(Vuelo, aereolinea, Ruta, fecha, dur, turista, ejecutivo, fechaAlta, null);
                    JOptionPane.showMessageDialog(null,
                            "Vuelo creado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panelDeVuelo);
                topFrame.dispose();
            }
        });
    }

    public Container getpanelDeVuelo() {
        return panelDeVuelo;
    }
}
