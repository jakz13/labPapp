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


                // Validaciones específicas de campos
                if (nombreUsuario.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El nombre de usuario es obligatorio.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nombreCliente.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El nombre del cliente es obligatorio.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (apellido.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El apellido es obligatorio.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (email.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El email es obligatorio.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (documento.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El documento es obligatorio.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nacionalidad.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "La nacionalidad es obligatoria.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!CiRadioButton.isSelected() && !PasaporteRadioButton.isSelected()) {
                    JOptionPane.showMessageDialog(null,
                            "Debe seleccionar un tipo de documento.",
                            "Error", JOptionPane.ERROR_MESSAGE);
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
                try {
                    sistema.altaCliente(nombreUsuario, nombreCliente, apellido, email, fecha, nacionalidad, tipoDoc, documento);
                    JOptionPane.showMessageDialog(null,
                            "Usuario creado correctamente.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar campos después del éxito
                    NombreUsuario.setText("");
                    NombreCliente.setText("");
                    Apellido.setText("");
                    EmailUsuario.setText("");
                    Documento.setText("");
                    NacionalidadCliente.setText("");
                    grupoDocumento.clearSelection();
                    DiaNacimiento.setSelectedItem("Día");
                    MesNacimiento.setSelectedItem("Mes");
                    AnoNacimiento.setSelectedItem("Año");

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error inesperado al crear el usuario: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
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