package Presentacion.gui.guiSesion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CompraPaquete {
    private JPanel panel1;
    private JComboBox PaqueteSeleccionado;
    private JComboBox ClienteElegido;
    private JTextField CostoCalculado;
    private JButton cancelarCompraButton;
    private JButton aceptarCompraButton;
    private JComboBox Dia;
    private JComboBox Ano;
    private JComboBox Mes;

    public CompraPaquete() {
        aceptarCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
