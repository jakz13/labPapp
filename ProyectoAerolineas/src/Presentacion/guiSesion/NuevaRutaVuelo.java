package guiSesion;

import Logica.Aerolinea;
import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class NuevaRutaVuelo {
    private String aerolineaSeleccionada;
    private JPanel PanelAltaRuta;
    private JTextField NombreRuta;
    private JTextField AereolineaEncargada;
    private JTextField Origen;
    private JTextField Destino;
    private JTextField CostoTurista;
    private JTextField CostoEjecutivo;
    private JButton crear;
    private JButton cancelarButton;
    private JTextField Equipaje;
    private JList ListaAereolinea;
    private JTextArea descripcion;
    private JTextField Categoria;

    public NuevaRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultListModel<String> modeloAerolineas = new DefaultListModel<>();
        for (Aerolinea a : sistema.listarAerolineas()) {
            String item = a.getNickname() + " (" + a.getNombre() + ")";
            modeloAerolineas.addElement(item);
        }
        ListaAereolinea.setModel(modeloAerolineas);
        ListaAereolinea.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccionado = (String) ListaAereolinea.getSelectedValue();
                if (seleccionado != null) {
                    aerolineaSeleccionada = seleccionado.split(" ")[0];
                }
            }
        });

        crear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    Aerolinea aerolinea = sistema.obtenerAerolinea(aerolineaSeleccionada);
                    String nombreRuta = NombreRuta.getText().trim();
                    String Descripcion = descripcion.getText().trim();
                    String origen = Origen.getText().trim();
                    String destino = Destino.getText().trim();
                    String[] categoria = new String[]{Categoria.getText().trim()};
                    String hora = "00:00";
                    double costoTurista = Double.parseDouble(CostoTurista.getText().trim());
                    double costoEjecutivo = Double.parseDouble(CostoEjecutivo.getText().trim());
                    double equipaje = Double.parseDouble(Equipaje.getText().trim());
                    LocalDate fecha = LocalDate.now();
                    if (nombreRuta.isEmpty() || origen.isEmpty() || destino.isEmpty() || costoTurista < 0 || costoEjecutivo < 0 || aerolineaSeleccionada == null || aerolineaSeleccionada.isEmpty() || equipaje < 0) {
                        JOptionPane.showMessageDialog(null,
                                "Debe completar todos los campos.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    try {
                        sistema.altaRutaVuelo(nombreRuta, Descripcion, aerolinea, origen, destino, hora,fecha, costoTurista, costoEjecutivo, equipaje, categoria);
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
            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(PanelAltaRuta);
                    topFrame.dispose();
            }
        });
    }

    public Container getPanelAltaRuta() {
        return PanelAltaRuta;
    }
}


