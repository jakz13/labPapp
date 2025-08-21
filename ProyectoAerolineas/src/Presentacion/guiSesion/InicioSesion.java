// InicioSesion.java
package guiSesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class InicioSesion {
    private JPanel PanelDeSesion;
    private JButton ALTAVUELOButton;
    private JButton ALTACIUDADButton;
    private JTextField NombreUsuario;
    private JTextField NombreCliente;
    private JTextField Apellido;
    private JButton crearButton;
    private JButton cancelarButton;
    private JTextField EmailUsuario;
    private JComboBox CiPasaporte;
    private JTextField Documento;
    private JRadioButton PasaporteRadioButton;
    private JRadioButton CiRadioButton;
    private JTextField NacionalidadCliente;
    private JComboBox DiaNacimiento;
    private JComboBox MesNacimiento;
    private JComboBox AnoNacimiento;

    public InicioSesion() {
        // Agrupa los JRadioButton para que solo uno pueda estar seleccionado
        ButtonGroup grupoDocumento = new ButtonGroup();
        grupoDocumento.add(PasaporteRadioButton);
        grupoDocumento.add(CiRadioButton);

        PanelDeSesion.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                super.componentShown(e);
            }
        });
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = NombreUsuario.getText();
                String apellido = Apellido.getText();
                String nombreCliente = NombreCliente.getText();
                String email = EmailUsuario.getText();
                String documento = Documento.getText();

                if (CiRadioButton.isSelected()) {
                    // Validar formato de CI (ejemplo: solo números, 7 u 8 dígitos)
                    if (!Documento.getText().matches("\\d{8}")) {
                        JOptionPane.showMessageDialog(null,
                                "La CI debe tener 8 números.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else if (PasaporteRadioButton.isSelected()) {
                    // Validar formato de pasaporte (ejemplo: 2 letras + 6 números)
                    if (!Documento.getText().matches("[A-Z]{2}\\d{6}")) {
                        JOptionPane.showMessageDialog(null,
                                "El pasaporte debe tener el formato: 2 letras y 6 números (ej: AB123456).",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Debe seleccionar CI o Pasaporte.",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        });
    }

    public Container getPanelDeSesion() {
        return PanelDeSesion;
    }
}