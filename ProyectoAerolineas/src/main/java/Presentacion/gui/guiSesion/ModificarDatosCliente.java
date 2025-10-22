package Presentacion.gui.guiSesion;

import logica.Fabrica;
import logica.ISistema;
import logica.TipoDoc;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ModificarDatosCliente {
    private JPanel panelDeModificacion;
    private JTextField NomNuevo;
    private JTextField Apellido;
    private JTextField Nacionalidad;
    private JButton modificarButton;
    private JButton cancelarButton;
    private JRadioButton CiRadioButton;
    private JRadioButton PasaporteRadioButton;
    private JTextField Documento;
    private JComboBox DiaNacimiento;
    private JComboBox MesNacimiento;
    private JComboBox AnoNacimiento;
    private JPasswordField passwordField;
    private JPasswordField ConfirmarPassword;
    private JCheckBox cambiarPasswordCheckBox;
    private JLabel nuevaPasswordLabel;
    private JLabel confirmarPasswordLabel;

    private final String nickname;

    public ModificarDatosCliente(String nickname) {
        this.nickname = nickname;
        ISistema sistema = Fabrica.getInstance().getISistema();

        // Configurar estado inicial de campos de contraseña
        configurarCamposPassword();

        // Cargar datos actuales del cliente
        var cliente = sistema.obtenerCliente(nickname);
        if (cliente != null) {
            cargarDatosActuales(cliente);
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
                String nombre = NomNuevo.getText().trim();
                String apellido = Apellido.getText().trim();
                String nacionalidad = Nacionalidad.getText().trim();
                String documento = Documento.getText().trim();

                // Obtener fecha
                LocalDate fecha = obtenerFechaNacimiento();
                if (fecha == null) return;

                // Obtener tipo de documento
                TipoDoc tipoDoc = obtenerTipoDocumento();
                if (tipoDoc == null) return;

                // Modificar datos básicos
                sistema.modificarDatosDeCliente(nickname, nombre, apellido, nacionalidad, fecha, tipoDoc, documento);

                // Cambiar contraseña si está seleccionado
                if (cambiarPasswordCheckBox.isSelected()) {
                    String nuevaPassword = new String(passwordField.getPassword()).trim();

                    // Actualizar contraseña
                    sistema.actualizarPassword(cliente.getEmail(), nuevaPassword);
                }

                JOptionPane.showMessageDialog(null,
                        "Datos modificados correctamente." +
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

    private void cargarDatosActuales(DataTypes.DtCliente cliente) {
        NomNuevo.setText(cliente.getNombre());
        Apellido.setText(cliente.getApellido());
        Nacionalidad.setText(cliente.getNacionalidad());
        Documento.setText(cliente.getNumeroDocumento());

        if (cliente.getTipoDocumento() == TipoDoc.CEDULAIDENTIDAD) {
            CiRadioButton.setSelected(true);
        } else if (cliente.getTipoDocumento() == TipoDoc.PASAPORTE) {
            PasaporteRadioButton.setSelected(true);
        }

        LocalDate fecha = cliente.getFechaNacimiento();
        if (fecha != null) {
            DiaNacimiento.setSelectedItem(String.valueOf(fecha.getDayOfMonth()));
            MesNacimiento.setSelectedItem(String.valueOf(fecha.getMonthValue()));
            AnoNacimiento.setSelectedItem(String.valueOf(fecha.getYear()));
        }

        // Mostrar información del cliente en el título o tooltip
        panelDeModificacion.setToolTipText("Modificando datos de: " + cliente.getNombre() + " " + cliente.getApellido());
    }

    private boolean validarCampos() {
        String nombre = NomNuevo.getText().trim();
        String apellido = Apellido.getText().trim();
        String nacionalidad = Nacionalidad.getText().trim();
        String documento = Documento.getText().trim();

        // Validar campos obligatorios
        if (nombre.isEmpty() || apellido.isEmpty() || nacionalidad.isEmpty() || documento.isEmpty() ||
                (!CiRadioButton.isSelected() && !PasaporteRadioButton.isSelected())) {
            JOptionPane.showMessageDialog(null,
                    "Debe completar todos los campos básicos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validar fecha
        String diaStr = (String) DiaNacimiento.getSelectedItem();
        String mesStr = (String) MesNacimiento.getSelectedItem();
        String anioStr = (String) AnoNacimiento.getSelectedItem();

        if (diaStr.equals("Día") || mesStr.equals("Mes") || anioStr.equals("Año")) {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar una fecha válida.",
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

    private LocalDate obtenerFechaNacimiento() {
        try {
            String diaStr = (String) DiaNacimiento.getSelectedItem();
            String mesStr = (String) MesNacimiento.getSelectedItem();
            String anioStr = (String) AnoNacimiento.getSelectedItem();

            int dia = Integer.parseInt(diaStr);
            int mes = Integer.parseInt(mesStr);
            int anio = Integer.parseInt(anioStr);

            LocalDate fecha = LocalDate.of(anio, mes, dia);
            LocalDate hoy = LocalDate.now();

            // ✅ SOLO validar que no sea fecha futura (sin validación de edad)
            if (fecha.isAfter(hoy)) {
                JOptionPane.showMessageDialog(null,
                        "La fecha de nacimiento no puede ser futura.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            return fecha;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Error en el formato de la fecha.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private TipoDoc obtenerTipoDocumento() {
        if (CiRadioButton.isSelected()) {
            if (!Documento.getText().matches("\\d{8}")) {
                JOptionPane.showMessageDialog(null,
                        "La CI debe tener exactamente 8 números.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return TipoDoc.CEDULAIDENTIDAD;
        } else if (PasaporteRadioButton.isSelected()) {
            if (!Documento.getText().matches("[A-Z]{2}\\d{6}")) {
                JOptionPane.showMessageDialog(null,
                        "El pasaporte debe tener el formato: 2 letras + 6 números\nEjemplo: AB123456",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            return TipoDoc.PASAPORTE;
        } else {
            JOptionPane.showMessageDialog(null,
                    "Debe seleccionar un tipo de documento.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public Container getPanelDeVuelo() {
        return panelDeModificacion;
    }
}