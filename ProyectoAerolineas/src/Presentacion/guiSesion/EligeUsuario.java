package guiSesion;

import Logica.Aerolinea;
import Logica.Cliente;
import Logica.Fabrica;
import Logica.ISistema;

import javax.swing.*;
import java.awt.*;

public class EligeUsuario {
    private JTextField campoUsuarios;
    private JList ListaUsuarios;
    private JButton seleccionarButton;
    private JButton cancelarButton;
    private JPanel JPanelModificar;
    private String nicknameSeleccionado;
    private boolean esCliente;

    public EligeUsuario() {
        ISistema sistema = Fabrica.getInstance().getISistema();
        DefaultListModel<String> modelo = new DefaultListModel<>();

        if (sistema.listarClientes().size() > 0) {
            // --- Mostrar clientes ---
            for (Cliente c : sistema.listarClientes()) {
                String item = c.getNickname() + " (" + c.getNombre() + ")";
                modelo.addElement(item);
            }
            esCliente = true;
        } else {
            for (Aerolinea a : sistema.listarAerolineas()) {
                String item = a.getNickname() + " (" + a.getNombre() + ")";
                modelo.addElement(item);
            }
            esCliente = false;

            ListaUsuarios.setModel(modelo);

            ListaUsuarios.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    String item = (String) ListaUsuarios.getSelectedValue();
                    if (item != null) {
                        nicknameSeleccionado = item.split(" ")[0]; // guardo nickname
                        String nombre = item.substring(item.indexOf("(") + 1, item.indexOf(")"));
                        campoUsuarios.setText(nombre); // muestro solo el nombre
                    }
                }
            });

            seleccionarButton.addActionListener(e -> {
                if (nicknameSeleccionado == null || nicknameSeleccionado.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Debe seleccionar un usuario.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFrame frame;
                if (esCliente) {
                    // Abrir modificar cliente
                    frame = new JFrame("Modificar Cliente");
                    frame.setContentPane(new ModificarDatosCliente(nicknameSeleccionado).getPanelDeVuelo());
                } else {
                    // Abrir modificar aerolínea
                    frame = new JFrame("Modificar Aerolínea");
                    frame.setContentPane(new ModificarDatosAereolinea(nicknameSeleccionado).getPanelDeModificacion());
                }
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(campoUsuarios);
                topFrame.dispose();
            });

            cancelarButton.addActionListener(e -> {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(campoUsuarios);
                topFrame.dispose();
            });
        }
    }
    public Container getpanelModificar() {
        return JPanelModificar;
    }
}
