package guiSesion;

import Logica.Fabrica;
import Logica.ISistema;
import Logica.Sistema;
import javax.swing.*;
import java.awt.*;

public class AltaPaquete {
    private JPanel panel1;
    private JTextField campoNombre;
    private JTextField campoDescripcion;
    private JTextField campoCosto;
    private JTextField campoDescuento;
    private JTextField campoValidez;
    private JButton crearButton;
    private JButton cancelarButton;
    private JLabel Nombre;
    private JLabel Descripcion;
    private JLabel Descuento;
    private JLabel Validez;

    public AltaPaquete() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        crearButton.addActionListener(e -> {
            // Lógica para crear el paquete
            String nombre = campoNombre.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String descuentoStr = campoDescuento.getText().trim();
            String validezStr = campoValidez.getText().trim();

            if (nombre.isEmpty() || descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar los campos obligatorios: Nombre, Descripción y Costo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double costo = 0;
            int descuento = 0;
            int validez = 0;

            sistema.altaPaquete(nombre, descripcion, costo, descuento, validez);

            JOptionPane.showMessageDialog(null,
                    "Paquete creado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        cancelarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            topFrame.dispose();
        });
    }

    public Container getPanel1() {
        return panel1;
    }
/*
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
*/
}
