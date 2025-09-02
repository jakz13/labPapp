package guiSesion;

import Logica.Fabrica;
import Logica.ISistema;
import Logica.Sistema;
import javax.swing.*;

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
    private JLabel Costo;
    private JLabel Descuento;
    private JLabel Validez;

    public AltaPaquete() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        crearButton.addActionListener(e -> {
            // Lógica para crear el paquete
            String nombre = campoNombre.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String costoStr = campoCosto.getText().trim();
            String descuentoStr = campoDescuento.getText().trim();
            String validezStr = campoValidez.getText().trim();

            if (nombre.isEmpty() || descripcion.isEmpty() || costoStr.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar los campos obligatorios: Nombre, Descripción y Costo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double costo;
            int descuento = 0;
            int validez = 0;

            try {
                costo = Double.parseDouble(costoStr);
                if (!descuentoStr.isEmpty()) {
                    descuento = Integer.parseInt(descuentoStr);
                }
                if (!validezStr.isEmpty()) {
                    validez = Integer.parseInt(validezStr);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Costo debe ser un número válido. Descuento y Validez deben ser enteros.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

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
/*
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
*/
}
