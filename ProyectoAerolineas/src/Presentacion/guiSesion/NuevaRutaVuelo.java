package guiSesion;

import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class NuevaRutaVuelo {
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

    public NuevaRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        crear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                {
                    String nombreRuta = NombreRuta.getText().trim();
                    String origen = Origen.getText().trim();
                    String destino = Destino.getText().trim();
                    double costoTurista = Double.parseDouble(CostoTurista.getText().trim());
                    double costoEjecutivo = Double.parseDouble(CostoEjecutivo.getText().trim());
                    String Aereolinea = AereolineaEncargada.getText().trim();
                    double equipaje = Double.parseDouble(Equipaje.getText().trim());
                    LocalDate fecha = LocalDate.now();
                    if (nombreRuta.isEmpty() || origen.isEmpty() || destino.isEmpty() || costoTurista < 0 || costoEjecutivo < 0 || Aereolinea.isEmpty() || equipaje < 0) {
                        JOptionPane.showMessageDialog(null,
                                "Debe completar todos los campos.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    try {
                        sistema.altaRutaVuelo(nombreRuta, Aereolinea, origen, destino, costoTurista, costoEjecutivo, equipaje, fecha);
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


