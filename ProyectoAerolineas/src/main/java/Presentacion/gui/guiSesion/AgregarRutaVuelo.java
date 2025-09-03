package Presentacion.gui.guiSesion;

import Logica.Fabrica;
import Logica.ISistema;
import Logica.Sistema;
import javax.swing.*;


public class AgregarRutaVuelo {
    private JComboBox campoPaquete;
    private JComboBox campoAerolinea;
    private JList campoRutaVuelo;
    private JComboBox campoTipoAsiento;
    private JTextField campoCantidadAsiento;
    private JButton añadirButton;
    private JButton cancelarButton;


    public AgregarRutaVuelo() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        añadirButton.addActionListener(e -> {
            // Lógica para agregar la ruta de vuelo al paquete
            String paquete = (String) campoPaquete.getSelectedItem();
            String aerolinea = (String) campoAerolinea.getSelectedItem();
            Object rutaVuelo = campoRutaVuelo.getSelectedValue();
            String tipoAsiento = (String) campoTipoAsiento.getSelectedItem();
            String cantidadAsientoStr = campoCantidadAsiento.getText().trim();

            if (paquete == null || aerolinea == null || rutaVuelo == null || tipoAsiento == null || cantidadAsientoStr.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar todos los campos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cantidadAsiento;
            try {
                cantidadAsiento = Integer.parseInt(cantidadAsientoStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Cantidad de Asientos debe ser un número entero válido.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Aquí se llamaría al método del sistema para agregar la ruta de vuelo al paquete
            // sistema.agregarRutaVueloAlPaquete(paquete, aerolinea, rutaVuelo, tipoAsiento, cantidadAsiento);

            JOptionPane.showMessageDialog(null,
                    "Ruta de vuelo agregada correctamente al paquete.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        cancelarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(campoPaquete);
            topFrame.dispose();
        });
    }
}