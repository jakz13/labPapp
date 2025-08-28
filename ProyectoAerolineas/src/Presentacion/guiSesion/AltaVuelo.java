package guiSesion;

import Logica.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class AltaVuelo {
    private JPanel panelDeVuelo;
    private JTextField campoAereolinea;
    private JLabel Aereolinea;
    private JLabel Ruta;
    private JTextField campoRuta;
    private JButton crearButton;
    private JButton cancelarButton;
    private JList ListaAereolineas;
    private JList ListaRutasDeVuelo;
    private JTextField NOmVuelo;
    private JComboBox Dia;
    private JComboBox Ano;
    private JComboBox Mes;
    private JTextField Duracion;
    private JTextField CantTuristas;
    private JTextField CantEjecutivo;

    public AltaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultListModel<String> modeloAerolineas = new DefaultListModel<>();
        for (Aerolinea a : sistema.listarAerolineas()) {
            modeloAerolineas.addElement(a.getNombre()); // o getNickname()
        }
        ListaAereolineas.setModel(modeloAerolineas);

        ListaAereolineas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String aerolineaSeleccionada = (String) ListaAereolineas.getSelectedValue();
                if (aerolineaSeleccionada != null) {
                    DefaultListModel<String> modeloRutas = new DefaultListModel<>();
                    for (RutaVuelo r : sistema.listarRutasPorAerolinea(aerolineaSeleccionada)) {
                        modeloRutas.addElement(r.getNombre());
                    }
                    ListaRutasDeVuelo.setModel(modeloRutas);
                }
            }
        });
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String aereolinea = campoAereolinea.getText();
                String ruta = campoRuta.getText();
                int duracion = Integer.parseInt(Duracion.getText());
                String Vuelo = NOmVuelo.getText();
                int turista = Integer.parseInt(CantTuristas.getText());
                int ejecutivo = Integer.parseInt(CantEjecutivo.getText());

                String diaStr = (String) Dia.getSelectedItem();
                String mesStr = (String) Mes.getSelectedItem();
                String anioStr = (String) Ano.getSelectedItem();

                LocalDate fechaAlta = LocalDate.now();

                if (aereolinea.isEmpty() || ruta.isEmpty() || duracion > 0 || turista > 0 || ejecutivo > 0 || Vuelo.isEmpty()) {
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
                boolean existeAerolinea = sistema.listarAerolineas()
                        .stream().anyMatch(a -> a.getNombre().equalsIgnoreCase(aereolinea));

                if (!existeAerolinea) {
                    JOptionPane.showMessageDialog(null,
                            "La aerolínea ingresada no existe.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean existeRuta = sistema.listarRutasPorAerolinea(aereolinea)
                        .stream().anyMatch(r -> r.getNombre().equalsIgnoreCase(ruta));

                if (!existeRuta) {
                    JOptionPane.showMessageDialog(null,
                            "La ruta ingresada no existe para esa aerolínea.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int dia = Integer.parseInt(diaStr);
                int mes = Integer.parseInt(mesStr);
                int anio = Integer.parseInt(anioStr);

                LocalDate fecha = LocalDate.of(anio, mes, dia);

                try {
                    sistema.altaVuelo(Vuelo, aereolinea, ruta,fecha,  duracion, turista, ejecutivo, fechaAlta);
                    JOptionPane.showMessageDialog(null,
                            "Ruta creada correctamente.",
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

