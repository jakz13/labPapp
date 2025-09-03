// InicioSesion.java
package Presentacion.gui.guiSesion;


import Logica.Fabrica;
import Logica.ISistema;
import Logica.TipoDoc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;


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
    private JTextField Documento;
    private JRadioButton PasaporteRadioButton;
    private JRadioButton CiRadioButton;
    private JTextField NacionalidadCliente;
    private JComboBox DiaNacimiento;
    private JComboBox MesNacimiento;
    private JComboBox AnoNacimiento;

    public InicioSesion() {
        // Agrupa los JRadioButton para que solo uno pueda estar seleccionado
        ISistema sistema = Fabrica.getInstance().getISistema();
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
                String nacionalidad = NacionalidadCliente.getText();

                TipoDoc tipoDoc;

                String diaStr = (String) DiaNacimiento.getSelectedItem();
                String mesStr = (String) MesNacimiento.getSelectedItem();
                String anioStr = (String) AnoNacimiento.getSelectedItem();


                if (nombreUsuario.isEmpty() || nombreCliente.isEmpty() || email.isEmpty() || documento.isEmpty() || apellido.isEmpty() ||  (!CiRadioButton.isSelected() && !PasaporteRadioButton.isSelected())) {
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

                int dia = Integer.parseInt(diaStr);
                int mes = Integer.parseInt(mesStr);
                int anio = Integer.parseInt(anioStr);

                LocalDate fecha = LocalDate.of(anio, mes, dia);

                if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Correo electrónico no válido.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (CiRadioButton.isSelected()) {
                    // Validar formato de CI (8 dígitos)
                    if (!Documento.getText().matches("\\d{8}")) {
                        JOptionPane.showMessageDialog(null,
                                "La CI debe tener 8 números.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tipoDoc = TipoDoc.CI;
                } else if (PasaporteRadioButton.isSelected()) {
                    // Validar formato de pasaporte (2 letras + 6 números)
                    if (!Documento.getText().matches("[A-Z]{2}\\d{6}")) {
                        JOptionPane.showMessageDialog(null,
                                "El pasaporte debe tener el formato: 2 letras y 6 números (ej: AB123456).",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tipoDoc = TipoDoc.PASAPORTE;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Debe seleccionar CI o Pasaporte.",
                            "Error", JOptionPane.WARNING_MESSAGE);
                    return;

                }

                sistema.altaCliente(nombreUsuario, nombreCliente, apellido, email, fecha, nacionalidad, tipoDoc, documento);
                DesplegarUsuarios ventanaUsuarios = new DesplegarUsuarios(sistema.listarClientes());
                DefaultTableModel modelo = (DefaultTableModel) ventanaUsuarios.TablaUsuarios.getModel();
                modelo.addRow(new Object[]{NombreUsuario, Apellido, Documento});
                JOptionPane.showMessageDialog(null,
                        "Usuario creado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                // DesplegarUsuarios.addRow(new Object[]{NombreUsuario, Apellido, Documento});
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