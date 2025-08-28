package guiSesion;

import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AltaAereolinea {
    private JPanel PanelDeSesion;
    private JTextField NombreUsuario;   // -> será el "nickname"
    private JTextField NombreAereolinea;
    private JTextArea Descripcion;
    private JTextField EmailUsuario;
    private JButton crearButton;
    private JButton cancelarButton;
    private JTextField Link;

    public AltaAereolinea() {
        ISistema sistema = Fabrica.getInstance().getISistema();

        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname   = NombreUsuario.getText().trim();
                String nombre     = NombreAereolinea.getText().trim();
                String descripcion = Descripcion.getText().trim();
                String correo     = EmailUsuario.getText().trim();
                String sitioWeb = Link.getText().trim();

                // Relleno los campos?
                if (nickname.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Debe completar todos los campos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Correo electrónico no válido.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    sistema.altaAerolinea(nickname, nombre, descripcion, correo, sitioWeb);
                    JOptionPane.showMessageDialog(null,
                            "Aereolinea creada correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    // Mostrar ventana con las aerolíneas registradas
                    /* DesplegarAerolineas ventana = new DesplegarAerolineas(sistema.listarAerolineas());
                    DefaultTableModel modelo = (DefaultTableModel) ventana.TablaAerolineas.getModel();
                    modelo.addRow(new Object[]{nickname, nombre, correo});
                    */
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
                Window window = SwingUtilities.getWindowAncestor(PanelDeSesion);
                if (window != null) {
                    window.dispose();
                }
            }
        });
    }

    public Container getPanelDeSesion() {
        return PanelDeSesion;
    }
}