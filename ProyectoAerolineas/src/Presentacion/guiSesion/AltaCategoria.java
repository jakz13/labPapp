package guiSesion;

import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;

public class AltaCategoria {
    private JPanel panel1;
    private JTextField campoNomCategoria;
    private JButton aceptarButton;
    private JButton cancelarButton;

    AltaCategoria(){
        ISistema sistema = Fabrica.getInstance().getISistema();

        aceptarButton.addActionListener(e -> {
            String nomCategoria = campoNomCategoria.getText().trim();

            if (nomCategoria.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar el campo obligatorio: Nombre de la Categoría.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            sistema.altaCategoria(nomCategoria);
            JOptionPane.showMessageDialog(null,
                    "Categoría creada exitosamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            campoNomCategoria.setText("");
        });

        cancelarButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
            topFrame.dispose();
        });
    }
}
