// Supón que tienes una clase Cliente con los métodos getId(), getNombre(), getApellido(), getDocumento()
package guiSesion;


import Logica.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DesplegarUsuarios {
    private JPanel PanelUsuarios;
    public JTable TablaUsuarios;

    public DesplegarUsuarios(List<Cliente> clientes) {
        PanelUsuarios = new JPanel(new BorderLayout());

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");

        for (Cliente c : clientes) {
            modelo.addRow(new Object[]{c.getNombre(), c.getApellido()});
        }

        TablaUsuarios = new JTable(modelo);
        TablaUsuarios.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(TablaUsuarios);
        PanelUsuarios.add(scrollPane, BorderLayout.CENTER);
    }

    public Container getPanelUsuarios() {
        return PanelUsuarios;
    }
}