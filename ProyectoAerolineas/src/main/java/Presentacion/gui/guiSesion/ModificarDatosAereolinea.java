package Presentacion.gui.guiSesion;

import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;

public class ModificarDatosAereolinea {
    private JPanel panelDeModificacion;
    private JTextField URL;
    private JButton modificarButton;
    private JButton cancelarButton;
    private JTextArea Descripcion;
    private JPasswordField passwordField;
    private JPasswordField ConfirmarPassword;
    private JCheckBox cambiarPasswordCheckBox;
    private JLabel nuevaPasswordLabel;
    private JLabel confirmarPasswordLabel;

    private final String nickname;

    public ModificarDatosAereolinea(String nickname) {
        this.nickname = nickname;
        ISistema sistema = Fabrica.getInstance().getISistema();

        // Configurar estado inicial de campos de contraseña
        configurarCamposPassword();

        // Cargar datos actuales de la aerolínea
        var aerolinea = sistema.obtenerAerolinea(nickname);
        if (aerolinea != null) {
            cargarDatosActuales(aerolinea);
        }

        // Listener para el checkbox de cambiar contraseña
        cambiarPasswordCheckBox.addActionListener(e -> {
            boolean seleccionado = cambiarPasswordCheckBox.isSelected();
            toggleCamposPassword(seleccionado);
        });

        modificarButton.addActionListener(e -> {
            if (!validarCampos()) return;

            try {
                // Obtener datos del formulario
                String nuevaDescripcion = Descripcion.getText().trim();
                String nuevaURL = URL.getText().trim();

                // Modificar datos básicos
                sistema.modificarDatosAerolinea(nickname, nuevaDescripcion, nuevaURL);

                // Cambiar contraseña si está seleccionado
                if (cambiarPasswordCheckBox.isSelected()) {
                    String nuevaPassword = new String(passwordField.getPassword()).trim();

                    // Actualizar contraseña
                    sistema.actualizarPassword(aerolinea.getEmail(), nuevaPassword);
                }

                JOptionPane.showMessageDialog(null,
                        "Datos de la aerolínea modificados correctamente." +
                                (cambiarPasswordCheckBox.isSelected() ? "\nContraseña actualizada correctamente." : ""),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // Cerrar ventana
                Window window = SwingUtilities.getWindowAncestor(panelDeModificacion);
                if (window != null) {
                    window.dispose();
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null,
                        "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error inesperado: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelarButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(panelDeModificacion);
            if (window != null) {
                window.dispose();
            }
        });
    }

    private void configurarCamposPassword() {
        // Inicialmente deshabilitar campos de contraseña
        toggleCamposPassword(false);

        // Tooltips para ayudar al usuario
        passwordField.setToolTipText("La nueva contraseña debe tener al menos 6 caracteres, incluir mayúsculas, minúsculas y números");
        ConfirmarPassword.setToolTipText("Repita la nueva contraseña para confirmar");
    }

    private void toggleCamposPassword(boolean habilitar) {
        nuevaPasswordLabel.setEnabled(habilitar);
        passwordField.setEnabled(habilitar);
        confirmarPasswordLabel.setEnabled(habilitar);
        ConfirmarPassword.setEnabled(habilitar);

        if (!habilitar) {
            passwordField.setText("");
            ConfirmarPassword.setText("");
        } else {
            passwordField.requestFocus();
        }
    }

    private void cargarDatosActuales(DataTypes.DtAerolinea aerolinea) {
        Descripcion.setText(aerolinea.getDescripcion());
        URL.setText(aerolinea.getSitioWeb());

        // Mostrar información de la aerolínea en el título o tooltip
        panelDeModificacion.setToolTipText("Modificando datos de: " + aerolinea.getNombre());
    }

    private boolean validarCampos() {
        String descripcion = Descripcion.getText().trim();
        String url = URL.getText().trim();

        // Validar campos obligatorios
        if (descripcion.isEmpty() || url.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Debe completar todos los campos básicos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar contraseña si está habilitado
        if (cambiarPasswordCheckBox.isSelected()) {
            String nuevaPassword = new String(passwordField.getPassword()).trim();
            String confirmacion = new String(ConfirmarPassword.getPassword()).trim();

            if (nuevaPassword.isEmpty() || confirmacion.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Debe completar ambos campos de nueva contraseña.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!nuevaPassword.equals(confirmacion)) {
                JOptionPane.showMessageDialog(null,
                        "Las nuevas contraseñas no coinciden.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                ConfirmarPassword.setText("");
                passwordField.requestFocus();
                return false;
            }

            if (nuevaPassword.length() < 6) {
                JOptionPane.showMessageDialog(null,
                        "La nueva contraseña debe tener al menos 6 caracteres.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                ConfirmarPassword.setText("");
                passwordField.requestFocus();
                return false;
            }

            if (!nuevaPassword.matches(".*[A-Z].*") || !nuevaPassword.matches(".*[a-z].*") || !nuevaPassword.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(null,
                        "La nueva contraseña debe contener:\n- Al menos una letra mayúscula\n- Al menos una letra minúscula\n- Al menos un número",
                        "Error", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                ConfirmarPassword.setText("");
                passwordField.requestFocus();
                return false;
            }
        }

        return true;
    }

    public Container getPanelDeModificacion() {
        return panelDeModificacion;
    }
}